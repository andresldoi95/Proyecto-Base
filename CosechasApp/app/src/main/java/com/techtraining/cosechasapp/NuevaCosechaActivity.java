package com.techtraining.cosechasapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.techtraining.cosechasapp.controls.CustomDatePicker;
import com.techtraining.cosechasapp.db.Camion;
import com.techtraining.cosechasapp.db.Controlador;
import com.techtraining.cosechasapp.db.Cosecha;
import com.techtraining.cosechasapp.db.Destino;
import com.techtraining.cosechasapp.db.FormatoEntrega;
import com.techtraining.cosechasapp.db.Material;
import com.techtraining.cosechasapp.db.OrigenMadera;
import com.techtraining.cosechasapp.db.Procedencia;
import com.techtraining.cosechasapp.db.TipoMadera;
import com.techtraining.cosechasapp.tasks.CargarDatosNuevaCosecha;
import com.techtraining.cosechasapp.tasks.GuardarCabeceraCosecha;

import java.util.UUID;

public class NuevaCosechaActivity extends AppCompatActivity {
    public Spinner spnCamion;
    public Spinner spnControlador;
    public Spinner spnDestino;
    public Spinner spnOrigen;
    public Spinner spnMaterial;
    public EditText etCodigoPo;
    public Spinner spnTipoMadera;
    public Spinner spnFormatoEntrega;
    public Spinner spnOrigenMadera;
    public EditText etFechaTumba;
    public EditText etGuiaForestal;
    public EditText etGuiaRemision;
    private boolean formularioValido() {
        Camion selectedCamion = (Camion) spnCamion.getSelectedItem();
        if (selectedCamion == null) {
            Toast.makeText(this, R.string.camion_requerido, Toast.LENGTH_SHORT).show();
            return false;
        }
        Controlador selectedControlador = (Controlador) spnControlador.getSelectedItem();
        if (selectedControlador == null) {
            Toast.makeText(this, R.string.controlador_requerido, Toast.LENGTH_SHORT).show();
            return false;
        }
        Destino selectedDestino = (Destino) spnDestino.getSelectedItem();
        if (selectedDestino == null) {
            Toast.makeText(this, R.string.destino_requerido, Toast.LENGTH_SHORT).show();
            return false;
        }
        Material selectedMaterial = (Material) spnMaterial.getSelectedItem();
        if (selectedMaterial == null) {
            Toast.makeText(this, R.string.material_requerido, Toast.LENGTH_SHORT).show();
            return false;
        }
        Procedencia selectedProcedencia = (Procedencia) spnOrigen.getSelectedItem();
        if (selectedProcedencia == null) {
            Toast.makeText(this, R.string.procedencia_requerida, Toast.LENGTH_SHORT).show();
            return false;
        }
        String codigoPo = etCodigoPo.getText().toString();
        if (codigoPo.length() == 0) {
            Toast.makeText(this, R.string.codigo_po_requerido, Toast.LENGTH_SHORT).show();
            return false;
        }
        String fechaTumba = etFechaTumba.getText().toString();
        if (fechaTumba.length() == 0) {
            Toast.makeText(this, R.string.fecha_tumba_requerida, Toast.LENGTH_SHORT).show();
            return false;
        }
        TipoMadera tipoMadera = (TipoMadera) spnTipoMadera.getSelectedItem();
        if (tipoMadera == null) {
            Toast.makeText(this, R.string.tipo_madera_requerido, Toast.LENGTH_SHORT).show();
            return false;
        }
        FormatoEntrega formatoEntrega = (FormatoEntrega) spnFormatoEntrega.getSelectedItem();
        if (formatoEntrega == null) {
            Toast.makeText(this, R.string.formato_entrega_requerido, Toast.LENGTH_SHORT).show();
            return false;
        }
        OrigenMadera origenMadera = (OrigenMadera) spnOrigenMadera.getSelectedItem();
        if (origenMadera == null) {
            Toast.makeText(this, R.string.origen_madera_requerido, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.nueva_cosecha);
        setContentView(R.layout.activity_nueva_cosecha);
        etGuiaForestal = findViewById(R.id.etGuiaForestal);
        etGuiaRemision = findViewById(R.id.etGuiaRemision);
        spnCamion = findViewById(R.id.spnCamion);
        spnControlador = findViewById(R.id.spnControlador);
        spnDestino = findViewById(R.id.spnDestino);
        spnOrigen = findViewById(R.id.spnOrigen);
        spnMaterial = findViewById(R.id.spnMaterial);
        etCodigoPo = findViewById(R.id.etCodigoPo);
        spnTipoMadera = findViewById(R.id.spnTipoMadera);
        spnFormatoEntrega = findViewById(R.id.spnFormatoEntrega);
        spnOrigenMadera = findViewById(R.id.spnOrigenMadera);
        etFechaTumba = findViewById(R.id.etFechaTumba);
        etFechaTumba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDatePicker customDatePicker = CustomDatePicker.newInstance(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        int finalMonth = month+1;
                        final String selectedDate =  year + "-" + (finalMonth > 0?"0":"") + finalMonth + "-" + dayOfMonth;
                        etFechaTumba.setText(selectedDate);
                    }
                });
                customDatePicker.show(NuevaCosechaActivity.this.getSupportFragmentManager(), "datePicker");
            }
        });
        new CargarDatosNuevaCosecha(this).execute();
    }
    private void guardar() {
        final SharedPreferences preferences = getSharedPreferences(Helper.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        Camion selectedCamion = (Camion) spnCamion.getSelectedItem();
        Controlador selectedControlador = (Controlador) spnControlador.getSelectedItem();
        Destino selectedDestino = (Destino) spnDestino.getSelectedItem();
        Material selectedMaterial = (Material) spnMaterial.getSelectedItem();
        Procedencia selectedProcedencia = (Procedencia) spnOrigen.getSelectedItem();
        TipoMadera selectedTipoMadera = (TipoMadera) spnTipoMadera.getSelectedItem();
        FormatoEntrega selectedFormatoEntrega = (FormatoEntrega) spnFormatoEntrega.getSelectedItem();
        OrigenMadera selectedOrigenMadera = (OrigenMadera) spnOrigenMadera.getSelectedItem();
        String cosechaId = preferences.getString(Helper.CURRENT_COSECHA_ID_NAME, null);
        if (cosechaId == null){
            UUID uuid = UUID.randomUUID();
            cosechaId = uuid.toString();
        }
        Cosecha cosecha = new Cosecha();
        cosecha.id = cosechaId;
        cosecha.camionId = selectedCamion.id;
        cosecha.codigoPo = etCodigoPo.getText().toString();
        cosecha.controladorId = selectedControlador.id;
        cosecha.destinoId = selectedDestino.id;
        cosecha.materialId = selectedMaterial.id;
        cosecha.origenId = selectedProcedencia.id;
        cosecha.fechaTumba = etFechaTumba.getText().toString();
        cosecha.guiaForestal = etGuiaForestal.getText().toString();
        cosecha.guiaRemision = etGuiaRemision.getText().toString();
        cosecha.tipoMaderaId = selectedTipoMadera.id;
        cosecha.formatoEntregaId = selectedFormatoEntrega.id;
        cosecha.origenMaderaId = selectedOrigenMadera.id;
        cosecha.estado = "P";
        new GuardarCabeceraCosecha(NuevaCosechaActivity.this, cosecha).execute();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.nueva_cosecha_menu, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_continuar:
                if (formularioValido()) {
                    guardar();
                    Intent intent = new Intent(NuevaCosechaActivity.this, LlenadoCamion.class);
                    startActivity(intent);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}