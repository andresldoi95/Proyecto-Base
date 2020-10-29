package com.techtraining.cosechasapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.techtraining.cosechasapp.db.Espesor;
import com.techtraining.cosechasapp.db.FilaCosecha;
import com.techtraining.cosechasapp.db.ItemFilaCosecha;
import com.techtraining.cosechasapp.db.Largo;
import com.techtraining.cosechasapp.db.TipoBulto;
import com.techtraining.cosechasapp.tasks.CargarDatosItemCosecha;
import com.techtraining.cosechasapp.tasks.GuardarItemFilaCosecha;

public class ItemCosechaActivity extends AppCompatActivity {
    public EditText etPlantilla;
    public Spinner spnTipoBulto;
    private AppCompatButton btnGuardar;
    private boolean formularioValido() {
        boolean valido = true;
        if (etPlantilla.getText().length() == 0) {
            etPlantilla.setError(getString(R.string.bultos_requeridos));
            valido = false;
        }
        if (spnTipoBulto.getSelectedItem() == null) {
            Toast.makeText(this, getString(R.string.tipo_bulto_requerido), Toast.LENGTH_SHORT).show();
            valido = false;
        }
        return valido;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_cosecha);
        etPlantilla = findViewById(R.id.etPlantilla);
        spnTipoBulto = findViewById(R.id.spnTipoBulto);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(formularioValido()) {
                    FilaCosecha filaCosecha = new FilaCosecha();
                    filaCosecha.id = getSharedPreferences(Helper.SHARED_PREFERENCES_NAME, MODE_PRIVATE).getString(Helper.CURRENT_FILA_NAME, null);
                    filaCosecha.bultos = Integer.parseInt(etPlantilla.getText().toString());
                    filaCosecha.tipoBultoId = ((TipoBulto) spnTipoBulto.getSelectedItem()).id;
                    new GuardarItemFilaCosecha(ItemCosechaActivity.this, filaCosecha).execute();
                }
            }
        });
        new CargarDatosItemCosecha(this).execute();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}