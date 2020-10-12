package com.techtraining.cosechasapp.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.techtraining.cosechasapp.DBManager;
import com.techtraining.cosechasapp.R;
import com.techtraining.cosechasapp.db.AppDatabase;
import com.techtraining.cosechasapp.db.Aserrador;
import com.techtraining.cosechasapp.db.AserradorDao;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ImportarAserradores extends AsyncTask<Void, Void, Void> {
    private JSONArray jsonArray;
    private Context context;
    private Toast toast;
    public ImportarAserradores(Context context, JSONArray jsonArray) {
        this.jsonArray = jsonArray;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        toast = Toast.makeText(context, R.string.importando_aserradores, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        AppDatabase appDatabase = DBManager.getInstance(context);
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int id = jsonObject.getInt("id");
                AserradorDao aserradorDao = appDatabase.aserradorDao();
                Aserrador aserrador = aserradorDao.loadById(id);
                if (aserrador == null) {
                    aserrador = new Aserrador();
                    aserrador.id = id;
                    aserrador.nombre = jsonObject.getString("nombre");
                    aserrador.empresaId = jsonObject.getInt("empresa_id");
                    aserrador.identificacion = jsonObject.getString("identificacion");
                    aserrador.estado = jsonObject.getString("estado");
                    aserradorDao.insertOne(aserrador);
                }
                else {
                    aserrador.nombre = jsonObject.getString("nombre");
                    aserrador.identificacion = jsonObject.getString("identificacion");
                    aserrador.estado = jsonObject.getString("estado");
                    aserrador.empresaId = jsonObject.getInt("empresa_id");
                    aserradorDao.update(aserrador);
                }
            }
            catch (JSONException ex) {
                Log.e(ImportarAserradores.class.getName(), ex.getMessage());
            }
        }
        return null;
    }
    @Override
    protected void onPostExecute(Void aVoid) {
        toast.setText(R.string.aserradores_importados);
        toast.show();
    }
}
