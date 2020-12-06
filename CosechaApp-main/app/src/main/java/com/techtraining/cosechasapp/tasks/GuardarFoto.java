package com.techtraining.cosechasapp.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.techtraining.cosechasapp.DBManager;
import com.techtraining.cosechasapp.GaleriaFilaActivity;
import com.techtraining.cosechasapp.Helper;
import com.techtraining.cosechasapp.R;
import com.techtraining.cosechasapp.db.AppDatabase;
import com.techtraining.cosechasapp.db.ImagenFila;

import java.util.UUID;

public class GuardarFoto extends AsyncTask<Void, Void, Void> {
    private Context context;
    private String path;
    public GuardarFoto(Context context, String path) {
        this.context = context;
        this.path = path;
    }
    @Override
    protected Void doInBackground(Void... voids) {
        AppDatabase appDatabase = DBManager.getInstance(context);
        String filaId = context.getSharedPreferences(Helper.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).getString(Helper.CURRENT_FILA_NAME, null);
        if (filaId != null) {
            ImagenFila imagenFila = new ImagenFila();
            imagenFila.filaId = filaId;
            imagenFila.id = UUID.randomUUID().toString();
            imagenFila.path = path;
            appDatabase.imagenFilaDao().insertOne(imagenFila);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        ((GaleriaFilaActivity) context).cargarFotos();
        Toast.makeText(context, R.string.foto_almacenada, Toast.LENGTH_SHORT).show();
    }
}
