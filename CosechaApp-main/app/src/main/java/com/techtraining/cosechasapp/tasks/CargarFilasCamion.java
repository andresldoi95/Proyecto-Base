package com.techtraining.cosechasapp.tasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.techtraining.cosechasapp.DBManager;
import com.techtraining.cosechasapp.Helper;
import com.techtraining.cosechasapp.LlenadoCamionActivity;
import com.techtraining.cosechasapp.adapters.FilasCosechasAdapter;
import com.techtraining.cosechasapp.db.AppDatabase;
import com.techtraining.cosechasapp.db.Cosecha;
import com.techtraining.cosechasapp.db.FilaCosecha;

import java.util.List;

public class CargarFilasCamion extends AsyncTask<Void, Void, Void> {
    private Context context;
    private AppDatabase appDatabase;
    private List<FilaCosecha> filas;
    private Cosecha cosecha;
    public CargarFilasCamion(Context context) {
        this.context = context;
    }
    @Override
    protected Void doInBackground(Void... voids) {
        appDatabase = DBManager.getInstance(context);
        SharedPreferences preferences = context.getSharedPreferences(Helper.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        String id = preferences.getString(Helper.CURRENT_COSECHA_ID_NAME, null);
        boolean isHistorial = context.getSharedPreferences(Helper.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).getBoolean(Helper.IS_HISTORIAL, false);
        if(isHistorial){
            id = context.getSharedPreferences(Helper.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).getString(Helper.CURRENT_COSECHA_ID_HISTORIAL, null);
        }
        if (id != null) {
            cosecha = appDatabase.cosechaDao().loadById(id);
            filas = appDatabase.filaCosechaDao().loadByCosecha(cosecha.id);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (filas != null) {
            final LlenadoCamionActivity activity = (LlenadoCamionActivity) context;
            activity.lvFilas.setAdapter(new FilasCosechasAdapter(filas, context, cosecha));
        }
    }
}
