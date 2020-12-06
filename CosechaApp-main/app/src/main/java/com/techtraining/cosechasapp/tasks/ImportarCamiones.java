package com.techtraining.cosechasapp.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.techtraining.cosechasapp.DBManager;
import com.techtraining.cosechasapp.R;
import com.techtraining.cosechasapp.db.AppDatabase;
import com.techtraining.cosechasapp.db.Camion;
import com.techtraining.cosechasapp.db.CamionDao;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ImportarCamiones extends AsyncTask<Void, Void, Void> {
    private JSONArray jsonArray;
    private Context context;
    public ImportarCamiones(Context context, JSONArray jsonArray) {
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
                CamionDao camionDao = appDatabase.camionDao();
                Camion camion = camionDao.loadById(id);
                if (camion == null) {
                    camion = new Camion();
                    camion.id = id;
                    camion.tipoCamion = jsonObject.getString("tipo_camion");
                    camion.placa = jsonObject.getString("placa");
                    camion.ancho = jsonObject.getDouble("ancho");
                    camion.alto = jsonObject.getDouble("alto");
                    camion.empresaId  = jsonObject.getInt("empresa_id");
                    camion.camionero = jsonObject.getString("camionero");
                    camion.identificacionCamionero = jsonObject.getString("identificacion_camionero");
                    camion.estado = jsonObject.getString("estado");
                    camion.filas = jsonObject.getInt("filas");
                    camion.codigoVendor = jsonObject.getString("codigo_vendor");
                    camionDao.insertOne(camion);
                }
                else {
                    camion.tipoCamion = jsonObject.getString("tipo_camion");
                    camion.placa = jsonObject.getString("placa");
                    camion.ancho = jsonObject.getDouble("ancho");
                    camion.alto = jsonObject.getDouble("alto");
                    camion.empresaId  = jsonObject.getInt("empresa_id");
                    camion.camionero = jsonObject.getString("camionero");
                    camion.identificacionCamionero = jsonObject.getString("identificacion_camionero");
                    camion.estado = jsonObject.getString("estado");
                    camion.filas = jsonObject.getInt("filas");
                    camion.codigoVendor = jsonObject.getString("codigo_vendor");
                    camionDao.update(camion);
                }
            }
            catch (JSONException ex) {
                Log.e(ImportarCamiones.class.getName(), ex.getMessage());
            }
        }
        return null;
    }
    @Override
    protected void onPostExecute(Void aVoid) {
    }
}
