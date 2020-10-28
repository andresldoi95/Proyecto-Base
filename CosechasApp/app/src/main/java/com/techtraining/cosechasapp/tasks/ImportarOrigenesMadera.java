package com.techtraining.cosechasapp.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.techtraining.cosechasapp.DBManager;
import com.techtraining.cosechasapp.R;
import com.techtraining.cosechasapp.db.AppDatabase;
import com.techtraining.cosechasapp.db.OrigenMadera;
import com.techtraining.cosechasapp.db.OrigenMaderaDao;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ImportarOrigenesMadera extends AsyncTask<Void, Void, Void> {
    private JSONArray jsonArray;
    private Context context;
    private Toast toast;
    public ImportarOrigenesMadera(Context context, JSONArray jsonArray) {
        this.jsonArray = jsonArray;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        toast = Toast.makeText(context, R.string.importando_origenes_madera, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        AppDatabase appDatabase = DBManager.getInstance(context);
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int id = jsonObject.getInt("id");
                OrigenMaderaDao origenMaderaDao = appDatabase.origenMaderaDao();
                OrigenMadera origenMadera = origenMaderaDao.loadById(id);
                if (origenMadera == null) {
                    origenMadera = new OrigenMadera();
                    origenMadera.id = id;
                    origenMadera.empresaId = jsonObject.getInt("empresa_id");
                    origenMadera.descripcion = jsonObject.getString("descripcion");
                    origenMadera.anioCultivo = jsonObject.getInt("anio_cultivo");
                    origenMadera.hectareas = jsonObject.getDouble("hectareas");
                    origenMadera.volumenInventario = jsonObject.getInt("volumen_inventario");
                    origenMadera.estado = jsonObject.getString("estado");
                    origenMaderaDao.insertOne(origenMadera);
                }
                else {
                    origenMadera.empresaId = jsonObject.getInt("empresa_id");
                    origenMadera.descripcion = jsonObject.getString("descripcion");
                    origenMadera.anioCultivo = jsonObject.getInt("anio_cultivo");
                    origenMadera.hectareas = jsonObject.getDouble("hectareas");
                    origenMadera.volumenInventario = jsonObject.getInt("volumen_inventario");
                    origenMadera.estado = jsonObject.getString("estado");
                    origenMaderaDao.update(origenMadera);
                }
            }
            catch (JSONException ex) {
                Log.e(ImportarOrigenesMadera.class.getName(), ex.getMessage());
            }
        }
        return null;
    }
    @Override
    protected void onPostExecute(Void aVoid) {
        toast.setText(R.string.origenes_madera_importados);
        toast.show();
    }
}
