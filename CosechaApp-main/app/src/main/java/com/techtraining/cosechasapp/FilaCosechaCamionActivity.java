package com.techtraining.cosechasapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.GridView;
import com.techtraining.cosechasapp.tasks.GenerarMatrizCosecha;

public class FilaCosechaCamionActivity extends AppCompatActivity {
    public GridView grvMatriz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fila_cosecha_camion);
        grvMatriz = findViewById(R.id.grvMatriz);
        new GenerarMatrizCosecha(this   ).execute();
    }
}