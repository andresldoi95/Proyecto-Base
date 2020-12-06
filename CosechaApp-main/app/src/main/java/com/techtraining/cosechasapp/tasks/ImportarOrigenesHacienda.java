package com.techtraining.cosechasapp.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.techtraining.cosechasapp.DBManager;
import com.techtraining.cosechasapp.R;
import com.techtraining.cosechasapp.db.AppDatabase;
import com.techtraining.cosechasapp.db.HaciendaMadera;
import com.techtraining.cosechasapp.db.HaciendaMaderaDao;
import com.techtraining.cosechasapp.db.OrigenHacienda;
import com.techtraining.cosechasapp.db.OrigenHaciendaDao;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ImportarOrigenesHacienda extends AsyncTask<Void, Void, Void> {
    private JSONArray jsonArray;
    private Context context;
    public ImportarOrigenesHacienda(Context context, JSONArray jsonArray) {
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
                OrigenHaciendaDao origenHaciendaDao = appDatabase.origenHaciendaDao();
                OrigenHacienda origenHacienda = origenHaciendaDao.loadById(id);
                if (origenHacienda == null) {
                    origenHacienda = new OrigenHacienda();
                    origenHacienda.id = id;
                    origenHacienda.descripcion = jsonObject.getString("descripcion");
                    origenHacienda.empresaId = jsonObject.getInt("empresa_id");
                    origenHacienda.estado = jsonObject.getString("estado");
                    origenHaciendaDao.insertOne(origenHacienda);
                }
                else {
                    origenHacienda.descripcion = jsonObject.getString("descripcion");
                    origenHacienda.empresaId = jsonObject.getInt("empresa_id");
                    origenHacienda.estado = jsonObject.getString("estado");
                    origenHaciendaDao.update(origenHacienda);
                }
                HaciendaMaderaDao haciendaMaderaDao = appDatabase.haciendaMaderaDao();
                haciendaMaderaDao.deleteByOrigenMadera(origenHacienda.id);
                JSONArray haciendasMadera = jsonObject.getJSONArray("haciendas");
                for (int j = 0; j < haciendasMadera.length(); j++) {
                    HaciendaMadera haciendaMadera = new HaciendaMadera();
                    JSONObject obj = haciendasMadera.getJSONObject(j);
                    haciendaMadera.haciendaId = obj.getInt("id");
                    haciendaMadera.origenMaderaId = origenHacienda.id;
                    haciendaMaderaDao.insertOne(haciendaMadera);
                }
            }
            catch (JSONException ex) {
                Log.e(ImportarOrigenesHacienda.class.getName(), ex.getMessage());
            }
        }
        return null;
    }
    @Override
    protected void onPostExecute(Void aVoid) {
    }
}
