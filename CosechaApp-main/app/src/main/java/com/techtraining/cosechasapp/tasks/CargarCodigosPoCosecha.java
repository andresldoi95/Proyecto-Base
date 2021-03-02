package com.techtraining.cosechasapp.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.techtraining.cosechasapp.DBManager;
import com.techtraining.cosechasapp.NuevaCosechaActivity;
import com.techtraining.cosechasapp.db.AppDatabase;
import com.techtraining.cosechasapp.db.Tarifa;
import com.techtraining.cosechasapp.db.TarifaTipoCamion;

public class CalcularFlete extends AsyncTask<Void, Void, Void> {
    private Context context;
    private AppDatabase appDatabase;
    private int origenMaderaId;
    private  int destinoId;
    private  String tipoCamion;

    private Tarifa tarifa;
    private TarifaTipoCamion tarifaTipoCamion;

    public CalcularFlete(Context context, int origenMaderaId, int destinoId, String tipoCamion) {
        this.context = context;
        this.origenMaderaId = origenMaderaId;
        this.destinoId = destinoId;
        this.tipoCamion = tipoCamion;

    }
    @Override
    protected Void doInBackground(Void... voids) {
        try{
            appDatabase = DBManager.getInstance(context);
            tarifa = appDatabase.tarifaDao().loadByIdOrigenDestino(origenMaderaId, destinoId);
            tarifaTipoCamion = appDatabase.tarifaTipoCamionDao().loadByIdOTipoCamion(tarifa.id, tipoCamion);
        }catch (Exception e){

        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        NuevaCosechaActivity activity = (NuevaCosechaActivity) context;
        if (tarifaTipoCamion != null) {

            activity.etValorFlete.setText(String.valueOf(tarifaTipoCamion.valor));
        }
        else {
            activity.etValorFlete.setText(String.valueOf(0));
        }
    }

}
