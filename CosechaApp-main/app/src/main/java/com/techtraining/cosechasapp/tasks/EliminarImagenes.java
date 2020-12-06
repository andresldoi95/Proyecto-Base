package com.techtraining.cosechasapp.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.techtraining.cosechasapp.DBManager;
import com.techtraining.cosechasapp.GaleriaFilaActivity;
import com.techtraining.cosechasapp.R;
import com.techtraining.cosechasapp.db.AppDatabase;
import com.techtraining.cosechasapp.db.ImagenFila;

import java.util.List;

public class EliminarImagenes extends AsyncTask<Void, Void, Void> {
    private Context context;
    private AppDatabase appDatabase;
    private List<ImagenFila> imagenes;
    public EliminarImagenes(Context context, List<ImagenFila> imagenes) {
        this.context = context;
        this.imagenes = imagenes;
    }
    @Override
    protected Void doInBackground(Void... voids) {
        appDatabase = DBManager.getInstance(context);
        for (int i = 0; i < imagenes.size(); i++) {
            appDatabase.imagenFilaDao().delete(imagenes.get(i));
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        Toast.makeText(context, R.string.imagenes_eliminadas, Toast.LENGTH_SHORT).show();
        GaleriaFilaActivity activity = (GaleriaFilaActivity) context;
        activity.cargarFotos();
    }

}
