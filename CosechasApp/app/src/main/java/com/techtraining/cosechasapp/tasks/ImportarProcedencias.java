package com.techtraining.cosechasapp.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.techtraining.cosechasapp.DBManager;
import com.techtraining.cosechasapp.R;
import com.techtraining.cosechasapp.db.AppDatabase;
import com.techtraining.cosechasapp.db.Procedencia;
import com.techtraining.cosechasapp.db.ProcedenciaDao;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ImportarProcedencias extends AsyncTask<Void, Void, Void> {
    private JSONArray jsonArray;
    private Context context;
    private Toast toast;
    public ImportarProcedencias(Context context, JSONArray jsonArray) {
        this.jsonArray = jsonArray;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        toast = Toast.makeText(context, R.string.importando_procedencias, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        AppDatabase appDatabase = DBManager.getInstance(context);
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int id = jsonObject.getInt("id");
                ProcedenciaDao procedenciaDao = appDatabase.procedenciaDao();
                Procedencia procedencia = procedenciaDao.loadById(id);
                if (procedencia == null) {
                    procedencia = new Procedencia();
                    procedencia.id = id;
                    procedencia.descripcion = jsonObject.getString("descripcion");
                    procedencia.codigo = jsonObject.getString("codigo");
                    procedencia.email = jsonObject.getString("email");
                    procedencia.empresaId = jsonObject.getInt("empresa_id");
                    procedenciaDao.insertOne(procedencia);
                }
                else {
                    procedencia.descripcion = jsonObject.getString("descripcion");
                    procedencia.codigo = jsonObject.getString("codigo");
                    procedencia.email = jsonObject.getString("email");
                    procedencia.empresaId = jsonObject.getInt("empresa_id");
                    procedenciaDao.update(procedencia);
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
        toast.setText(R.string.procedencias_importadas);
        toast.show();
    }
}
