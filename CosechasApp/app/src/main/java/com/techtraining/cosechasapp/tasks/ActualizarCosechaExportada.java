package com.techtraining.cosechasapp.tasks;

import android.content.Context;
import android.os.AsyncTask;
import com.techtraining.cosechasapp.DBManager;
import com.techtraining.cosechasapp.db.AppDatabase;
import com.techtraining.cosechasapp.db.Cosecha;

public class ActualizarCosechaExportada extends AsyncTask<Void, Void, Void> {
    private Context context;
    private AppDatabase appDatabase;
    private Cosecha cosecha;
    public ActualizarCosechaExportada(Context context, Cosecha cosecha) {
        this.context = context;
        this.cosecha = cosecha;
    }
    @Override
    protected Void doInBackground(Void... voids) {
        appDatabase = DBManager.getInstance(context);
        cosecha.estado = "E";
        appDatabase.cosechaDao().update(cosecha);
        return null;
    }
}
