package com.techtraining.cosechasapp.tasks;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;

import com.techtraining.cosechasapp.DBManager;
import com.techtraining.cosechasapp.FilaCosechaCamionActivity;
import com.techtraining.cosechasapp.Helper;
import com.techtraining.cosechasapp.ItemCosechaActivity;
import com.techtraining.cosechasapp.LlenadoCamion;
import com.techtraining.cosechasapp.LlenadoSueltosActivity;
import com.techtraining.cosechasapp.R;
import com.techtraining.cosechasapp.adapters.FilasCosechasAdapter;
import com.techtraining.cosechasapp.db.AppDatabase;
import com.techtraining.cosechasapp.db.Camion;
import com.techtraining.cosechasapp.db.Cosecha;
import com.techtraining.cosechasapp.db.FilaCosecha;
import java.util.UUID;
import java.util.ArrayList;
import java.util.List;

public class CargarFilasCamion extends AsyncTask<Void, Void, Void> {
    private Context context;
    private AppDatabase appDatabase;
    private List<FilaCosecha> filas;
    private Cosecha cosecha;
    public CargarFilasCamion(Context context) {
        this.context = context;
    }
    @Override
    protected Void doInBackground(Void... voids) {
        appDatabase = DBManager.getInstance(context);
        SharedPreferences preferences = context.getSharedPreferences(Helper.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        String id = preferences.getString(Helper.CURRENT_COSECHA_ID_NAME, null);
        if (id != null) {
            cosecha = appDatabase.cosechaDao().loadById(id);
            filas = appDatabase.filaCosechaDao().loadByCosecha(cosecha.id);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (filas != null) {
            final LlenadoCamion activity = (LlenadoCamion) context;
            activity.lvFilas.setAdapter(new FilasCosechasAdapter(filas, context, cosecha));
        }
    }
}
