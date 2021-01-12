package com.techtraining.cosechasapp.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.techtraining.cosechasapp.DBManager;
import com.techtraining.cosechasapp.db.AppDatabase;
import com.techtraining.cosechasapp.db.OrigenMadera;
import com.techtraining.cosechasapp.db.OrigenMaderaAnio;
import com.techtraining.cosechasapp.db.OrigenMaderaAnioDao;
import com.techtraining.cosechasapp.db.OrigenMaderaDao;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ImportarOrigenesMaderaAnios extends AsyncTask<Void, Void, Void> {
    private JSONArray jsonArray;
    private Context context;
    public ImportarOrigenesMaderaAnios(Context context, JSONArray jsonArray) {
        this.jsonArray = jsonArray;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected Void doInBackground(Void... voids) {
        AppDatabase appDatabase = DBManager.getInstance(context);
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int id = jsonObject.getInt("id");
                OrigenMaderaAnioDao origenMaderaAnioDao = appDatabase.origenMaderaAnioDao();
                OrigenMaderaAnio origenMaderaAnio = origenMaderaAnioDao.loadById(id);
                if (origenMaderaAnio == null) {
                    origenMaderaAnio = new OrigenMaderaAnio();
                    origenMaderaAnio.id = id;
                    origenMaderaAnio.origen_madera_id = jsonObject.getInt("origen_madera_id");
                    origenMaderaAnio.anio_cultivo = jsonObject.getString("anio_cultivo");
                    origenMaderaAnio.estado = jsonObject.getString("estado");
                    origenMaderaAnioDao.insertOne(origenMaderaAnio);
                }
                else {
                    origenMaderaAnio.origen_madera_id = jsonObject.getInt("origen_madera_id");
                    origenMaderaAnio.anio_cultivo = jsonObject.getString("anio_cultivo");
                    origenMaderaAnio.estado = jsonObject.getString("estado");
                    origenMaderaAnioDao.update(origenMaderaAnio);
                }
            }
            catch (JSONException ex) {
                Log.e(ImportarOrigenesMaderaAnios.class.getName(), ex.getMessage());
            }
        }
        return null;
    }
    @Override
    protected void onPostExecute(Void aVoid) {
    }
}
