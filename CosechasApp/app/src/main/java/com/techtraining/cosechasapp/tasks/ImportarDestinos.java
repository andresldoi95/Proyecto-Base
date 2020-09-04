package com.techtraining.cosechasapp.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.techtraining.cosechasapp.DBManager;
import com.techtraining.cosechasapp.R;
import com.techtraining.cosechasapp.db.AppDatabase;
import com.techtraining.cosechasapp.db.Destino;
import com.techtraining.cosechasapp.db.DestinoDao;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ImportarDestinos extends AsyncTask<Void, Void, Void> {
    private JSONArray jsonArray;
    private Context context;
    private Toast toast;
    public ImportarDestinos(Context context, JSONArray jsonArray) {
        this.jsonArray = jsonArray;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        toast = Toast.makeText(context, R.string.importando_destinos, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        AppDatabase appDatabase = DBManager.getInstance(context);
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int id = jsonObject.getInt("id");
                DestinoDao destinoDao = appDatabase.destinoDao();
                Destino destino = destinoDao.loadById(id);
                if (destino == null) {
                    destino = new Destino();
                    destino.id = id;
                    destino.descripcion = jsonObject.getString("descripcion");
                    destino.codigo = jsonObject.getString("codigo");
                    destino.email = jsonObject.getString("email");
                    destino.empresaId = jsonObject.getInt("empresa_id");
                    destinoDao.insertOne(destino);
                }
                else {
                    destino.descripcion = jsonObject.getString("descripcion");
                    destino.codigo = jsonObject.getString("codigo");
                    destino.email = jsonObject.getString("email");
                    destino.empresaId = jsonObject.getInt("empresa_id");
                    destinoDao.update(destino);
                }
            }
            catch (JSONException ex) {
                Log.e(ImportarDestinos.class.getName(), ex.getMessage());
            }
        }
        return null;
    }
    @Override
    protected void onPostExecute(Void aVoid) {
        toast.setText(R.string.destinos_importados);
        toast.show();
    }
}
