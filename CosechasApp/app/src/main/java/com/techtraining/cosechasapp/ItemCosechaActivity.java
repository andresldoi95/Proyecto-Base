package com.techtraining.cosechasapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class ItemCosechaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_cosecha);
        setTitle(R.string.datos_bloque);
    }
}