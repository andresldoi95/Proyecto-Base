package com.techtraining.cosechasapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.techtraining.cosechasapp.tasks.CargarFilasCamion;

public class LlenadoCamion extends AppCompatActivity {
    public ListView lvFilas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_llenado_camion);
        setTitle(R.string.llenado_camion);
        lvFilas = findViewById(R.id.lvFilas);

    }

    @Override
    protected void onResume() {
        new CargarFilasCamion(LlenadoCamion.this).execute();
        super.onResume();
    }
}