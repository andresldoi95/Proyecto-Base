package com.techtraining.cosechasapp.tasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import com.techtraining.cosechasapp.DBManager;
import com.techtraining.cosechasapp.Helper;
import com.techtraining.cosechasapp.R;
import com.techtraining.cosechasapp.db.AppDatabase;
import com.techtraining.cosechasapp.db.Cosecha;
import com.techtraining.cosechasapp.db.CosechaDao;
import com.techtraining.cosechasapp.db.FilaCamion;
import com.techtraining.cosechasapp.db.FilaCamionDao;
import com.techtraining.cosechasapp.db.FilaCosecha;
import com.techtraining.cosechasapp.db.FilaCosechaDao;
import com.techtraining.cosechasapp.db.ItemFilaCosecha;
import com.techtraining.cosechasapp.db.ItemFilaCosechaDao;

import java.util.List;
import java.util.UUID;

public class GuardarCabeceraCosecha extends AsyncTask<Void, Void, Void> {
    private Context context;
    private Cosecha cosecha;
    public GuardarCabeceraCosecha(Context context, Cosecha cosecha) {
        this.context = context;
        this.cosecha = cosecha;
    }
    @Override
    protected Void doInBackground(Void... voids) {
        AppDatabase appDatabase = DBManager.getInstance(context);
        CosechaDao cosechaDao = appDatabase.cosechaDao();
        Cosecha cosechaExistente = cosechaDao.loadById(cosecha.id);
        if (cosechaExistente == null){
            cosechaDao.insertOne(cosecha);
            SharedPreferences.Editor editor = context.getSharedPreferences(Helper.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).edit();
            editor.putString(Helper.CURRENT_COSECHA_ID_NAME, cosecha.id);
            editor.commit();
        }
        else
            cosechaDao.update(cosecha);
        FilaCosechaDao filaCosechaDao = appDatabase.filaCosechaDao();
        ItemFilaCosechaDao itemFilaCosechaDao = appDatabase.itemFilaCosechaDao();
        List<FilaCosecha> filasCosecha = filaCosechaDao.loadByCosecha(cosecha.id);
        if (filasCosecha.size() == 0) {
            FilaCamionDao filaCamionDao = appDatabase.filaCamionDao();
            List<FilaCamion> filasCamion = filaCamionDao.loadByCamion(cosecha.camionId);
            for (int i = 0; i < filasCamion.size(); i++) {
                FilaCamion filaCamion = filasCamion.get(i);
                FilaCosecha filaCosecha = new FilaCosecha();
                filaCosecha.id = UUID.randomUUID().toString();
                filaCosecha.indice = i;
                filaCosecha.cosechaId = cosecha.id;
                filaCosecha.bultos = 0;
                filaCosecha.bft = 0;
                filaCosecha.columnas = filaCamion.columnas;
                filaCosecha.filas = filaCamion.filas;
                filaCosechaDao.insertOne(filaCosecha);
                for (int j = 0; j < filaCosecha.filas; j++) {
                    for (int k = 0; k < filaCosecha.columnas; k++) {
                        ItemFilaCosecha item = new ItemFilaCosecha();
                        item.id = UUID.randomUUID().toString();
                        item.plantilla = 0;
                        item.filaId = filaCosecha.id;
                        item.bft = 0;
                        item.bultos = 0;
                        item.columna = k;
                        item.fila = j;
                        itemFilaCosechaDao.insertOne(item);
                    }
                }
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        Toast.makeText(context, R.string.cosecha_almacenada, Toast.LENGTH_SHORT).show();
    }
}
