package com.techtraining.cosechasapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
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
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.techtraining.cosechasapp.controls.CustomDatePicker;
import com.techtraining.cosechasapp.db.AppDatabase;
import com.techtraining.cosechasapp.db.Aserrador;
import com.techtraining.cosechasapp.db.Camion;
import com.techtraining.cosechasapp.db.Controlador;
import com.techtraining.cosechasapp.db.Cosecha;
import com.techtraining.cosechasapp.db.Destino;
import com.techtraining.cosechasapp.db.FormatoEntrega;
import com.techtraining.cosechasapp.db.OrigenHacienda;
import com.techtraining.cosechasapp.db.OrigenMadera;
import com.techtraining.cosechasapp.db.OrigenMaderaAnio;
import com.techtraining.cosechasapp.db.TipoMadera;
import com.techtraining.cosechasapp.tasks.CalcularFlete;
import com.techtraining.cosechasapp.tasks.CargarDatosNuevaCosecha;
import com.techtraining.cosechasapp.tasks.CargarHaciendaAniosCosecha;
import com.techtraining.cosechasapp.tasks.CargarMaterialCosecha;
import com.techtraining.cosechasapp.tasks.GuardarCabeceraCosecha;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class NuevaCosechaActivity extends AppCompatActivity {
    public SearchableSpinner spnOrigenHacieda;
    public EditText etDiasT2k;
    public EditText etFechaDespacho;
    public SearchableSpinner spnCamion;
    public SearchableSpinner spnDestino;
    public SearchableSpinner spnAserrador;
    public EditText etCodigoPo;
    public SearchableSpinner spnTipoMadera;
    public SearchableSpinner spnFormatoEntrega;
    public SearchableSpinner spnOrigenMadera;
    public SearchableSpinner spnOrigenMaderaAnio;
    public EditText etFechaTumba;
    public EditText etGuiaForestal;
    public EditText etGuiaRemision;
    public EditText etValorFlete;
    public int materialId = -1;
    public EditText etMaterial;
    private boolean isHistorial = false;
    private boolean formularioValido() {
        Camion selectedCamion = (Camion) spnCamion.getSelectedItem();
        if (selectedCamion == null) {
            Toast.makeText(this, R.string.camion_requerido, Toast.LENGTH_SHORT).show();
            return false;
        }
        Destino selectedDestino = (Destino) spnDestino.getSelectedItem();
        if (selectedDestino == null) {
            Toast.makeText(this, R.string.destino_requerido, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (materialId == -1) {
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
        String guiaRemision = etGuiaRemision.getText().toString();
        if (guiaRemision.length() <15 ) {
            Toast.makeText(this, "La Guía de Remisión debe contener 15 dígitos.", Toast.LENGTH_SHORT).show();
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

        OrigenMaderaAnio origenMaderaAnio = (OrigenMaderaAnio) spnOrigenMaderaAnio.getSelectedItem();
        if (origenMaderaAnio == null) {
            Toast.makeText(this, R.string.origen_madera_anio_requerido, Toast.LENGTH_SHORT).show();
            return false;
        }

        OrigenHacienda origenHacienda = (OrigenHacienda) spnOrigenHacieda.getSelectedItem();
        if (origenHacienda == null) {
            Toast.makeText(this, R.string.origen_hacienda_requerida, Toast.LENGTH_SHORT).show();
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
        isHistorial = getSharedPreferences(Helper.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).getBoolean(Helper.IS_HISTORIAL, false);
        spnOrigenHacieda = findViewById(R.id.spnOrigenHacienda);
        etValorFlete = (EditText) findViewById(R.id.etValorFlete);
        etDiasT2k = findViewById(R.id.etDiasT2k);
        etFechaDespacho = findViewById(R.id.etFechaDespacho);
        etGuiaForestal = findViewById(R.id.etGuiaForestal);
        etGuiaRemision = findViewById(R.id.etGuiaRemision);
        spnCamion = findViewById(R.id.spnCamion);
        spnDestino = findViewById(R.id.spnDestino);
        spnAserrador = findViewById(R.id.spnAserrador);
        etCodigoPo = findViewById(R.id.etCodigoPo);
        etMaterial = findViewById(R.id.etMaterial);
        spnTipoMadera = findViewById(R.id.spnTipoMadera);
        spnTipoMadera.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setearMaterial();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spnFormatoEntrega = findViewById(R.id.spnFormatoEntrega);
        spnFormatoEntrega.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    if(!isHistorial){
                        FormatoEntrega formatoEntrega = (FormatoEntrega) spnFormatoEntrega.getSelectedItem();
                        if(formatoEntrega!=null){
                            if(formatoEntrega.id==3){
                                spnTipoMadera.setEnabled(false);
                                spnTipoMadera.setFocusable(false);
                                spnTipoMadera.setSelection(0);
                            }else{
                                spnTipoMadera.setEnabled(true);
                                spnTipoMadera.setFocusable(true);
                            }
                        }else{
                            spnTipoMadera.setEnabled(true);
                            spnTipoMadera.setFocusable(true);
                        }
                    }

                }catch (Exception e){
                    Log.e( "onItemSelected: ", e.toString() );
                }
                setearMaterial();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spnOrigenMadera = findViewById(R.id.spnOrigenMadera);
        spnOrigenMaderaAnio = findViewById(R.id.spnOrigenMaderaAnio);




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

                try {
                    setearAniosHacienda();


                }catch (Exception e){

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                setearAniosHacienda();
                calcularValorFlete();
            }
        });
        spnOrigenHacieda.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i("***", spnOrigenHacieda.getSelectedItem().toString());
                setearMaterial();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
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

        spnOrigenHacieda.setTitle(getString(R.string.ingrese_origen_madera));
        spnOrigenHacieda.setPositiveButton(getString(R.string.close));

        spnCamion.setTitle(getString(R.string.ingrese_origen_camion));
        spnCamion.setPositiveButton(getString(R.string.close));

        spnDestino.setTitle(getString(R.string.ingrese_desitno));
        spnDestino.setPositiveButton(getString(R.string.close));

        spnAserrador.setTitle(getString(R.string.ingrese_asserador));
        spnAserrador.setPositiveButton(getString(R.string.close));

        spnTipoMadera.setTitle(getString(R.string.ingrese_tipo_madera));
        spnTipoMadera.setPositiveButton(getString(R.string.close));

        spnFormatoEntrega.setTitle(getString(R.string.ingrese_formato_entrega));
        spnFormatoEntrega.setPositiveButton(getString(R.string.close));

        spnOrigenMadera.setTitle(getString(R.string.ingrese_origen_hacienda));
        spnOrigenMadera.setPositiveButton(getString(R.string.close));

        spnOrigenMaderaAnio.setTitle(getString(R.string.ingrese_origen_hacienda_anio));
        spnOrigenMaderaAnio.setPositiveButton(getString(R.string.close));

        if(isHistorial){
            spnOrigenHacieda.setEnabled(false);
            etValorFlete.setEnabled(false);
            etDiasT2k.setEnabled(false);
            etFechaDespacho.setEnabled(false);
            etGuiaForestal.setEnabled(false);
            etGuiaRemision.setEnabled(false);
            spnCamion.setEnabled(false);
            spnDestino.setEnabled(false);
            spnAserrador.setEnabled(false);
            etCodigoPo.setEnabled(false);
            etMaterial.setEnabled(false);
            spnTipoMadera.setEnabled(false);
            spnOrigenMadera.setEnabled(false);
            spnOrigenMaderaAnio.setEnabled(false);
            spnFormatoEntrega.setEnabled(false);
            etFechaTumba.setEnabled(false);
        }
    }

    private void setearMaterial() {
        TipoMadera tipoMadera = (TipoMadera) spnTipoMadera.getSelectedItem();
        OrigenHacienda origenHacienda = (OrigenHacienda) spnOrigenHacieda.getSelectedItem();
        if (tipoMadera != null && origenHacienda != null) {
            new CargarMaterialCosecha(this, tipoMadera.id, origenHacienda.id).execute();
        }
    }

    private void setearAniosHacienda() {
        OrigenMadera origenMadera = (OrigenMadera) spnOrigenMadera.getSelectedItem();
        if (origenMadera != null ) {
            new CargarHaciendaAniosCosecha(this, origenMadera.id).execute();
        }
    }

    @Override
    protected void onResume() {
        isHistorial = getSharedPreferences(Helper.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).getBoolean(Helper.IS_HISTORIAL, false);
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
        Destino selectedDestino = (Destino) spnDestino.getSelectedItem();
        Aserrador selectedAserrador = (Aserrador) spnAserrador.getSelectedItem();
        TipoMadera selectedTipoMadera = (TipoMadera) spnTipoMadera.getSelectedItem();
        FormatoEntrega selectedFormatoEntrega = (FormatoEntrega) spnFormatoEntrega.getSelectedItem();
        OrigenMadera selectedOrigenMadera = (OrigenMadera) spnOrigenMadera.getSelectedItem();
        OrigenMaderaAnio selectedOrigenMaderaAnio = (OrigenMaderaAnio) spnOrigenMaderaAnio.getSelectedItem();
        OrigenHacienda selectedOrigenHacienda = (OrigenHacienda) spnOrigenHacieda.getSelectedItem();
        String cosechaId = preferences.getString(Helper.CURRENT_COSECHA_ID_NAME, null);
        if (cosechaId == null){
            UUID uuid = UUID.randomUUID();
            cosechaId = uuid.toString();
        }
        Cosecha cosecha = new Cosecha();
        cosecha.id = cosechaId;
        cosecha.origenHaciendaId = selectedOrigenHacienda.id;
        cosecha.camionId = selectedCamion.id;
        cosecha.codigoPo = etCodigoPo.getText().toString();
        cosecha.destinoId = selectedDestino.id;
        cosecha.materialId = materialId;
        cosecha.aserradorId = selectedAserrador.id;
        cosecha.fechaTumba = etFechaTumba.getText().toString();
        cosecha.fechaDespacho = etFechaDespacho.getText().toString();
        cosecha.diasT2k = etDiasT2k.getText().toString();
        cosecha.guiaForestal = etGuiaForestal.getText().toString();
        cosecha.guiaRemision = etGuiaRemision.getText().toString();
        cosecha.tipoMaderaId = selectedTipoMadera.id;
        cosecha.formatoEntregaId = selectedFormatoEntrega.id;
        cosecha.origenMaderaId = selectedOrigenMadera.id;
        cosecha.origenMaderaAnioId = selectedOrigenMaderaAnio.id;
        Log.e( "origenMaderaAnioId: ", String.valueOf(selectedOrigenMaderaAnio.id));
        cosecha.estado = "P";
        cosecha.valorFlete = Double.parseDouble(etValorFlete.getText().toString());
        cosecha.tipoLlenado = selectedFormatoEntrega.id == 1?"B":(selectedFormatoEntrega.id == 2?"S":"T");
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
                    if(!isHistorial) guardar();
                    FormatoEntrega formatoEntrega = (FormatoEntrega) spnFormatoEntrega.getSelectedItem();
                    Intent intent;
                    if (formatoEntrega.id == 3) {
                        final SharedPreferences.Editor editor = getSharedPreferences(Helper.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).edit();
                        editor.putString(Helper.newPhotoPath, "");
                        editor.commit();
                        intent = new Intent(NuevaCosechaActivity.this, TrozaActivity.class);
                    }
                    else {
                        intent = new Intent(NuevaCosechaActivity.this, LlenadoCamionActivity.class);
                    }
                    startActivity(intent);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}