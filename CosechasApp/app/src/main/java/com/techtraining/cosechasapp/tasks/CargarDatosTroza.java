package com.techtraining.cosechasapp.tasks;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.Toast;

import com.techtraining.cosechasapp.DBManager;
import com.techtraining.cosechasapp.Helper;
import com.techtraining.cosechasapp.R;
import com.techtraining.cosechasapp.TrozaActivity;
import com.techtraining.cosechasapp.db.AppDatabase;
import com.techtraining.cosechasapp.db.Troza;
import com.techtraining.cosechasapp.db.TrozaDao;

import java.util.UUID;

public class CargarDatosTroza extends AsyncTask<Void, Void, Void> {
    private Context context;
    private AppDatabase appDatabase;
    private Troza troza;
    public CargarDatosTroza(Context context) {
        this.context = context;
    }
    @Override
    protected Void doInBackground(Void... voids) {
        appDatabase = DBManager.getInstance(context);
        TrozaDao trozaDao = appDatabase.trozaDao();
        troza = trozaDao.loadByCosecha(context.getSharedPreferences(Helper.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).getString(Helper.CURRENT_COSECHA_ID_NAME, null));
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (troza != null) {
            TrozaActivity trozaActivity = ((TrozaActivity) context);
            trozaActivity.etObservaciones.setText(troza.observaciones);
            trozaActivity.etVolumenEstimado.setText(String.valueOf(troza.volumenEstimado));
            trozaActivity.etNumeroTrozas.setText(String.valueOf(troza.numeroTrozas));
            if (trozaActivity.isReadStoragePermissionGranted() && troza.foto != null) {
                trozaActivity.currentFoto = troza.foto;
                trozaActivity.ivFoto.setImageBitmap(BitmapFactory.decodeFile(troza.foto));
            }
        }

    }

}
