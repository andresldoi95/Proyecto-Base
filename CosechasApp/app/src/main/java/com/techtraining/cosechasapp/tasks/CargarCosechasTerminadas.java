package com.techtraining.cosechasapp.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.techtraining.cosechasapp.CosechasTerminadasActivity;
import com.techtraining.cosechasapp.DBManager;
import com.techtraining.cosechasapp.adapters.CosechasTerminadasAdapter;
import com.techtraining.cosechasapp.db.AppDatabase;
import com.techtraining.cosechasapp.db.Cosecha;

import java.util.List;

public class CargarCosechasTerminadas extends AsyncTask<Void, Void, Void> {
    private Context context;
    private List<Cosecha> cosechas;
    public CargarCosechasTerminadas(Context context) {
        this.context = context;
    }
    @Override
    protected Void doInBackground(Void... voids) {
        AppDatabase appDatabase = DBManager.getInstance(context);
        cosechas = appDatabase.cosechaDao().getFinalizadas();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        CosechasTerminadasActivity activity = (CosechasTerminadasActivity) context;
        activity.lvCosechas.setAdapter(new CosechasTerminadasAdapter(cosechas, context));
    }
}
