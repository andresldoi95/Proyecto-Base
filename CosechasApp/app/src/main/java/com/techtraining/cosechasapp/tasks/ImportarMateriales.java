package com.techtraining.cosechasapp.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.techtraining.cosechasapp.DBManager;
import com.techtraining.cosechasapp.R;
import com.techtraining.cosechasapp.db.AppDatabase;
import com.techtraining.cosechasapp.db.Material;
import com.techtraining.cosechasapp.db.MaterialDao;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ImportarMateriales extends AsyncTask<Void, Void, Void> {
    private JSONArray jsonArray;
    private Context context;
    private Toast toast;
    public ImportarMateriales(Context context, JSONArray jsonArray) {
        this.jsonArray = jsonArray;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        toast = Toast.makeText(context, R.string.importando_materiales, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        AppDatabase appDatabase = DBManager.getInstance(context);
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int id = jsonObject.getInt("id");
                MaterialDao materialDao = appDatabase.materialDao();
                Material material = materialDao.loadById(id);
                if (material == null) {
                    material = new Material();
                    material.id = id;
                    material.descripcion = jsonObject.getString("descripcion");
                    material.codigo = jsonObject.getString("codigo");
                    material.empresaId = jsonObject.getInt("empresa_id");
                    material.estado = jsonObject.getString("estado");
                    materialDao.insertOne(material);
                }
                else {
                    material.descripcion = jsonObject.getString("descripcion");
                    material.codigo = jsonObject.getString("codigo");
                    material.empresaId = jsonObject.getInt("empresa_id");
                    material.estado = jsonObject.getString("estado");
                    materialDao.update(material);
                }
            }
            catch (JSONException ex) {
                Log.e(ImportarMateriales.class.getName(), ex.getMessage());
            }
        }
        return null;
    }
    @Override
    protected void onPostExecute(Void aVoid) {
        toast.setText(R.string.materiales_importados);
        toast.show();
    }
}
