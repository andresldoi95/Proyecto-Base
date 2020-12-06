package com.techtraining.cosechasapp.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.techtraining.cosechasapp.DBManager;
import com.techtraining.cosechasapp.R;
import com.techtraining.cosechasapp.db.AppDatabase;
import com.techtraining.cosechasapp.db.TipoMadera;
import com.techtraining.cosechasapp.db.TipoMaderaDao;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ImportarTiposMadera extends AsyncTask<Void, Void, Void> {
    private JSONArray jsonArray;
    private Context context;
    public ImportarTiposMadera(Context context, JSONArray jsonArray) {
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
                TipoMaderaDao tipoMaderaDao = appDatabase.tipoMaderaDao();
                TipoMadera tipoMadera = tipoMaderaDao.loadById(id);
                if (tipoMadera == null) {
                    tipoMadera = new TipoMadera();
                    tipoMadera.id = id;
                    tipoMadera.descripcion = jsonObject.getString("descripcion");
                    tipoMadera.valor = jsonObject.getDouble("valor");
                    tipoMadera.empresaId = jsonObject.getInt("empresa_id");
                    tipoMadera.estado = jsonObject.getString("estado");
                    tipoMaderaDao.insertOne(tipoMadera);
                }
                else {
                    tipoMadera.descripcion = jsonObject.getString("descripcion");
                    tipoMadera.valor = jsonObject.getDouble("valor");
                    tipoMadera.empresaId = jsonObject.getInt("empresa_id");
                    tipoMadera.estado = jsonObject.getString("estado");
                    tipoMaderaDao.update(tipoMadera);
                }
            }
            catch (JSONException ex) {
                Log.e(ImportarTiposMadera.class.getName(), ex.getMessage());
            }
        }
        return null;
    }
    @Override
    protected void onPostExecute(Void aVoid) {
    }
}
