package com.techtraining.cosechasapp.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;

import com.techtraining.cosechasapp.DBManager;
import com.techtraining.cosechasapp.Helper;
import com.techtraining.cosechasapp.ItemCosechaActivity;
import com.techtraining.cosechasapp.R;
import com.techtraining.cosechasapp.db.AppDatabase;
import com.techtraining.cosechasapp.db.FilaCosecha;
import com.techtraining.cosechasapp.db.FilaSuelto;
import com.techtraining.cosechasapp.db.TipoBulto;

import java.util.List;

public class CargarDatosItemCosecha extends AsyncTask<Void, Void, Void> {
    private Context context;
    private AppDatabase appDatabase;
    private FilaSuelto filaCosecha;
    private List<TipoBulto> tiposBulto;
    public CargarDatosItemCosecha(Context context) {
        this.context = context;
    }
    @Override
    protected Void doInBackground(Void... voids) {
        appDatabase = DBManager.getInstance(context);
        filaCosecha = appDatabase.filaSueltoDao().loadById(context.getSharedPreferences(Helper.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).getString(Helper.CURRENT_ITEM_SUELTO_NAME, null));
        tiposBulto = appDatabase.tipoBultoDao().getAllActive();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        ItemCosechaActivity activity = (ItemCosechaActivity) context;
        activity.spnTipoBulto.setAdapter(new ArrayAdapter<>(activity, android.R.layout.simple_spinner_dropdown_item, tiposBulto  ));
        if (filaCosecha != null) {
            activity.setTitle(activity.getString(R.string.datos_bloque) + ": (" + (filaCosecha.indice + 1) + ")");
            activity.etPlantilla.setText(String.valueOf(filaCosecha.bultos));
            for (int i = 0; i < tiposBulto.size(); i++) {
                TipoBulto tipoBulto = tiposBulto.get(i);
                if (filaCosecha.tipoBultoId == tipoBulto.id) {
                    activity.spnTipoBulto.setSelection(i);
                    break;
                }
            }
        }
        else
            activity.setTitle(activity.getString(R.string.datos_bloque));
    }
}
