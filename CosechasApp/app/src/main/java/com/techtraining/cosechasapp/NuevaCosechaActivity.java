package com.techtraining.cosechasapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.techtraining.cosechasapp.controls.CustomDatePicker;
import com.techtraining.cosechasapp.db.Aserrador;
import com.techtraining.cosechasapp.db.Camion;
import com.techtraining.cosechasapp.db.Controlador;
import com.techtraining.cosechasapp.db.Cosecha;
import com.techtraining.cosechasapp.db.Destino;
import com.techtraining.cosechasapp.db.FormatoEntrega;
import com.techtraining.cosechasapp.db.Material;
import com.techtraining.cosechasapp.db.OrigenMadera;
import com.techtraining.cosechasapp.db.TipoMadera;
import com.techtraining.cosechasapp.tasks.CalcularFlete;
import com.techtraining.cosechasapp.tasks.CargarDatosNuevaCosecha;
import com.techtraining.cosechasapp.tasks.GuardarCabeceraCosecha;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class NuevaCosechaActivity extends AppCompatActivity {
    public EditText etDiasT2k;
    public EditText etFechaDespacho;
    public Spinner spnCamion;
    public Spinner spnControlador;
    public Spinner spnDestino;
    public Spinner spnAserrador;
    public Spinner spnMaterial;
    public EditText etCodigoPo;
    public Spinner spnTipoMadera;
    public Spinner spnFormatoEntrega;
    public Spinner spnOrigenMadera;
    public EditText etFechaTumba;
    public EditText etGuiaForestal;
    public EditText etGuiaRemision;
    public RadioButton rbBultos;
    public RadioButton rbSueltos;
    public EditText etValorFlete;
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
        Aserrador selectedAserrador = (Aserrador) spnAserrador.getSelectedItem();
        if (selectedAserrador == null) {
            Toast.makeText(this, R.string.aserrador_requerido, Toast.LENGTH_SHORT).show();
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
        if (etValorFlete.getText().toString().length() == 0) {
            Toast.makeText(this, R.string.valor_flete_requerido, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    private void calcularDiast2k (String fecha) {
        SimpleDateFormat sdf = new SimpleDateFormat(Helper.DEFAULT_DATE_FORMAT);
        Date currentTime = Calendar.getInstance().getTime();
        try {
            etDiasT2k.setText(String.valueOf(Math.abs(TimeUnit.DAYS.convert(sdf.parse(fecha).getTime() - currentTime.getTime(), TimeUnit.MILLISECONDS))));
        }
        catch (ParseException ex) {
            Log.e(NuevaCosechaActivity.class.getName(), ex.getMessage());
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.nueva_cosecha);
        setContentView(R.layout.activity_nueva_cosecha);
        etValorFlete = (EditText) findViewById(R.id.etValorFlete);
        rbBultos = (RadioButton) findViewById(R.id.rbBultos);
        rbSueltos = (RadioButton) findViewById(R.id.rbSueltos);
        etDiasT2k = findViewById(R.id.etDiasT2k);
        etFechaDespacho = findViewById(R.id.etFechaDespacho);
        etGuiaForestal = findViewById(R.id.etGuiaForestal);
        etGuiaRemision = findViewById(R.id.etGuiaRemision);
        spnCamion = findViewById(R.id.spnCamion);
        spnControlador = findViewById(R.id.spnControlador);
        spnDestino = findViewById(R.id.spnDestino);
        spnAserrador = findViewById(R.id.spnAserrador);
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
                        final String selectedDate =  year + "-" + (finalMonth  < 9?"0":"") + finalMonth + "-" + dayOfMonth;
                        etFechaTumba.setText(selectedDate);
                    }
                });
                customDatePicker.show(NuevaCosechaActivity.this.getSupportFragmentManager(), "datePicker");
            }
        });
        etFechaTumba.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                calcularDiast2k(s.toString());
            }
        });
        spnOrigenMadera.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                calcularValorFlete();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                calcularValorFlete();
            }
        });
        spnDestino.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                calcularValorFlete();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                calcularValorFlete();
            }
        });
    }

    @Override
    protected void onResume() {
        new CargarDatosNuevaCosecha(this).execute();
        super.onResume();
    }

    public void calcularValorFlete() {
        OrigenMadera origenMadera = (OrigenMadera) spnOrigenMadera.getSelectedItem();
        Destino destino = (Destino) spnDestino.getSelectedItem();
        if (origenMadera != null && destino != null) {
            new CalcularFlete(this, origenMadera.id, destino.id).execute();
        }
    }
    private void guardar() {
        final SharedPreferences preferences = getSharedPreferences(Helper.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        Camion selectedCamion = (Camion) spnCamion.getSelectedItem();
        Controlador selectedControlador = (Controlador) spnControlador.getSelectedItem();
        Destino selectedDestino = (Destino) spnDestino.getSelectedItem();
        Material selectedMaterial = (Material) spnMaterial.getSelectedItem();
        Aserrador selectedAserrador = (Aserrador) spnAserrador.getSelectedItem();
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
        cosecha.aserradorId = selectedAserrador.id;
        cosecha.fechaTumba = etFechaTumba.getText().toString();
        cosecha.fechaDespacho = etFechaDespacho.getText().toString();
        cosecha.diasT2k = etDiasT2k.getText().toString();
        cosecha.guiaForestal = etGuiaForestal.getText().toString();
        cosecha.guiaRemision = etGuiaRemision.getText().toString();
        cosecha.tipoMaderaId = selectedTipoMadera.id;
        cosecha.formatoEntregaId = selectedFormatoEntrega.id;
        cosecha.origenMaderaId = selectedOrigenMadera.id;
        cosecha.estado = "P";
        cosecha.valorFlete = Double.parseDouble(etValorFlete.getText().toString());
        cosecha.tipoLlenado = rbBultos.isChecked()?"B":"S";
        new GuardarCabeceraCosecha(NuevaCosechaActivity.this, cosecha).execute();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.nueva_cosecha_menu, menu);
        return true;
    }
    @Override
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