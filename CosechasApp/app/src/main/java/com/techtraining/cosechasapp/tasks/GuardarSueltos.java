package com.techtraining.cosechasapp.tasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import com.techtraining.cosechasapp.DBManager;
import com.techtraining.cosechasapp.Helper;
import com.techtraining.cosechasapp.ItemSueltosActivity;
import com.techtraining.cosechasapp.R;
import com.techtraining.cosechasapp.db.AppDatabase;
import com.techtraining.cosechasapp.db.Camion;
import com.techtraining.cosechasapp.db.Cosecha;
import com.techtraining.cosechasapp.db.Espesor;
import com.techtraining.cosechasapp.db.FilaCosecha;
import com.techtraining.cosechasapp.db.FilaCosechaDao;
import com.techtraining.cosechasapp.db.FilaSuelto;
import com.techtraining.cosechasapp.db.FilaSueltoDao;
import com.techtraining.cosechasapp.db.Largo;
import com.techtraining.cosechasapp.db.Parametro;
import com.techtraining.cosechasapp.db.ParametroDao;

import java.text.DecimalFormat;
import java.util.List;
import java.util.UUID;

public class GuardarSueltos extends AsyncTask<Void, Void, Void> {
    private Context context;
    private AppDatabase appDatabase;
    private List<Parametro> parametros;
    private FilaSuelto filaSuelto;
    public GuardarSueltos(Context context, FilaSuelto filaSuelto) {
        this.context = context;
        this.filaSuelto = filaSuelto;
    }
    @Override
    protected Void doInBackground(Void... voids) {
        appDatabase = DBManager.getInstance(context);
        FilaSueltoDao filaSueltoDao = appDatabase.filaSueltoDao();
        ParametroDao parametroDao = appDatabase.parametroDao();
        FilaCosechaDao filaCosechaDao = appDatabase.filaCosechaDao();
        FilaCosecha filaCosecha = filaCosechaDao.loadById(filaSuelto.filaId);
        parametros = parametroDao.getAll();
        if (parametros.size() > 0 && filaCosecha != null) {
            Cosecha cosecha = appDatabase.cosechaDao().loadById(filaCosecha.cosechaId);
            if (cosecha != null) {
                Camion camion = appDatabase.camionDao().loadById(cosecha.camionId);
                if (camion != null) {
                    Parametro parametro = parametros.get(0);
                    SharedPreferences preferences = context.getSharedPreferences(Helper.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
                    String filaSueltoId = preferences.getString(Helper.CURRENT_ITEM_SUELTO_NAME, null);
                    Largo largo = appDatabase.largoDao().loadById(filaSuelto.largoId);
                    Espesor espesor = appDatabase.espesorDao().loadById(filaSuelto.espesorId);
                    double valorLargo = 0;
                    double valorEspesor = 0;
                    if (largo != null)
                        valorLargo = largo.valor;
                    if (espesor != null)
                        valorEspesor = espesor.valor;
                    double bft = (((camion.ancho * valorLargo * valorEspesor) / 12) * parametro.factorHuecoSueltos) * filaSuelto.bultos;
                    DecimalFormat df = new DecimalFormat("#.##");
                    filaSuelto.bft = Double.parseDouble(df.format(bft));
                    if (filaSueltoId == null) {
                        filaSueltoId = UUID.randomUUID().toString();
                        filaSuelto.id = filaSueltoId;
                        filaSueltoDao.insertOne(filaSuelto);
                    }
                    else {
                        filaSuelto.id = filaSueltoId;
                        filaSueltoDao.update(filaSuelto);
                    }
                    List<FilaSuelto> sueltos = filaSueltoDao.loadByFila(filaSuelto.filaId);
                    double bftTotal = 0;
                    for (int i = 0; i < sueltos.size(); i++) {
                        bftTotal += sueltos.get(i).bft;
                    }
                    filaCosecha.bft = Double.parseDouble(df.format(bftTotal));
                    filaCosechaDao.update(filaCosecha);
                }
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (parametros.size() > 0){
            Toast.makeText(context, R.string.bloque_guardado, Toast.LENGTH_SHORT).show();
            ((ItemSueltosActivity) context).finish();
        }
        else
            Toast.makeText(context, R.string.parametros_requeridos, Toast.LENGTH_SHORT).show();
    }
}