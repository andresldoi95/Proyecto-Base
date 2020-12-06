package com.techtraining.cosechasapp.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.techtraining.cosechasapp.DBManager;
import com.techtraining.cosechasapp.R;
import com.techtraining.cosechasapp.db.AppDatabase;
import com.techtraining.cosechasapp.db.FormatoEntrega;
import com.techtraining.cosechasapp.db.FormatoEntregaDao;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ImportarFormatosEntrega extends AsyncTask<Void, Void, Void> {
    private JSONArray jsonArray;
    private Context context;
    public ImportarFormatosEntrega(Context context, JSONArray jsonArray) {
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
                FormatoEntregaDao formatoEntregaDao = appDatabase.formatoEntregaDao();
                FormatoEntrega formatoEntrega = formatoEntregaDao.loadById(id);
                if (formatoEntrega == null) {
                    formatoEntrega = new FormatoEntrega();
                    formatoEntrega.id = id;
                    formatoEntrega.descripcion = jsonObject.getString("descripcion");
                    formatoEntrega.empresaId = jsonObject.getInt("empresa_id");
                    formatoEntrega.estado = jsonObject.getString("estado");
                    formatoEntrega.factorHueco = jsonObject.getDouble("factor_hueco");
                    formatoEntregaDao.insertOne(formatoEntrega);
                }
                else {
                    formatoEntrega.descripcion = jsonObject.getString("descripcion");
                    formatoEntrega.empresaId = jsonObject.getInt("empresa_id");
                    formatoEntrega.estado = jsonObject.getString("estado");
                    formatoEntrega.factorHueco = jsonObject.getDouble("factor_hueco");
                    formatoEntregaDao.update(formatoEntrega);
                }
            }
            catch (JSONException ex) {
                Log.e(ImportarFormatosEntrega.class.getName(), ex.getMessage());
            }
        }
        return null;
    }
    @Override
    protected void onPostExecute(Void aVoid) {
    }
}
