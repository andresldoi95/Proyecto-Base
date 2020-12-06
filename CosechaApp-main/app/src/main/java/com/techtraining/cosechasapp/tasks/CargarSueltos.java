package com.techtraining.cosechasapp.tasks;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;

import com.techtraining.cosechasapp.DBManager;
import com.techtraining.cosechasapp.Helper;
import com.techtraining.cosechasapp.ItemCosechaActivity;
import com.techtraining.cosechasapp.ItemSueltosActivity;
import com.techtraining.cosechasapp.LlenadoSueltosActivity;
import com.techtraining.cosechasapp.R;
import com.techtraining.cosechasapp.adapters.FilaSueltoAdapter;
import com.techtraining.cosechasapp.db.AppDatabase;
import com.techtraining.cosechasapp.db.FilaCosecha;
import com.techtraining.cosechasapp.db.FilaSuelto;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class CargarSueltos extends AsyncTask<Void, Void, Void> {
    private Context context;
    private AppDatabase appDatabase;
    private List<FilaSuelto> sueltos;
    private FilaCosecha filaCosecha;
    public FilaSueltoAdapter filaSueltoAdapter;
    private boolean isHistorial;

    public CargarSueltos(Context context) {
        this.context = context;
        isHistorial = context.getSharedPreferences(Helper.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).getBoolean(Helper.IS_HISTORIAL, false);
    }
    @Override
    protected Void doInBackground(Void... voids) {
        appDatabase = DBManager.getInstance(context);
        filaCosecha = appDatabase.filaCosechaDao().loadById(context.getSharedPreferences(Helper.SHARED_PREFERENCES_NAME, MODE_PRIVATE).getString(Helper.CURRENT_FILA_NAME, null));
        if (filaCosecha != null)
            sueltos = appDatabase.filaSueltoDao().loadByFila(filaCosecha.id);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (sueltos != null) {
            final LlenadoSueltosActivity activity = (LlenadoSueltosActivity) context;
            activity.setTitle(activity.getString(R.string.datos_bloque) + ": (" + (filaCosecha.indice + 1) + ")");
            filaSueltoAdapter = new FilaSueltoAdapter(sueltos, context);
            activity.lvSueltos.setAdapter(filaSueltoAdapter);
            activity.lvSueltos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if(!isHistorial){
                        String currentLlenado = activity.getSharedPreferences(Helper.SHARED_PREFERENCES_NAME, MODE_PRIVATE).getString(Helper.CURRENT_LLENADO_NAME, null);
                        Intent intent;
                        if ("S".equals(currentLlenado))
                            intent = new Intent(activity, ItemSueltosActivity.class);
                        else
                            intent = new Intent(activity, ItemCosechaActivity.class);
                        SharedPreferences.Editor editor = activity.getSharedPreferences(Helper.SHARED_PREFERENCES_NAME, MODE_PRIVATE).edit();
                        FilaSuelto filaSuelto = (FilaSuelto) activity.lvSueltos.getItemAtPosition(position);
                        editor.putString(Helper.CURRENT_ITEM_SUELTO_NAME, filaSuelto.id);
                        editor.commit();
                        intent.putExtra(Helper.INDICE_NAME, position);
                        activity.startActivity(intent);
                    }
                }
            });
        }
    }
}
