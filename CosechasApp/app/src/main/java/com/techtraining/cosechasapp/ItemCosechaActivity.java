package com.techtraining.cosechasapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.techtraining.cosechasapp.db.Espesor;
import com.techtraining.cosechasapp.db.ItemFilaCosecha;
import com.techtraining.cosechasapp.db.Largo;
import com.techtraining.cosechasapp.tasks.CargarDatosItemCosecha;
import com.techtraining.cosechasapp.tasks.GuardarItemFilaCosecha;

public class ItemCosechaActivity extends AppCompatActivity {
    public EditText etPlantilla;
    public EditText etBultos;
    public Spinner spnEspesor;
    public Spinner spnLargo;
    private AppCompatButton btnGuardar;
    private boolean formularioValido() {
        boolean valido = true;
        if (etBultos.getText().length() == 0) {
            etBultos.setError(getString(R.string.bultos_requeridos));
            valido = false;
        }
        if (etPlantilla.getText().length() == 0) {
            etPlantilla.setError(getString(R.string.plantilla_requerida));
            valido = false;
        }
        if (spnEspesor.getSelectedItem() == null) {
            Toast.makeText(this, getString(R.string.espesor_requerido), Toast.LENGTH_SHORT).show();
            valido = false;
        }
        if (spnLargo.getSelectedItem() == null) {
            Toast.makeText(this, getString(R.string.largo_requerido), Toast.LENGTH_SHORT).show();
            valido = false;
        }
        return valido;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_cosecha);
        etPlantilla = findViewById(R.id.etPlantilla);
        etBultos = findViewById(R.id.etBultos);
        spnEspesor = findViewById(R.id.spnEspesor);
        spnLargo = findViewById(R.id.spnLargo);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(formularioValido()) {
                    ItemFilaCosecha itemFilaCosecha = new ItemFilaCosecha();
                    itemFilaCosecha.id = getSharedPreferences(Helper.SHARED_PREFERENCES_NAME, MODE_PRIVATE).getString(Helper.CURRENT_ITEM_FILA_NAME, null);
                    itemFilaCosecha.plantilla = Double.parseDouble(etPlantilla.getText().toString());
                    itemFilaCosecha.bultos = Double.parseDouble(etBultos.getText().toString());
                    itemFilaCosecha.espesorId = ((Espesor) spnEspesor.getSelectedItem()).id;
                    itemFilaCosecha.largoId = ((Largo) spnLargo.getSelectedItem()).id;
                    new GuardarItemFilaCosecha(ItemCosechaActivity.this, itemFilaCosecha).execute();
                }
            }
        });
        new CargarDatosItemCosecha(this).execute();
    }
}