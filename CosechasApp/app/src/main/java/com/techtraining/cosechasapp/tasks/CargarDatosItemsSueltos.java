package com.techtraining.cosechasapp.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;

import com.techtraining.cosechasapp.DBManager;
import com.techtraining.cosechasapp.R;
import com.techtraining.cosechasapp.db.AppDatabase;
import com.techtraining.cosechasapp.db.Espesor;
import com.techtraining.cosechasapp.db.FilaSuelto;
import com.techtraining.cosechasapp.db.Largo;

public class CargarDatosItemsSueltos extends AsyncTask<Void, Void, Void> {
    private Context context;
    private AppDatabase appDatabase;
    private FilaSuelto filaSuelto;
    private TextView tvInformacion;
    private Espesor espesor;
    private Largo largo;
    public CargarDatosItemsSueltos(Context context, FilaSuelto filaSuelto, TextView tvInformacion) {
        this.context = context;
        this.filaSuelto = filaSuelto;
        this.tvInformacion = tvInformacion;
    }
    @Override
    protected Void doInBackground(Void... voids) {
        appDatabase = DBManager.getInstance(context);
        espesor = appDatabase.espesorDao().loadById(filaSuelto.espesorId);
        largo = appDatabase.largoDao().loadById(filaSuelto.largoId);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        String informacion = "";
        informacion += "\n" + context.getString(R.string.plantilla) + ": " + filaSuelto.bultos;
        if (espesor != null)
            informacion += "\n" +  context.getString(R.string.espesor) + ": " + espesor.valor;
        if (espesor != null)
            informacion += "\n" +  context.getString(R.string.largo) + ": " + largo.valor;
        tvInformacion.setText(informacion);
    }
}