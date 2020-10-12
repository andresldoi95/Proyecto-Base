package com.techtraining.cosechasapp.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;

import com.techtraining.cosechasapp.DBManager;
import com.techtraining.cosechasapp.R;
import com.techtraining.cosechasapp.db.AppDatabase;
import com.techtraining.cosechasapp.db.Cosecha;
import com.techtraining.cosechasapp.db.Espesor;
import com.techtraining.cosechasapp.db.FilaCosecha;
import com.techtraining.cosechasapp.db.Largo;
import com.techtraining.cosechasapp.db.Tarifa;
import com.techtraining.cosechasapp.db.TipoBulto;

public class CargarDatosFila extends AsyncTask<Void, Void, Void> {
    private Context context;
    private AppDatabase appDatabase;
    private FilaCosecha filaCosecha;
    private  Cosecha cosecha;
    private  TipoBulto tipoBulto;
    private TextView tvInformacion;
    private Espesor espesor;
    private Largo largo;
    private Tarifa tarifa;
    public CargarDatosFila(Context context, FilaCosecha filaCosecha, TextView tvInformacion) {
        this.context = context;
        this.filaCosecha = filaCosecha;
        this.tvInformacion = tvInformacion;
    }
    @Override
    protected Void doInBackground(Void... voids) {
        appDatabase = DBManager.getInstance(context);
        cosecha = appDatabase.cosechaDao().loadById(filaCosecha.cosechaId);
        if (cosecha != null && cosecha.tipoLlenado.equals("B")){
            tipoBulto = appDatabase.tipoBultoDao().loadById(filaCosecha.tipoBultoId);
            if (tipoBulto != null) {
                espesor = appDatabase.espesorDao().loadById(tipoBulto.espesorId);
                largo = appDatabase.largoDao().loadById(tipoBulto.largoId);
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (cosecha != null && tipoBulto != null) {
            String informacion = "";
            informacion += "\n" + context.getString(R.string.plantilla) + ": " + filaCosecha.bultos;
            informacion += "\n" + context.getString(R.string.ancho) + ": " + tipoBulto.ancho;
            if (espesor != null)
                informacion += "\n" +  context.getString(R.string.espesor) + ": " + espesor.valor;
            if (espesor != null)
                informacion += "\n" +  context.getString(R.string.largo) + ": " + largo.valor;
            tvInformacion.setText(informacion);
        }
        else
            tvInformacion.setText("");
    }
}
