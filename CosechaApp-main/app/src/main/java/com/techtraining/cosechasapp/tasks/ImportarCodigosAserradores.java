package com.techtraining.cosechasapp.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.techtraining.cosechasapp.DBManager;
import com.techtraining.cosechasapp.R;
import com.techtraining.cosechasapp.db.AppDatabase;
import com.techtraining.cosechasapp.db.CodigoAserrador;
import com.techtraining.cosechasapp.db.CodigoAserradorDao;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ImportarCodigosAserradores extends AsyncTask<Void, Void, Void> {
    private JSONArray jsonArray;
    private Context context;
    public ImportarCodigosAserradores(Context context, JSONArray jsonArray) {
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
                CodigoAserradorDao codigoAserradorDao = appDatabase.codigoAserradorDao();
                CodigoAserrador codigoAserrador = codigoAserradorDao.loadById(id);
                if (codigoAserrador == null) {
                    codigoAserrador = new CodigoAserrador();
                    codigoAserrador.id = id;
                    codigoAserrador.descripcion = jsonObject.getString("descripcion");
                    codigoAserrador.codigo = jsonObject.getString("codigo");
                    codigoAserrador.aserradorId = jsonObject.getInt("aserrador_id");
                    codigoAserrador.empresaId = jsonObject.getInt("empresa_id");
                    codigoAserradorDao.insertOne(codigoAserrador);
                }
                else {
                    codigoAserrador.descripcion = jsonObject.getString("descripcion");
                    codigoAserrador.codigo = jsonObject.getString("codigo");
                    codigoAserrador.aserradorId = jsonObject.getInt("aserrador_id");
                    codigoAserrador.empresaId = jsonObject.getInt("empresa_id");
                    codigoAserradorDao.update(codigoAserrador);
                }
            }
            catch (JSONException ex) {
                Log.e(ImportarCodigosAserradores.class.getName(), ex.getMessage());
            }
        }
        return null;
    }
    @Override
    protected void onPostExecute(Void aVoid) {
    }
}
