package com.techtraining.cosechasapp.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.techtraining.cosechasapp.DBManager;
import com.techtraining.cosechasapp.R;
import com.techtraining.cosechasapp.db.AppDatabase;
import com.techtraining.cosechasapp.db.Controlador;
import com.techtraining.cosechasapp.db.ControladorDao;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ImportarControladores extends AsyncTask<Void, Void, Void>  {
    private JSONArray jsonArray;
    private Context context;
    public ImportarControladores(Context context, JSONArray jsonArray) {
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
                ControladorDao controladorDao = appDatabase.controladorDao();
                Controlador controlador = controladorDao.loadById(id);
                if (controlador == null) {
                    controlador = new Controlador();
                    controlador.id = id;
                    controlador.nombre = jsonObject.getString("nombre");
                    controlador.empresaId = jsonObject.getInt("empresa_id");
                    controlador.identificacion = jsonObject.getString("identificacion");
                    controlador.estado = jsonObject.getString("estado");
                    controladorDao.insertOne(controlador);
                }
                else {
                    controlador.nombre = jsonObject.getString("nombre");
                    controlador.identificacion = jsonObject.getString("identificacion");
                    controlador.estado = jsonObject.getString("estado");
                    controlador.empresaId = jsonObject.getInt("empresa_id");
                    controladorDao.update(controlador);
                }
            }
            catch (JSONException ex) {
                Log.e(ImportarControladores.class.getName(), ex.getMessage());
            }
        }
        return null;
    }
    @Override
    protected void onPostExecute(Void aVoid) {
    }
}
