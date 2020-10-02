package com.techtraining.cosechasapp.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;

import com.techtraining.cosechasapp.DBManager;
import com.techtraining.cosechasapp.Helper;
import com.techtraining.cosechasapp.LlenadoSueltosActivity;
import com.techtraining.cosechasapp.R;
import com.techtraining.cosechasapp.adapters.FilaSueltoAdapter;
import com.techtraining.cosechasapp.db.AppDatabase;
import com.techtraining.cosechasapp.db.FilaCosecha;
import com.techtraining.cosechasapp.db.FilaSuelto;

import java.util.List;

public class CargarSueltos extends AsyncTask<Void, Void, Void> {
    private Context context;
    private AppDatabase appDatabase;
    private List<FilaSuelto> sueltos;
    private FilaCosecha filaCosecha;
    public CargarSueltos(Context context) {
        this.context = context;
    }
    @Override
    protected Void doInBackground(Void... voids) {
        appDatabase = DBManager.getInstance(context);
        filaCosecha = appDatabase.filaCosechaDao().loadById(context.getSharedPreferences(Helper.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).getString(Helper.CURRENT_FILA_NAME, null));
        if (filaCosecha != null)
            sueltos = appDatabase.filaSueltoDao().loadByFila(filaCosecha.id);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (sueltos != null) {
            LlenadoSueltosActivity activity = (LlenadoSueltosActivity) context;
            activity.setTitle(activity.getString(R.string.datos_bloque) + ": (" + (filaCosecha.indice + 1) + ")");
            activity.lvSueltos.setAdapter(new FilaSueltoAdapter(sueltos, context));
            activity.lvSueltos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                }
            });
        }
    }
}
