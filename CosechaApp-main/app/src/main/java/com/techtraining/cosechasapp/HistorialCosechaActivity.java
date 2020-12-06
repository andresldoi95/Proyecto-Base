package com.techtraining.cosechasapp;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.techtraining.cosechasapp.tasks.CargarCosechasHistorial;
import com.techtraining.cosechasapp.tasks.CargarCosechasTerminadas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ListView;

public class HistorialCosechaActivity extends AppCompatActivity {

    public ListView lvCosechas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_cosecha);
        lvCosechas = findViewById(R.id.lvCosechas);
        new CargarCosechasHistorial(this).execute();
    }
}