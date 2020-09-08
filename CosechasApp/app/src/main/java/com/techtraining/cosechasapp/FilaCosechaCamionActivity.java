package com.techtraining.cosechasapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.techtraining.cosechasapp.adapters.FilaCosechaCamionAdapter;
import com.techtraining.cosechasapp.db.ItemFilaCosecha;

import java.util.ArrayList;
import java.util.List;

public class FilaCosechaCamionActivity extends AppCompatActivity {
    public static final String FILAS_NAME = "filas";
    public static final String COLUMNAS_NAME = "columnas";
    public static final String FILA_NAME = "fila";
    public static int filas = -1;
    public static int columnas = - 1;
    public static int fila = -1;
    private List<ItemFilaCosecha> matriz;
    public GridView grvMatriz;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fila_cosecha_camion);
        grvMatriz = findViewById(R.id.grvMatriz);
        matriz = new ArrayList<>();
        Intent intent = getIntent();
        if (fila == -1) {
            filas = intent.getIntExtra(FILAS_NAME, 0);
            columnas = intent.getIntExtra(COLUMNAS_NAME, 0);
            fila = intent.getIntExtra(FILA_NAME, 0);
        }
        setTitle(getString(R.string.fila) + " " + String.valueOf(fila));
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                ItemFilaCosecha item = new ItemFilaCosecha();
                item.bft = 0;
                item.bultos = 0;
                item.columna = j + 1;
                item.fila = i + 1;
                matriz.add(item);
            }
        }
        grvMatriz.setNumColumns(columnas);
        grvMatriz.setAdapter(new FilaCosechaCamionAdapter(getApplicationContext(), matriz));
        grvMatriz.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(FilaCosechaCamionActivity.this, ItemCosechaActivity.class);
                startActivity(intent);
            }
        });

    }
}