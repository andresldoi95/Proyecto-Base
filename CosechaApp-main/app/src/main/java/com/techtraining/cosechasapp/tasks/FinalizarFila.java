package com.techtraining.cosechasapp.tasks;

import android.content.Context;

import android.os.AsyncTask;
import android.widget.Toast;

import com.techtraining.cosechasapp.DBManager;

import com.techtraining.cosechasapp.LlenadoCamionActivity;
import com.techtraining.cosechasapp.R;
import com.techtraining.cosechasapp.db.AppDatabase;
import com.techtraining.cosechasapp.db.FilaCosecha;


public class FinalizarFila extends AsyncTask<Void, Void, Void> {
    private Context context;
    private AppDatabase appDatabase;
    private FilaCosecha filaCosecha;
    public FinalizarFila(Context context, FilaCosecha filaCosecha) {
        this.context = context;
        this.filaCosecha = filaCosecha;
    }
    @Override
    protected Void doInBackground(Void... voids) {
        appDatabase = DBManager.getInstance(context);
        filaCosecha.estado = "F";
        appDatabase.filaCosechaDao().update(filaCosecha);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        Toast.makeText(context, R.string.fila_finalizada, Toast.LENGTH_SHORT).show();
        LlenadoCamionActivity activity = (LlenadoCamionActivity) context;
        activity.cargarDatos();
    }
}
