package com.techtraining.cosechasapp.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;

import com.techtraining.cosechasapp.DBManager;
import com.techtraining.cosechasapp.Helper;
import com.techtraining.cosechasapp.ItemSueltosActivity;
import com.techtraining.cosechasapp.R;
import com.techtraining.cosechasapp.db.AppDatabase;
import com.techtraining.cosechasapp.db.Espesor;
import com.techtraining.cosechasapp.db.FilaCosecha;
import com.techtraining.cosechasapp.db.FilaSuelto;
import com.techtraining.cosechasapp.db.Largo;

import java.util.List;

public class CargarDatosSueltos extends AsyncTask<Void, Void, Void> {
    private Context context;
    private AppDatabase appDatabase;
    private FilaSuelto filaSuelto;
    private List<Largo> largos;
    private List<Espesor> espesores;
    private FilaCosecha filaCosecha;
    public CargarDatosSueltos(Context context) {
        this.context = context;
    }
    @Override
    protected Void doInBackground(Void... voids) {
        appDatabase = DBManager.getInstance(context);
        filaCosecha = appDatabase.filaCosechaDao().loadById(context.getSharedPreferences(Helper.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).getString(Helper.CURRENT_FILA_NAME, null));
        /*if (filaCosecha != null && filaCosecha.tipo.equals("E")) {
            List<FilaSuelto> filasSueltos = appDatabase.filaSueltoDao().loadByFila(filaCosecha.id);
            if (filasSueltos.size() > 0) {
                filaSuelto = filasSueltos.get(0);
            }
        }
        else*/
        filaSuelto = appDatabase.filaSueltoDao().loadById(context.getSharedPreferences(Helper.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).getString(Helper.CURRENT_ITEM_SUELTO_NAME, null));
        largos = appDatabase.largoDao().getAllActive();
        espesores = appDatabase.espesorDao().getAllActive();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        ItemSueltosActivity activity = (ItemSueltosActivity) context;
        activity.spnLargo.setAdapter(new ArrayAdapter<>(activity, android.R.layout.simple_spinner_dropdown_item, largos));
        activity.spnEspesor.setAdapter(new ArrayAdapter<>(activity, android.R.layout.simple_spinner_dropdown_item, espesores));
        if (filaSuelto != null) {
            activity.setTitle(activity.getString(R.string.datos_bloque) + ": (" + (filaCosecha.indice + 1) + ", " + (filaSuelto.indice + 1) + ")");
            activity.etPlantilla.setText(String.valueOf(filaSuelto.bultos));
            for (int i = 0; i < largos.size(); i++) {
                Largo largo = largos.get(i);
                if (filaSuelto.largoId == largo.id) {
                    activity.spnLargo.setSelection(i);
                    break;
                }
            }
            for (int i = 0; i < espesores.size(); i++) {
                Espesor espesor = espesores.get(i);
                if (filaSuelto.espesorId == espesor.id) {
                    activity.spnEspesor.setSelection(i);
                    break;
                }
            }
        }
        else{
            activity.etPlantilla.setText(String.valueOf(0));
            activity.setTitle(activity.getString(R.string.datos_bloque) + ": (" + (filaCosecha.indice + 1) + ", " + (activity.indice + 1) + ")");
        }
    }
}
