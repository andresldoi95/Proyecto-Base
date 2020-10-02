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
    private Camion camion;
    public CargarFilasCamion(Context context) {
        this.context = context;
    }
    @Override
    protected Void doInBackground(Void... voids) {
        appDatabase = DBManager.getInstance(context);
        SharedPreferences preferences = context.getSharedPreferences(Helper.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        String id = preferences.getString(Helper.CURRENT_COSECHA_ID_NAME, null);
        if (id != null) {
            Cosecha cosecha = appDatabase.cosechaDao().loadById(id);
            camion = appDatabase.camionDao().loadById(cosecha.camionId);
            filas = appDatabase.filaCosechaDao().loadByCosecha(cosecha.id);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (filas != null) {
            final LlenadoCamion activity = (LlenadoCamion) context;
            activity.lvFilas.setAdapter(new FilasCosechasAdapter(filas, context));
            activity.lvFilas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (camion != null) {
                        FilaCosecha fila = filas .get(position);
                        final SharedPreferences.Editor editor = activity.getSharedPreferences(Helper.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).edit();
                        editor.putString(Helper.CURRENT_FILA_NAME, fila.id);
                        editor.commit();
                        if (camion.tipoCamion.equals("B")) {
                            Intent intent = new Intent(activity, ItemCosechaActivity.class);
                            editor.putString(Helper.CURRENT_LLENADO_NAME, "B");
                            editor.commit();
                            activity.startActivity(intent);
                        }
                        else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setCancelable(true);
                            builder.setTitle(context.getString(R.string.titulo_confirmacion));
                            builder.setMessage(context.getString(R.string.mensaje_confirmacion));
                            builder.setPositiveButton(context.getString(R.string.respuesta_bultos),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(activity, ItemCosechaActivity.class);
                                            editor.putString(Helper.CURRENT_LLENADO_NAME, "B");
                                            editor.commit();
                                            activity.startActivity(intent);
                                        }
                                    });
                            builder.setNegativeButton(R.string.respuesta_sueltos, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(activity, LlenadoSueltosActivity.class);
                                    editor.putString(Helper.CURRENT_LLENADO_NAME, "S");
                                    editor.commit();
                                    activity.startActivity(intent);
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    }
                }
            });
        }
    }
}
