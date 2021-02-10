package com.techtraining.cosechasapp.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.techtraining.cosechasapp.DBManager;
import com.techtraining.cosechasapp.R;
import com.techtraining.cosechasapp.db.AppDatabase;
import com.techtraining.cosechasapp.db.Tarifa;
import com.techtraining.cosechasapp.db.TarifaDao;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ImportarTarifas extends AsyncTask<Void, Void, Void> {
    private JSONArray jsonArray;
    private Context context;
    public ImportarTarifas(Context context, JSONArray jsonArray) {
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
                TarifaDao tarifaDao = appDatabase.tarifaDao();
                Tarifa tarifa = tarifaDao.loadById(id);
                if (tarifa == null) {
                    tarifa = new Tarifa();
                    tarifa.id = id;
                    tarifa.valor = jsonObject.getDouble("valor");
                    tarifa.tipoCamion = jsonObject.getString("tipo_camion");
                    tarifa.destinoId = jsonObject.getInt("destino_id");
                    tarifa.origenMaderaId = jsonObject.getInt("origen_madera_id");
                    tarifa.empresaId = jsonObject.getInt("empresa_id");
                    tarifa.estado = jsonObject.getString("estado");
                    tarifaDao.insertOne(tarifa);
                }
                else {
                    tarifa.valor = jsonObject.getDouble("valor");
                    tarifa.tipoCamion = jsonObject.getString("tipo_camion");
                    tarifa.destinoId = jsonObject.getInt("destino_id");
                    tarifa.origenMaderaId = jsonObject.getInt("origen_madera_id");
                    tarifa.empresaId = jsonObject.getInt("empresa_id");
                    tarifa.estado = jsonObject.getString("estado");
                    tarifaDao.update(tarifa);
                }
            }
            catch (JSONException ex) {
                Log.e(ImportarProcedencias.class.getName(), ex.getMessage());
            }
        }
        return null;
    }
    @Override
    protected void onPostExecute(Void aVoid) {
    }
}
