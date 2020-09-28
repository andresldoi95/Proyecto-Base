package com.techtraining.cosechasapp.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.techtraining.cosechasapp.DBManager;
import com.techtraining.cosechasapp.R;
import com.techtraining.cosechasapp.db.AppDatabase;
import com.techtraining.cosechasapp.db.TipoBulto;
import com.techtraining.cosechasapp.db.TipoBultoDao;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ImportarTiposBulto extends AsyncTask<Void, Void, Void> {
    private JSONArray jsonArray;
    private Context context;
    private Toast toast;
    public ImportarTiposBulto(Context context, JSONArray jsonArray) {
        this.jsonArray = jsonArray;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        toast = Toast.makeText(context, R.string.importando_tipos_bulto, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        AppDatabase appDatabase = DBManager.getInstance(context);
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int id = jsonObject.getInt("id");
                TipoBultoDao tipoBultoDao = appDatabase.tipoBultoDao();
                TipoBulto tipoBulto = tipoBultoDao.loadById(id);
                if (tipoBulto == null) {
                    tipoBulto = new TipoBulto();
                    tipoBulto.id = id;
                    tipoBulto.espesorId = jsonObject.getInt("espesor_id");
                    tipoBulto.codigo = jsonObject.getString("codigo");
                    tipoBulto.largoId = jsonObject.getInt("largo_id");
                    tipoBulto.ancho = jsonObject.getDouble("ancho");
                    tipoBulto.empresaId = jsonObject.getInt("empresa_id");
                    tipoBulto.estado = jsonObject.getString("estado");
                    tipoBultoDao.insertOne(tipoBulto);
                }
                else {
                    tipoBulto.espesorId = jsonObject.getInt("espesor_id");
                    tipoBulto.codigo = jsonObject.getString("codigo");
                    tipoBulto.largoId = jsonObject.getInt("largo_id");
                    tipoBulto.ancho = jsonObject.getDouble("ancho");
                    tipoBulto.empresaId = jsonObject.getInt("empresa_id");
                    tipoBulto.estado = jsonObject.getString("estado");
                    tipoBultoDao.update(tipoBulto);
                }
            }
            catch (JSONException ex) {
                Log.e(ImportarTiposBulto.class.getName(), ex.getMessage());
            }
        }
        return null;
    }
    @Override
    protected void onPostExecute(Void aVoid) {
        toast.setText(R.string.tipos_bulto_importados);
        toast.show();
    }
}
