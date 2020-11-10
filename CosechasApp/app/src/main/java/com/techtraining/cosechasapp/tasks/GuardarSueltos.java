package com.techtraining.cosechasapp.tasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
import com.techtraining.cosechasapp.db.FormatoEntrega;
import com.techtraining.cosechasapp.db.Largo;
import com.techtraining.cosechasapp.db.ParametroDao;
import com.techtraining.cosechasapp.db.TipoBulto;

import java.text.DecimalFormat;
import java.util.List;
import java.util.UUID;

public class GuardarSueltos extends AsyncTask<Void, Void, Void> {
    private Context context;
    private AppDatabase appDatabase;
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
        if (filaCosecha != null) {
            Cosecha cosecha = appDatabase.cosechaDao().loadById(filaCosecha.cosechaId);
            if (cosecha != null) {
                FormatoEntrega formatoEntrega = appDatabase.formatoEntregaDao().loadById(cosecha.formatoEntregaId);
                if (formatoEntrega != null) {
                    Camion camion = appDatabase.camionDao().loadById(cosecha.camionId);
                    if (camion != null) {
                        SharedPreferences preferences = context.getSharedPreferences(Helper.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
                        /*if (filaCosecha.tipo.equals("E")) {
                            List<FilaSuelto> filaSueltoExistentes = filaSueltoDao.loadByFila(filaCosecha.id);
                            if (filaSueltoExistentes.size() > 0) {
                                filaSueltoId = filaSueltoExistentes.get(0).id;
                            }
                        }
                        else {*/
                        String filaSueltoId = preferences.getString(Helper.CURRENT_ITEM_SUELTO_NAME, null);
                        //}
                        Largo largo = appDatabase.largoDao().loadById(filaSuelto.largoId);
                        Espesor espesor = appDatabase.espesorDao().loadById(filaSuelto.espesorId);
                        double bft = 0;
                        if (largo != null && espesor != null) {
                            double valorLargo = 0;
                            double valorEspesor = 0;
                            if (largo != null)
                                valorLargo = largo.valor;
                            if (espesor != null)
                                valorEspesor = espesor.valor;
                            bft = (((camion.ancho * valorLargo * valorEspesor) / 12) * formatoEntrega.factorHueco) * filaSuelto.bultos;
                        }
                        else {
                            TipoBulto tipoBulto = appDatabase.tipoBultoDao().loadById(filaSuelto.tipoBultoId);
                            if (tipoBulto != null) {
                                largo = appDatabase.largoDao().loadById(tipoBulto.largoId);
                                espesor = appDatabase.espesorDao().loadById(tipoBulto.espesorId);
                                if (largo != null && espesor != null) {
                                    bft = (tipoBulto.ancho * largo.valor) * formatoEntrega.factorHueco * filaSuelto.bultos * espesor.valor;
                                }
                            }
                        }
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
                        int bultosTotales = 0;
                        for (int i = 0; i < sueltos.size(); i++) {
                            bftTotal += sueltos.get(i).bft;
                            bultosTotales += sueltos.get(i).bultos;
                        }
                        filaCosecha.bft = Double.parseDouble(df.format(bftTotal));
                        filaCosecha.bultos = bultosTotales;
                        filaCosechaDao.update(filaCosecha);
                    }
                }
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        Toast.makeText(context, R.string.bloque_guardado, Toast.LENGTH_SHORT).show();
        ((AppCompatActivity) context).finish();
    }
}
