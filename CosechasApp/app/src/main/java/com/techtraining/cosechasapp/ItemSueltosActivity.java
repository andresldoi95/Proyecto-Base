package com.techtraining.cosechasapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.techtraining.cosechasapp.db.Espesor;
import com.techtraining.cosechasapp.db.FilaSuelto;
import com.techtraining.cosechasapp.db.Largo;
import com.techtraining.cosechasapp.tasks.CargarDatosSueltos;
import com.techtraining.cosechasapp.tasks.GuardarSueltos;

public class ItemSueltosActivity extends AppCompatActivity {
    public Spinner spnLargo;
    public Spinner spnEspesor;
    public EditText etPlantilla;
    private AppCompatButton btnGuardar;
    public int indice;
    private boolean formularioValido() {
        if (spnLargo.getSelectedItem() == null) {
            Toast.makeText(this, R.string.largo_requerido, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (spnEspesor.getSelectedItem() == null) {
            Toast.makeText(this, R.string.espesor_requerido, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (etPlantilla.getText().length() == 0) {
            Toast.makeText(this, R.string.plantilla_requerida, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_sueltos);
        indice = getIntent().getIntExtra(Helper.INDICE_NAME, -1);
        spnLargo = findViewById(R.id.spnLargo);
        spnEspesor = findViewById(R.id.spnEspesor);
        etPlantilla = findViewById(R.id.etPlantilla);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (formularioValido()) {
                    FilaSuelto filaSuelto = new FilaSuelto();
                    filaSuelto.bultos = Double.parseDouble(etPlantilla.getText().toString());
                    Largo largo = (Largo) spnLargo.getSelectedItem();
                    Espesor espesor = (Espesor) spnEspesor.getSelectedItem();
                    filaSuelto.indice = indice;
                    filaSuelto.largoId = largo.id;
                    filaSuelto.espesorId = espesor.id;
                    filaSuelto.filaId = getSharedPreferences(Helper.SHARED_PREFERENCES_NAME, MODE_PRIVATE).getString(Helper.CURRENT_FILA_NAME, null);
                    new GuardarSueltos(ItemSueltosActivity.this, filaSuelto).execute();
                }
            }
        });
        new CargarDatosSueltos(this).execute();
    }
}