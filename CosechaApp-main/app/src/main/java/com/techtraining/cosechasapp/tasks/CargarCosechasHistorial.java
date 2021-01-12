package com.techtraining.cosechasapp.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.techtraining.cosechasapp.CosechasTerminadasActivity;
import com.techtraining.cosechasapp.DBManager;
import com.techtraining.cosechasapp.Helper;
import com.techtraining.cosechasapp.HistorialCosechaActivity;
import com.techtraining.cosechasapp.adapters.CosechasHistorialAdapter;
import com.techtraining.cosechasapp.adapters.CosechasTerminadasAdapter;
import com.techtraining.cosechasapp.db.AppDatabase;
import com.techtraining.cosechasapp.db.Cosecha;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CargarCosechasHistorial extends AsyncTask<Void, Void, Void> {
    private Context context;
    private List<Cosecha> cosechas;
    private ArrayList<Cosecha> currentCosechas;
    public CargarCosechasHistorial(Context context) {
        this.context = context;
        currentCosechas = new ArrayList<Cosecha>();
    }
    @Override
    protected Void doInBackground(Void... voids) {
        AppDatabase appDatabase = DBManager.getInstance(context);
        cosechas = appDatabase.cosechaDao().getCosechasForHistorial();
        Date date = new Date();
        long today = date.getTime()/1000L;
        for (Cosecha cosecha:cosechas){
            try {
                cosecha.filas = appDatabase.filaCosechaDao().loadByCosecha(cosecha.id);
                cosecha.troza = appDatabase.trozaDao().loadByCosecha(cosecha.id);
                cosecha.camionPlaca =appDatabase.camionDao().loadById(cosecha.camionId).placa;
                DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                date = (Date)formatter.parse(cosecha.fechaDespacho);
                date = new Date(date.getTime() + Helper.UNIX_DAYS_HISTORIAL);
                long despachoMaxDate = date.getTime()/1000L;
                if(despachoMaxDate >= today && cosecha.estado.equals("E")){
                    currentCosechas.add(cosecha);
                }else if(cosecha.estado.equals("F")){
                    currentCosechas.add(cosecha);
                }
                Log.e( "cosecha: ", cosecha.filas.toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        HistorialCosechaActivity activity = (HistorialCosechaActivity) context;
        activity.lvCosechas.setAdapter(new CosechasHistorialAdapter(currentCosechas, context));
    }
}
