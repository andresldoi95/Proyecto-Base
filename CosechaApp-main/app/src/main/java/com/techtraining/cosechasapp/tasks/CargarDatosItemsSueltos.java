package com.techtraining.cosechasapp.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;

import com.techtraining.cosechasapp.DBManager;
import com.techtraining.cosechasapp.Helper;
import com.techtraining.cosechasapp.R;
import com.techtraining.cosechasapp.db.AppDatabase;
import com.techtraining.cosechasapp.db.Espesor;
import com.techtraining.cosechasapp.db.FilaSuelto;
import com.techtraining.cosechasapp.db.Largo;
import com.techtraining.cosechasapp.db.TipoBulto;

public class CargarDatosItemsSueltos extends AsyncTask<Void, Void, Void> {
    private Context context;
    private AppDatabase appDatabase;
    private FilaSuelto filaSuelto;
    private TextView tvInformacion;
    private Espesor espesor;
    private Largo largo;
    private TipoBulto tipoBulto;
    public CargarDatosItemsSueltos(Context context, FilaSuelto filaSuelto, TextView tvInformacion) {
        this.context = context;
        this.filaSuelto = filaSuelto;
        this.tvInformacion = tvInformacion;
    }
    @Override
    protected Void doInBackground(Void... voids) {
        appDatabase = DBManager.getInstance(context);
        espesor = appDatabase.espesorDao().loadById(filaSuelto.espesorId);
        largo = appDatabase.largoDao().loadById(filaSuelto.largoId);
        tipoBulto = appDatabase.tipoBultoDao().loadById(filaSuelto.tipoBultoId);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        String informacion = "";
        String tipoLlenado = context.getSharedPreferences(Helper.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).getString(Helper.CURRENT_LLENADO_NAME, null);
        informacion += "\n" + ("S".equals(tipoLlenado)?context.getString(R.string.plantilla):context.getString(R.string.bultos)) + ": " + filaSuelto.bultos;
        if (espesor != null)
            informacion += "\n" +  context.getString(R.string.espesor) + ": " + espesor.valor;
        if (largo != null)
            informacion += "\n" +  context.getString(R.string.largo) + ": " + largo.valor;
        if (tipoBulto != null)
            informacion += "\n" + context.getString(R.string.tipo_bulto) + ": " + tipoBulto.toString();
        tvInformacion.setText(informacion);
    }
}