package com.techtraining.cosechasapp.tasks;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;

import com.techtraining.cosechasapp.DBManager;
import com.techtraining.cosechasapp.FilaCosechaCamionActivity;
import com.techtraining.cosechasapp.Helper;
import com.techtraining.cosechasapp.ItemCosechaActivity;
import com.techtraining.cosechasapp.R;
import com.techtraining.cosechasapp.adapters.FilaCosechaCamionAdapter;
import com.techtraining.cosechasapp.db.AppDatabase;
import com.techtraining.cosechasapp.db.FilaCosecha;
import com.techtraining.cosechasapp.db.FilaCosechaDao;
import com.techtraining.cosechasapp.db.ItemFilaCosecha;
import com.techtraining.cosechasapp.db.ItemFilaCosechaDao;
import java.util.List;

public class GenerarMatrizCosecha extends AsyncTask<Void, Void, Void> {
    private Context context;
    private FilaCosecha filaCosecha;
    private AppDatabase appDatabase;
    private FilaCosechaDao filaCosechaDao;
    private ItemFilaCosechaDao itemFilaCosechaDao;
    private List<ItemFilaCosecha> matriz;
    public GenerarMatrizCosecha(Context context) {
        this.context = context;
    }
    @Override
    protected Void doInBackground(Void... voids) {
        appDatabase = DBManager.getInstance(context);
        filaCosechaDao = appDatabase.filaCosechaDao();
        itemFilaCosechaDao = appDatabase.itemFilaCosechaDao();
        SharedPreferences preferences = context.getSharedPreferences(Helper.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        filaCosecha = filaCosechaDao.loadById(preferences.getString(Helper.CURRENT_FILA_NAME, null));
        if (filaCosecha != null)
            matriz = itemFilaCosechaDao.loadByFila(filaCosecha.id);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (filaCosecha != null) {
            final FilaCosechaCamionActivity activity = (FilaCosechaCamionActivity) context;
            activity.setTitle(context.getString(R.string.fila) + " " + String.valueOf(filaCosecha.indice + 1));
            activity.grvMatriz.setNumColumns(filaCosecha.columnas);
            activity.grvMatriz.setAdapter(new FilaCosechaCamionAdapter(context, matriz));
            activity.grvMatriz.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    SharedPreferences.Editor editor = activity.getSharedPreferences(Helper.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).edit();
                    editor.putString(Helper.CURRENT_ITEM_FILA_NAME, matriz.get(position).id);
                    editor.commit();
                    Intent intent = new Intent(activity, ItemCosechaActivity.class);
                    activity.startActivity(intent);
                }
            });
        }
    }
}
