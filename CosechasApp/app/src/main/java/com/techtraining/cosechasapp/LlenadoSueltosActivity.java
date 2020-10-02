package com.techtraining.cosechasapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import com.techtraining.cosechasapp.tasks.CargarSueltos;

public class LlenadoSueltosActivity extends AppCompatActivity {
    public ListView lvSueltos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_llenado_sueltos);
        lvSueltos = findViewById(R.id.lvSueltos);
    }

    @Override
    protected void onResume() {
        new CargarSueltos(this).execute();
        super.onResume();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sueltos_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_nuevo:
                Intent intent = new Intent(LlenadoSueltosActivity.this, ItemCosechaActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}