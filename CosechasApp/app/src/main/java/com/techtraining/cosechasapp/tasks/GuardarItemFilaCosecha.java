package com.techtraining.cosechasapp.tasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import com.techtraining.cosechasapp.DBManager;
import com.techtraining.cosechasapp.Helper;
import com.techtraining.cosechasapp.ItemCosechaActivity;
import com.techtraining.cosechasapp.R;
import com.techtraining.cosechasapp.db.AppDatabase;
import com.techtraining.cosechasapp.db.CosechaDao;
import com.techtraining.cosechasapp.db.Espesor;
import com.techtraining.cosechasapp.db.FilaCosecha;
import com.techtraining.cosechasapp.db.FilaCosechaDao;
import com.techtraining.cosechasapp.db.Largo;
import com.techtraining.cosechasapp.db.Parametro;
import com.techtraining.cosechasapp.db.ParametroDao;
import com.techtraining.cosechasapp.db.TipoBulto;

import java.text.DecimalFormat;
import java.util.List;

public class GuardarItemFilaCosecha extends AsyncTask<Void, Void, Void> {
    private Context context;
    private AppDatabase appDatabase;
    private List<Parametro> parametros;
    private FilaCosecha filaCosecha;
    public GuardarItemFilaCosecha(Context context, FilaCosecha filaCosecha) {
        this.context = context;
        this.filaCosecha = filaCosecha;
    }
    @Override
    protected Void doInBackground(Void... voids) {
        appDatabase = DBManager.getInstance(context);
        FilaCosechaDao filaCosechaDao = appDatabase.filaCosechaDao();
        ParametroDao parametroDao = appDatabase.parametroDao();
        CosechaDao cosechaDao = appDatabase.cosechaDao();
        parametros = parametroDao.getAll();
        if (parametros.size() > 0) {
            Parametro parametro = parametros.get(0);
            SharedPreferences preferences = context.getSharedPreferences(Helper.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
            FilaCosecha filaCosechaExistente = filaCosechaDao.loadById(preferences.getString(Helper.CURRENT_FILA_NAME, null));
            if (filaCosechaExistente != null) {
                filaCosechaExistente.tipoBultoId = filaCosecha.tipoBultoId;
                filaCosechaExistente.bultos = filaCosecha.bultos;
                TipoBulto tipoBulto = appDatabase.tipoBultoDao().loadById(filaCosechaExistente.tipoBultoId);
                Largo largo = appDatabase.largoDao().loadById(tipoBulto.largoId);
                Espesor espesor = appDatabase.espesorDao().loadById(tipoBulto.espesorId);
                DecimalFormat df = new DecimalFormat("#.##");
                double bft = (tipoBulto.ancho * largo.valor) * parametro.factorHuecoBultos * filaCosechaExistente.bultos * espesor.valor;
                filaCosechaExistente.bft = Double.parseDouble(df.format(bft));
                filaCosechaDao.update(filaCosechaExistente);
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (parametros.size() > 0){
            Toast.makeText(context, R.string.bloque_guardado, Toast.LENGTH_SHORT).show();
            ((ItemCosechaActivity) context).finish();
        }
        else
            Toast.makeText(context, R.string.parametros_requeridos, Toast.LENGTH_SHORT).show();
    }
}
