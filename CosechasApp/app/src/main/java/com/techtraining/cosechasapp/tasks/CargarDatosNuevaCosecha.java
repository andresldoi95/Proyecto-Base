package com.techtraining.cosechasapp.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;

import com.techtraining.cosechasapp.DBManager;
import com.techtraining.cosechasapp.Helper;
import com.techtraining.cosechasapp.NuevaCosechaActivity;
import com.techtraining.cosechasapp.db.AppDatabase;
import com.techtraining.cosechasapp.db.Aserrador;
import com.techtraining.cosechasapp.db.Camion;
import com.techtraining.cosechasapp.db.Controlador;
import com.techtraining.cosechasapp.db.Cosecha;
import com.techtraining.cosechasapp.db.Destino;
import com.techtraining.cosechasapp.db.FormatoEntrega;
import com.techtraining.cosechasapp.db.OrigenHacienda;
import com.techtraining.cosechasapp.db.OrigenMadera;
import com.techtraining.cosechasapp.db.TipoMadera;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class CargarDatosNuevaCosecha extends AsyncTask<Void, Void, Void> {
    private Context context;
    private NuevaCosechaActivity activity;
    private List<Camion> camiones;
    private List<Controlador> controladores;
    private List<Destino> destinos;
    private List<Aserrador> aserradores;
    private List<OrigenMadera> origenesMadera;
    private List<TipoMadera> tiposMadera;
    private List<FormatoEntrega> formatosEntrega;
    private Cosecha currentCosecha;
    private List<OrigenHacienda> origenesHacienda;
    public CargarDatosNuevaCosecha(Context context) {
        this.context = context;
        activity = (NuevaCosechaActivity) context;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        activity.spnCamion.setAdapter(new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, camiones));
        activity.spnControlador.setAdapter(new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, controladores));
        activity.spnDestino.setAdapter(new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, destinos));
        activity.spnAserrador.setAdapter(new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, aserradores));
        activity.spnTipoMadera.setAdapter(new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, tiposMadera));
        activity.spnFormatoEntrega.setAdapter(new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, formatosEntrega));
        activity.spnOrigenMadera.setAdapter(new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, origenesMadera));
        activity.spnOrigenHacieda.setAdapter(new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, origenesHacienda));
        if (currentCosecha != null) {
            activity.etValorFlete.setText(String.valueOf(currentCosecha.valorFlete));
            activity.etCodigoPo.setText(currentCosecha.codigoPo);
            activity.etGuiaRemision.setText(currentCosecha.guiaRemision);
            activity.etGuiaForestal.setText(currentCosecha.guiaForestal);
            activity.etFechaTumba.setText(currentCosecha.fechaTumba);
            activity.etFechaDespacho.setText(currentCosecha.fechaDespacho);
            activity.etDiasT2k.setText(currentCosecha.diasT2k);
            for (int i = 0; i < camiones.size(); i++) {
                if (camiones.get(i).id == currentCosecha.camionId) {
                    activity.spnCamion.setSelection(i);
                    break;
                }
            }
            for (int i = 0; i < aserradores.size(); i++) {
                if (aserradores.get(i).id == currentCosecha.aserradorId) {
                    activity.spnAserrador.setSelection(i);
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
            for (int i = 0; i < tiposMadera.size(); i++) {
                if (tiposMadera.get(i).id == currentCosecha.tipoMaderaId) {
                    activity.spnTipoMadera.setSelection(i);
                    break;
                }
            }
            for (int i = 0; i < origenesMadera.size(); i++) {
                if (origenesMadera.get(i).id == currentCosecha.origenMaderaId) {
                    activity.spnOrigenMadera.setSelection(i);
                    break;
                }
            }
            for (int i = 0; i < formatosEntrega.size(); i++) {
                if (formatosEntrega.get(i).id == currentCosecha.formatoEntregaId) {
                    activity.spnFormatoEntrega.setSelection(i);
                    break;
                }
            }
        }
        else {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat(Helper.DEFAULT_DATE_FORMAT);
            activity.etFechaDespacho.setText(sdf.format(cal.getTime()));
        }
        TipoMadera tipoMadera = (TipoMadera) activity.spnTipoMadera.getSelectedItem();
        OrigenHacienda origenHacienda = (OrigenHacienda) activity.spnOrigenHacieda.getSelectedItem();
        if (tipoMadera != null && origenHacienda != null)
            new CargarMaterialCosecha(context, tipoMadera.id, origenHacienda.id).execute();
        activity.calcularValorFlete();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        AppDatabase appDatabase = DBManager.getInstance(context);
        camiones = appDatabase.camionDao().getAllActive();
        controladores = appDatabase.controladorDao().getAllActive();
        destinos = appDatabase.destinoDao().getAllActive();
        aserradores = appDatabase.aserradorDao().getAllActive();
        origenesMadera = appDatabase.origenMaderaDao().getAllActive();
        tiposMadera = appDatabase.tipoMaderaDao().getAllActive();
        formatosEntrega = appDatabase.formatoEntregaDao().getAllActive();
        origenesHacienda = appDatabase.origenHaciendaDao().getAllActive();
        String currentCosechaId = context.getSharedPreferences(Helper.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).getString(Helper.CURRENT_COSECHA_ID_NAME, null);
        if (currentCosechaId != null) {
            currentCosecha = appDatabase.cosechaDao().loadById(currentCosechaId);
        }
        return null;
    }
}
