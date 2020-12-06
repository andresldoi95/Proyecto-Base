package com.techtraining.cosechasapp.tasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import com.techtraining.cosechasapp.DBManager;
import com.techtraining.cosechasapp.Helper;
import com.techtraining.cosechasapp.ItemSueltosActivity;
import com.techtraining.cosechasapp.R;
import com.techtraining.cosechasapp.TrozaActivity;
import com.techtraining.cosechasapp.db.AppDatabase;
import com.techtraining.cosechasapp.db.Camion;
import com.techtraining.cosechasapp.db.Cosecha;
import com.techtraining.cosechasapp.db.Espesor;
import com.techtraining.cosechasapp.db.FilaCosecha;
import com.techtraining.cosechasapp.db.FilaCosechaDao;
import com.techtraining.cosechasapp.db.FilaSuelto;
import com.techtraining.cosechasapp.db.FilaSueltoDao;
import com.techtraining.cosechasapp.db.FormatoEntrega;
import com.techtraining.cosechasapp.db.Largo;
import com.techtraining.cosechasapp.db.ParametroDao;
import com.techtraining.cosechasapp.db.Troza;
import com.techtraining.cosechasapp.db.TrozaDao;
import com.techtraining.cosechasapp.db.TrozaFotos;
import com.techtraining.cosechasapp.db.TrozaFotosDao;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GuardarTroza extends AsyncTask<Void, Void, Void> {
    private Context context;
    private AppDatabase appDatabase;
    private Troza troza;
    private ArrayList<TrozaFotos> currentFotos;
    public GuardarTroza(Context context, Troza troza, ArrayList<TrozaFotos> currentFotos) {
        this.context = context;
        this.troza = troza;
        this.currentFotos = currentFotos;
    }
    @Override
    protected Void doInBackground(Void... voids) {
        appDatabase = DBManager.getInstance(context);
        TrozaDao trozaDao = appDatabase.trozaDao();
        TrozaFotosDao trozaFotosDao = appDatabase.trozaFotosDao();
        String currentCosecha = context.getSharedPreferences(Helper.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).getString(Helper.CURRENT_COSECHA_ID_NAME, null);
        Troza trozaExistente = trozaDao.loadByCosecha(currentCosecha);
        if (trozaExistente != null) {
            troza.cosechaId = currentCosecha;
            troza.id = trozaExistente.id;
            trozaDao.update(troza);
        }
        else {
            troza.cosechaId = currentCosecha;
            troza.id = UUID.randomUUID().toString();
            trozaDao.insertOne(troza);
        }
        for(TrozaFotos trozaFoto:currentFotos){
            trozaFoto.id = UUID.randomUUID().toString();
            trozaFoto.trozaId = troza.id;
            trozaFotosDao.insertOne(trozaFoto);
        }
        return null;
    }


    @Override
    protected void onPostExecute(Void aVoid) {
        Toast.makeText(context, R.string.troza_guardada, Toast.LENGTH_SHORT).show();
        ((TrozaActivity) context).finish();
        new FinalizarDespacho(context).execute();
    }
}
