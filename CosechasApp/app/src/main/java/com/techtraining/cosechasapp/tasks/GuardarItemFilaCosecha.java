package com.techtraining.cosechasapp.tasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import com.techtraining.cosechasapp.DBManager;
import com.techtraining.cosechasapp.Helper;
import com.techtraining.cosechasapp.R;
import com.techtraining.cosechasapp.db.AppDatabase;
import com.techtraining.cosechasapp.db.CosechaDao;
import com.techtraining.cosechasapp.db.Espesor;
import com.techtraining.cosechasapp.db.FilaCosecha;
import com.techtraining.cosechasapp.db.FilaCosechaDao;
import com.techtraining.cosechasapp.db.ItemFilaCosecha;
import com.techtraining.cosechasapp.db.ItemFilaCosechaDao;
import com.techtraining.cosechasapp.db.Largo;
import com.techtraining.cosechasapp.db.Parametro;
import com.techtraining.cosechasapp.db.ParametroDao;

import java.text.DecimalFormat;
import java.util.List;

public class GuardarItemFilaCosecha extends AsyncTask<Void, Void, Void> {
    private Context context;
    private AppDatabase appDatabase;
    private List<Parametro> parametros;
    private ItemFilaCosecha itemFilaCosecha;
    public GuardarItemFilaCosecha(Context context, ItemFilaCosecha itemFilaCosecha) {
        this.context = context;
        this.itemFilaCosecha = itemFilaCosecha;
    }
    @Override
    protected Void doInBackground(Void... voids) {
        appDatabase = DBManager.getInstance(context);
        ItemFilaCosechaDao itemFilaCosechaDao = appDatabase.itemFilaCosechaDao();
        ParametroDao parametroDao = appDatabase.parametroDao();
        CosechaDao cosechaDao = appDatabase.cosechaDao();
        parametros = parametroDao.getAll();
        if (parametros.size() > 0) {
            Parametro parametro = parametros.get(0);
            SharedPreferences preferences = context.getSharedPreferences(Helper.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
            ItemFilaCosecha itemFilaCosechaExitente = itemFilaCosechaDao.loadById(preferences.getString(Helper.CURRENT_ITEM_FILA_NAME, null));
            if (itemFilaCosechaExitente != null) {
                itemFilaCosechaExitente.largoId = itemFilaCosecha.largoId;
                itemFilaCosechaExitente.espesorId = itemFilaCosecha.espesorId;
                itemFilaCosechaExitente.bultos = itemFilaCosecha.bultos;
                itemFilaCosechaExitente.plantilla = itemFilaCosecha.plantilla;
                Largo largo = appDatabase.largoDao().loadById(itemFilaCosecha.largoId);
                Espesor espesor = appDatabase.espesorDao().loadById(itemFilaCosecha.espesorId);
                DecimalFormat df = new DecimalFormat("#.##");
                double bft = (espesor.valor * largo.valor) * parametro.factorHuecoSueltos * itemFilaCosecha.bultos * itemFilaCosecha.plantilla;
                itemFilaCosechaExitente.bft = Double.parseDouble(df.format(bft));
                itemFilaCosechaDao.update(itemFilaCosechaExitente);
                double bultosTotalesFila = 0;
                double bftTotalFila = 0;
                List<ItemFilaCosecha> filas = itemFilaCosechaDao.loadByFila(itemFilaCosechaExitente.filaId);
                for (int i = 0; i < filas.size(); i++) {
                    ItemFilaCosecha fila = filas.get(i);
                    bftTotalFila += fila.bft;
                    bultosTotalesFila += fila.bultos;
                }
                FilaCosechaDao filaCosechaDao = appDatabase.filaCosechaDao();
                FilaCosecha filaCosecha = filaCosechaDao.loadById(itemFilaCosechaExitente.filaId);
                if (filaCosecha != null) {
                    filaCosecha.bultos = Double.parseDouble(df.format(bultosTotalesFila));
                    filaCosecha.bft = Double.parseDouble(df.format(bftTotalFila));
                    filaCosechaDao.update(filaCosecha);
                }
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (parametros.size() > 0)
            Toast.makeText(context, R.string.bloque_guardado, Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(context, R.string.parametros_requeridos, Toast.LENGTH_SHORT).show();
    }
}
