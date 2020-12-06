package com.techtraining.cosechasapp.tasks;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ListView;
import android.widget.Toast;

import com.techtraining.cosechasapp.DBManager;
import com.techtraining.cosechasapp.Helper;
import com.techtraining.cosechasapp.R;
import com.techtraining.cosechasapp.TrozaActivity;
import com.techtraining.cosechasapp.adapters.TrozaFotosAdapter;
import com.techtraining.cosechasapp.db.AppDatabase;
import com.techtraining.cosechasapp.db.Troza;
import com.techtraining.cosechasapp.db.TrozaDao;
import com.techtraining.cosechasapp.db.TrozaFotos;
import com.techtraining.cosechasapp.db.TrozaFotosDao;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CargarDatosTroza extends AsyncTask<Void, Void, Void> {
    private Context context;
    private AppDatabase appDatabase;
    private Troza troza;
    private TrozaFotosAdapter adapter;
    private List<TrozaFotos> trozaFotos;
    private ListView listTrozaFotos;
    private boolean isHistorial;
    public CargarDatosTroza(Context context, TrozaFotosAdapter adapter, ListView listTrozaFotos) {
        this.context = context;
        this.adapter = adapter;
        this.listTrozaFotos = listTrozaFotos;
    }
    @Override
    protected Void doInBackground(Void... voids) {
        appDatabase = DBManager.getInstance(context);
        TrozaDao trozaDao = appDatabase.trozaDao();
        TrozaFotosDao trozaFotosDao = appDatabase.trozaFotosDao();
        String currentCosechaId = context.getSharedPreferences(Helper.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).getString(Helper.CURRENT_COSECHA_ID_NAME, null);
        isHistorial = context.getSharedPreferences(Helper.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).getBoolean(Helper.IS_HISTORIAL, false);
        if(isHistorial){
            currentCosechaId = context.getSharedPreferences(Helper.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).getString(Helper.CURRENT_COSECHA_ID_HISTORIAL, null);
        }
        troza = trozaDao.loadByCosecha(currentCosechaId);
        if(troza != null){
            trozaFotos = trozaFotosDao.loadAllByTrozaId(troza.id);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (troza != null) {
            TrozaActivity trozaActivity = ((TrozaActivity) context);
            trozaActivity.etObservaciones.setText(troza.observaciones);
            trozaActivity.etVolumenEstimado.setText(String.valueOf(troza.volumenEstimado));
            trozaActivity.etNumeroTrozas.setText(String.valueOf(troza.numeroTrozas));
            // if (trozaActivity.isReadStoragePermissionGranted() && troza.foto != null) {
                // trozaActivity.currentFoto = troza.foto;
                // trozaActivity.ivFoto.setImageBitmap(BitmapFactory.decodeFile(troza.foto));
            // }
        }
        if(trozaFotos != null && isHistorial){
            ((TrozaActivity)context).currentFotos = new ArrayList<>(trozaFotos);
            adapter = new TrozaFotosAdapter(((TrozaActivity)context).currentFotos, context);
            listTrozaFotos.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

}
