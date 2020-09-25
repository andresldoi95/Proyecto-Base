package com.techtraining.cosechasapp.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;

import com.techtraining.cosechasapp.DBManager;
import com.techtraining.cosechasapp.Helper;
import com.techtraining.cosechasapp.ItemCosechaActivity;
import com.techtraining.cosechasapp.R;
import com.techtraining.cosechasapp.db.AppDatabase;
import com.techtraining.cosechasapp.db.Espesor;
import com.techtraining.cosechasapp.db.ItemFilaCosecha;
import com.techtraining.cosechasapp.db.Largo;

import java.util.List;

public class CargarDatosItemCosecha extends AsyncTask<Void, Void, Void> {
    private Context context;
    private AppDatabase appDatabase;
    private ItemFilaCosecha itemFilaCosecha;
    private List<Espesor> espesores;
    private List<Largo> largos;
    public CargarDatosItemCosecha(Context context) {
        this.context = context;
    }
    @Override
    protected Void doInBackground(Void... voids) {
        appDatabase = DBManager.getInstance(context);
        itemFilaCosecha = appDatabase.itemFilaCosechaDao().loadById(context.getSharedPreferences(Helper.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).getString(Helper.CURRENT_ITEM_FILA_NAME, null));
        espesores = appDatabase.espesorDao().getAllActive();
        largos = appDatabase.largoDao().getAllActive();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (itemFilaCosecha != null) {
            ItemCosechaActivity activity = (ItemCosechaActivity) context;
            activity.setTitle(activity.getString(R.string.datos_bloque) + ": (" + (itemFilaCosecha.fila + 1) + "," + (itemFilaCosecha.columna + 1) + ")");
            activity.spnLargo.setAdapter(new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, largos  ));
            activity.spnEspesor.setAdapter(new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, espesores ));
            activity.etBultos.setText(String.valueOf(itemFilaCosecha.bultos));
            activity.etPlantilla.setText(String.valueOf(itemFilaCosecha.plantilla));
            for (int i = 0; i < largos.size(); i++) {
                Largo largo = largos.get(i);
                if (itemFilaCosecha.largoId == largo.id) {
                    activity.spnLargo.setSelection(i);
                    break;
                }
            }
            for (int i = 0; i < espesores.size(); i++) {
                Espesor espesor = espesores.get(i);
                if (itemFilaCosecha.espesorId == espesor.id) {
                    activity.spnEspesor.setSelection(i);
                    break;
                }
            }
        }
    }
}
