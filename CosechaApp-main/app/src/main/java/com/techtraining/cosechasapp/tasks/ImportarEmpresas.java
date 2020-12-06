package com.techtraining.cosechasapp.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.techtraining.cosechasapp.DBManager;
import com.techtraining.cosechasapp.R;
import com.techtraining.cosechasapp.db.AppDatabase;
import com.techtraining.cosechasapp.db.Empresa;
import com.techtraining.cosechasapp.db.EmpresaDao;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ImportarEmpresas extends AsyncTask<Void, Void, Void> {
    private JSONArray jsonArray;
    private Context context;
    public ImportarEmpresas(Context context, JSONArray jsonArray) {
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
                EmpresaDao empresaDao = appDatabase.empresaDao();
                Empresa empresa = empresaDao.loadById(id);
                if (empresa == null) {
                    empresa = new Empresa();
                    empresa.id = id;
                    empresa.nombre = jsonObject.getString("nombre");
                    empresa.descripcion = jsonObject.getString("descripcion");
                    empresa.estado = jsonObject.getString("estado");
                    empresaDao.insertOne(empresa);
                }
                else {
                    empresa.nombre = jsonObject.getString("nombre");
                    empresa.descripcion = jsonObject.getString("descripcion");
                    empresa.estado = jsonObject.getString("estado");
                    empresaDao.update(empresa);
                }
            }
            catch (JSONException ex) {
                Log.e(ImportarEmpresas.class.getName(), ex.getMessage());
            }
        }
        return null;
    }
    @Override
    protected void onPostExecute(Void aVoid) {
    }
}
