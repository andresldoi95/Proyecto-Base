package com.techtraining.cosechasapp.tasks;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;


import com.techtraining.cosechasapp.DBManager;
import com.techtraining.cosechasapp.Helper;
import com.techtraining.cosechasapp.LlenadoCamion;
import com.techtraining.cosechasapp.MainActivity;
import com.techtraining.cosechasapp.R;
import com.techtraining.cosechasapp.db.AppDatabase;
import com.techtraining.cosechasapp.db.Cosecha;
import com.techtraining.cosechasapp.db.CosechaDao;
import com.techtraining.cosechasapp.db.FilaCosecha;

public class FinalizarDespacho extends AsyncTask<Void, Void, Void> {
    private Context context;
    private AppDatabase appDatabase;
    public FinalizarDespacho(Context context) {
        this.context = context;
    }
    @Override
    protected Void doInBackground(Void... voids) {
        appDatabase = DBManager.getInstance(context);
        CosechaDao cosechaDao = appDatabase.cosechaDao();
        Cosecha cosecha = cosechaDao.loadById(context.getSharedPreferences(Helper.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).getString(Helper.CURRENT_COSECHA_ID_NAME, null));
        if (cosecha != null) {
            cosecha.estado = "F";
            cosechaDao.update(cosecha);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        Toast.makeText(context, R.string.despacho_finalizado, Toast.LENGTH_SHORT).show();
        LlenadoCamion activity = (LlenadoCamion) context;
        SharedPreferences.Editor editor = context.getSharedPreferences(Helper.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).edit();
        editor.remove(Helper.CURRENT_COSECHA_ID_NAME);
        editor.commit();
        Intent intent = new Intent(activity, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }
}
