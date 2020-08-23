package com.techtraining.cosechasapp.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.techtraining.cosechasapp.DBManager;
import com.techtraining.cosechasapp.R;
import com.techtraining.cosechasapp.db.AppDatabase;
import com.techtraining.cosechasapp.db.Espesor;
import com.techtraining.cosechasapp.db.EspesorDao;
import com.techtraining.cosechasapp.db.Largo;
import com.techtraining.cosechasapp.db.LargoDao;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ImportarLargos extends AsyncTask<Void, Void, Void> {
    private JSONArray jsonArray;
    private Context context;
    private Toast toast;
    public ImportarLargos(Context context, JSONArray jsonArray) {
        this.jsonArray = jsonArray;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        toast = Toast.makeText(context, R.string.importando_largos, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        AppDatabase appDatabase = DBManager.getInstance(context);
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int id = jsonObject.getInt("id");
                LargoDao largoDao = appDatabase.largoDao();
                Largo largo = largoDao.loadById(id);
                if (largo == null) {
                    largo = new Largo();
                    largo.id = id;
                    largo.descripcion = jsonObject.getString("descripcion");
                    largo.valor = jsonObject.getDouble("valor");
                    largo.empresaId = jsonObject.getInt("empresa_id");
                    largoDao.insertOne(largo);
                }
                else {
                    largo.descripcion = jsonObject.getString("descripcion");
                    largo.valor = jsonObject.getDouble("valor");
                    largo.empresaId = jsonObject.getInt("empresa_id");
                    largoDao.update(largo);
                }
            }
            catch (JSONException ex) {
                Log.e(ImportarLargos.class.getName(), ex.getMessage());
            }
        }
        return null;
    }
    @Override
    protected void onPostExecute(Void aVoid) {
        toast.setText(R.string.largos_importados);
        toast.show();
    }
}
