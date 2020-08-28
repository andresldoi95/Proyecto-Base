package com.techtraining.cosechasapp.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.techtraining.cosechasapp.DBManager;
import com.techtraining.cosechasapp.R;
import com.techtraining.cosechasapp.db.AppDatabase;
import com.techtraining.cosechasapp.db.Espesor;
import com.techtraining.cosechasapp.db.EspesorDao;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ImportarEspesores extends AsyncTask<Void, Void, Void> {
    private JSONArray jsonArray;
    private Context context;
    private Toast toast;
    public ImportarEspesores(Context context, JSONArray jsonArray) {
        this.jsonArray = jsonArray;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        toast = Toast.makeText(context, R.string.importando_espesores, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        AppDatabase appDatabase = DBManager.getInstance(context);
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int id = jsonObject.getInt("id");
                EspesorDao espesorDao = appDatabase.espesorDao();
                Espesor espesor = espesorDao.loadById(id);
                if (espesor == null) {
                    espesor = new Espesor();
                    espesor.id = id;
                    espesor.descripcion = jsonObject.getString("descripcion");
                    espesor.valor = jsonObject.getDouble("valor");
                    espesor.color = jsonObject.getString("color");
                    espesor.empresaId = jsonObject.getInt("empresa_id");
                    espesor.estado = jsonObject.getString("estado");
                    espesorDao.insertOne(espesor);
                }
                else {
                    espesor.descripcion = jsonObject.getString("descripcion");
                    espesor.valor = jsonObject.getDouble("valor");
                    espesor.color = jsonObject.getString("color");
                    espesor.empresaId = jsonObject.getInt("empresa_id");
                    espesor.estado = jsonObject.getString("estado");
                    espesorDao.update(espesor);
                }
            }
            catch (JSONException ex) {
                Log.e(ImportarEspesores.class.getName(), ex.getMessage());
            }
        }
        return null;
    }
    @Override
    protected void onPostExecute(Void aVoid) {
        toast.setText(R.string.espesores_importados);
        toast.show();
    }
}
