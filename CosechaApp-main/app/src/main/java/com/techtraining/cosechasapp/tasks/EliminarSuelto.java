package com.techtraining.cosechasapp.tasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.techtraining.cosechasapp.DBManager;
import com.techtraining.cosechasapp.Helper;
import com.techtraining.cosechasapp.R;
import com.techtraining.cosechasapp.adapters.FilaSueltoAdapter;
import com.techtraining.cosechasapp.db.AppDatabase;
import com.techtraining.cosechasapp.db.Camion;
import com.techtraining.cosechasapp.db.Cosecha;
import com.techtraining.cosechasapp.db.Espesor;
import com.techtraining.cosechasapp.db.FilaCosecha;
import com.techtraining.cosechasapp.db.FilaCosechaDao;
import com.techtraining.cosechasapp.db.FilaSuelto;
import com.techtraining.cosechasapp.db.FilaSueltoDao;
import com.techtraining.cosechasapp.db.FormatoEntrega;
import com.techtraining.cosechasapp.db.Largo;
import com.techtraining.cosechasapp.db.ParametroDao;
import com.techtraining.cosechasapp.db.TipoBulto;

import java.text.DecimalFormat;
import java.util.List;
import java.util.UUID;

public class EliminarSuelto extends AsyncTask<Void, Void, Void> {
    private Context context;
    private AppDatabase appDatabase;
    private FilaSuelto filaSuelto;
    private List<FilaSuelto> sueltos;
    private FilaSueltoAdapter filaSueltoAdapter;
    public EliminarSuelto(Context context, FilaSuelto filaSuelto, FilaSueltoAdapter filaSueltoAdapter) {
        this.context = context;
        this.filaSuelto = filaSuelto;
        this.filaSueltoAdapter=filaSueltoAdapter;
    }
    @Override
    protected Void doInBackground(Void... voids) {
        appDatabase = DBManager.getInstance(context);
        FilaSueltoDao filaSueltoDao = appDatabase.filaSueltoDao();
        FilaCosechaDao filaCosechaDao = appDatabase.filaCosechaDao();
        FilaCosecha filaCosecha = filaCosechaDao.loadById(filaSuelto.filaId);
        if (filaCosecha != null) {
            Cosecha cosecha = appDatabase.cosechaDao().loadById(filaCosecha.cosechaId);
            if (cosecha != null) {
                int indice = filaSuelto.indice;
                filaSueltoDao.delete(filaSuelto);
                sueltos = appDatabase.filaSueltoDao().loadByFila(filaCosecha.id);
                double bftTotal = 0;
                int bultosTotales = 0;
                for (FilaSuelto suelto:sueltos){
                    if(suelto.indice > indice){
                        suelto.indice = suelto.indice - 1;
                        filaSueltoDao.update(suelto);
                    }
                    bftTotal+=suelto.bft;
                    bultosTotales+=suelto.bultos;
                }
                sueltos = appDatabase.filaSueltoDao().loadByFila(filaCosecha.id);
                filaCosecha.bft = bftTotal;
                filaCosecha.bultos = bultosTotales;
                filaCosechaDao.update(filaCosecha);
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        Toast.makeText(context, R.string.bloque_eliminado, Toast.LENGTH_SHORT).show();
        filaSueltoAdapter.clear();
        filaSueltoAdapter.addAll(sueltos);
        filaSueltoAdapter.notifyDataSetChanged();
        // ((AppCompatActivity) context).finish();
    }
}
