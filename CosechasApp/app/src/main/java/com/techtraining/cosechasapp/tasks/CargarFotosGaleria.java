package com.techtraining.cosechasapp.tasks;

import android.content.Context;
import android.os.AsyncTask;
import com.techtraining.cosechasapp.DBManager;
import com.techtraining.cosechasapp.GaleriaFila;
import com.techtraining.cosechasapp.Helper;
import com.techtraining.cosechasapp.adapters.ImagenGaleriaAdapter;
import com.techtraining.cosechasapp.db.AppDatabase;
import com.techtraining.cosechasapp.db.ImagenFila;

import java.util.List;

public class CargarFotosGaleria extends AsyncTask<Void, Void, Void> {
    private Context context;
    private List<ImagenFila> imagenesFila;
    public CargarFotosGaleria(Context context) {
        this.context = context;
    }
    @Override
    protected Void doInBackground(Void... voids) {
        AppDatabase appDatabase = DBManager.getInstance(context);
        String filaId = context.getSharedPreferences(Helper.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).getString(Helper.CURRENT_FILA_NAME, null);
        if (filaId != null) {
            imagenesFila = appDatabase.imagenFilaDao().loadByFila(filaId);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        ((GaleriaFila) context).grvGaleria.setAdapter(new ImagenGaleriaAdapter(imagenesFila, context));
    }
}
