package com.techtraining.cosechasapp.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.techtraining.cosechasapp.DBManager;
import com.techtraining.cosechasapp.NuevaCosechaActivity;
import com.techtraining.cosechasapp.db.AppDatabase;
import com.techtraining.cosechasapp.db.Tarifa;

public class CalcularFlete extends AsyncTask<Void, Void, Void> {
    private Context context;
    private AppDatabase appDatabase;
    private int origenMaderaId;
    private  int destinoId;
    private Tarifa tarifa;
    public CalcularFlete(Context context, int origenMaderaId, int destinoId) {
        this.context = context;
        this.origenMaderaId = origenMaderaId;
        this.destinoId = destinoId;
    }
    @Override
    protected Void doInBackground(Void... voids) {
        appDatabase = DBManager.getInstance(context);
        tarifa = appDatabase.tarifaDao().loadByIdOrigenDestino(origenMaderaId, destinoId);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        NuevaCosechaActivity activity = (NuevaCosechaActivity) context;
        if (tarifa != null) {

            activity.etValorFlete.setText(String.valueOf(tarifa.valor));
        }
        else {
            activity.etValorFlete.setText(String.valueOf(0));
        }
    }

}
