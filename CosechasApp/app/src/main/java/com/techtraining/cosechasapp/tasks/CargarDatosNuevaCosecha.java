package com.techtraining.cosechasapp.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;

import com.techtraining.cosechasapp.DBManager;
import com.techtraining.cosechasapp.Helper;
import com.techtraining.cosechasapp.NuevaCosechaActivity;
import com.techtraining.cosechasapp.db.AppDatabase;
import com.techtraining.cosechasapp.db.Camion;
import com.techtraining.cosechasapp.db.Controlador;
import com.techtraining.cosechasapp.db.Cosecha;
import com.techtraining.cosechasapp.db.Destino;
import com.techtraining.cosechasapp.db.Material;
import com.techtraining.cosechasapp.db.Procedencia;

import java.util.List;

public class CargarDatosNuevaCosecha extends AsyncTask<Void, Void, Void> {
    private Context context;
    private NuevaCosechaActivity activity;
    private List<Camion> camiones;
    private List<Controlador> controladores;
    private List<Destino> destinos;
    private List<Procedencia> procedencias;
    private List<Material> materiales;
    private Cosecha currentCosecha;
    public CargarDatosNuevaCosecha(Context context) {
        this.context = context;
        activity = (NuevaCosechaActivity) context;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        activity.spnCamion.setAdapter(new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, camiones));
        activity.spnControlador.setAdapter(new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, controladores));
        activity.spnDestino.setAdapter(new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, destinos));
        activity.spnOrigen.setAdapter(new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, procedencias));
        activity.spnMaterial.setAdapter(new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, materiales));
        if (currentCosecha != null) {
            activity.etCodigoPo.setText(currentCosecha.codigoPo);
            for (int i = 0; i < camiones.size(); i++) {
                if (camiones.get(i).id == currentCosecha.camionId) {
                    activity.spnCamion.setSelection(i);
                    break;
                }
            }
            for (int i = 0; i < procedencias.size(); i++) {
                if (procedencias.get(i).id == currentCosecha.origenId) {
                    activity.spnOrigen.setSelection(i);
                    break;
                }
            }
            for (int i = 0; i < destinos.size(); i++) {
                if (destinos.get(i).id == currentCosecha.destinoId) {
                    activity.spnDestino.setSelection(i);
                    break;
                }
            }
            for (int i = 0; i < controladores.size(); i++) {
                if (controladores.get(i).id == currentCosecha.controladorId) {
                    activity.spnControlador.setSelection(i);
                    break;
                }
            }
            for (int i = 0; i < materiales.size(); i++) {
                if (materiales.get(i).id == currentCosecha.materialId) {
                    activity.spnMaterial.setSelection(i);
                    break;
                }
            }
        }
    }

    @Override
    protected Void doInBackground(Void... voids) {
        AppDatabase appDatabase = DBManager.getInstance(context);
        camiones = appDatabase.camionDao().getAllActive();
        controladores = appDatabase.controladorDao().getAllActive();
        destinos = appDatabase.destinoDao().getAllActive();
        procedencias = appDatabase.procedenciaDao().getAllActive();
        materiales = appDatabase.materialDao().getAllActive();
        String currentCosechaId = context.getSharedPreferences(Helper.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).getString(Helper.CURRENT_COSECHA_ID_NAME, null);
        if (currentCosechaId != null) {
            currentCosecha = appDatabase.cosechaDao().loadById(currentCosechaId);
        }
        return null;
    }
}
