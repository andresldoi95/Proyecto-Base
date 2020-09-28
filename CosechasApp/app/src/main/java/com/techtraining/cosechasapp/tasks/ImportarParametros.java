package com.techtraining.cosechasapp.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.techtraining.cosechasapp.DBManager;
import com.techtraining.cosechasapp.R;
import com.techtraining.cosechasapp.db.AppDatabase;
import com.techtraining.cosechasapp.db.Parametro;
import com.techtraining.cosechasapp.db.ParametroDao;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ImportarParametros extends AsyncTask<Void, Void, Void> {
    private JSONArray jsonArray;
    private Context context;
    private Toast toast;
    public ImportarParametros(Context context, JSONArray jsonArray) {
        this.jsonArray = jsonArray;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        toast = Toast.makeText(context, R.string.importando_parametros, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        AppDatabase appDatabase = DBManager.getInstance(context);
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int id = jsonObject.getInt("id");
                ParametroDao parametroDao = appDatabase.parametroDao();
                Parametro parametro = parametroDao.loadById(id);
                if (parametro == null) {
                    parametro = new Parametro();
                    parametro.id = id;
                    parametro.factorHuecoBultos = jsonObject.getDouble("factor_hueco_bultos");
                    parametro.factorHuecoSueltos = jsonObject.getDouble("factor_hueco_sueltos");
                    parametro.empresaId = jsonObject.getInt("empresa_id");
                    parametroDao.insertOne(parametro);
                }
                else {
                    parametro.factorHuecoBultos = jsonObject.getDouble("factor_hueco_bultos");
                    parametro.factorHuecoSueltos = jsonObject.getDouble("factor_hueco_sueltos");
                    parametro.empresaId = jsonObject.getInt("empresa_id");
                    parametroDao.update(parametro);
                }
            }
            catch (JSONException ex) {
                Log.e(ImportarParametros.class.getName(), ex.getMessage());
            }
        }
        return null;
    }
    @Override
    protected void onPostExecute(Void aVoid) {
        toast.setText(R.string.parametros_importados);
        toast.show();
    }
}
