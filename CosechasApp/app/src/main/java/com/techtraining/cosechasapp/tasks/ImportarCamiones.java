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
import com.techtraining.cosechasapp.db.FilaCamion;
import com.techtraining.cosechasapp.db.FilaCamionDao;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ImportarCamiones extends AsyncTask<Void, Void, Void> {
    private JSONArray jsonArray;
    private Context context;
    private Toast toast;
    public ImportarCamiones(Context context, JSONArray jsonArray) {
        this.jsonArray = jsonArray;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        toast = Toast.makeText(context, R.string.importando_camiones, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        AppDatabase appDatabase = DBManager.getInstance(context);
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int id = jsonObject.getInt("id");
                CamionDao camionDao = appDatabase.camionDao();
                FilaCamionDao filaCamionDao = appDatabase.filaCamionDao();
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
                    camionDao.update(camion);
                }
                filaCamionDao.deleteByCamion(camion.id);
                JSONArray filas = jsonObject.getJSONArray("filas");
                for (int j = 0; j < filas.length(); j++) {
                    JSONObject fila = filas.getJSONObject(j);
                    FilaCamion filaCamion = new FilaCamion();
                    filaCamion.id = fila.getString("id");
                    filaCamion.camionId = camion.id;
                    filaCamion.columnas = fila.getInt("columnas");
                    filaCamion.filas = fila.getInt("filas");
                    filaCamionDao.insertOne(filaCamion);
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
        toast.setText(R.string.camiones_importados);
        toast.show();
        toast.setText(R.string.importacion_finalizada);
        toast.show();
    }
}
