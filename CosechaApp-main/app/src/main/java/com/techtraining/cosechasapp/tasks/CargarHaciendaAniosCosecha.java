package com.techtraining.cosechasapp.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.techtraining.cosechasapp.DBManager;
import com.techtraining.cosechasapp.NuevaCosechaActivity;
import com.techtraining.cosechasapp.db.AppDatabase;
import com.techtraining.cosechasapp.db.Espesor;
import com.techtraining.cosechasapp.db.Largo;
import com.techtraining.cosechasapp.db.Material;
import com.techtraining.cosechasapp.db.OrigenMadera;
import com.techtraining.cosechasapp.db.OrigenMaderaAnio;

import java.util.List;

public class CargarHaciendaAniosCosecha extends AsyncTask<Void, Void, Void> {
    private Context context;
    private AppDatabase appDatabase;
    private int origenMaderaId;
    private List<OrigenMaderaAnio> origenesMaderaAnio;
    public CargarHaciendaAniosCosecha(Context context, int origenMaderaId) {
        this.context = context;
        this.origenMaderaId = origenMaderaId;
    }
    @Override
    protected Void doInBackground(Void... voids) {
        appDatabase = DBManager.getInstance(context);
        origenesMaderaAnio = appDatabase.origenMaderaAnioDao().getAllActiveOrigenMadera(origenMaderaId);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        NuevaCosechaActivity activity = (NuevaCosechaActivity) context;
        if(origenesMaderaAnio.size()>0){
            activity.spnOrigenMaderaAnio.setAdapter(new ArrayAdapter<>(activity, android.R.layout.simple_spinner_dropdown_item, origenesMaderaAnio));
        }else{


        }
    }
}
