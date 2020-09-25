package com.techtraining.cosechasapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import com.techtraining.cosechasapp.tasks.CargarCosechasTerminadas;

public class CosechasTerminadasActivity extends AppCompatActivity {
    public ListView lvCosechas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cosechas_terminadas);
        lvCosechas = findViewById(R.id.lvCosechas);
        setTitle(R.string.cosechas_terminadas);
        new CargarCosechasTerminadas(this).execute();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.cosechas_terminadas_menu, menu);
        return true;
    }
    private void exportar() {

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_exportar:
                exportar();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}