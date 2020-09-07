package com.techtraining.cosechasapp.tasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import com.techtraining.cosechasapp.DBManager;
import com.techtraining.cosechasapp.Helper;
import com.techtraining.cosechasapp.R;
import com.techtraining.cosechasapp.db.AppDatabase;
import com.techtraining.cosechasapp.db.Cosecha;
import com.techtraining.cosechasapp.db.CosechaDao;

public class GuardarCabeceraCosecha extends AsyncTask<Void, Void, Void> {
    private Context context;
    private Cosecha cosecha;
    public GuardarCabeceraCosecha(Context context, Cosecha cosecha) {
        this.context = context;
        this.cosecha = cosecha;
    }
    @Override
    protected Void doInBackground(Void... voids) {
        AppDatabase appDatabase = DBManager.getInstance(context);
        CosechaDao cosechaDao = appDatabase.cosechaDao();
        Cosecha cosechaExistente = cosechaDao.loadById(cosecha.id);
        if (cosechaExistente == null){
            cosechaDao.insertOne(cosecha);
            SharedPreferences.Editor editor = context.getSharedPreferences(Helper.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).edit();
            editor.putString(Helper.CURRENT_COSECHA_ID_NAME, cosecha.id);
            editor.commit();
        }
        else
            cosechaDao.update(cosecha);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        Toast.makeText(context, R.string.cosecha_almacenada, Toast.LENGTH_SHORT).show();
    }
}
