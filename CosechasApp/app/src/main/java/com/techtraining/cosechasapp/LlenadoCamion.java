package com.techtraining.cosechasapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import com.techtraining.cosechasapp.tasks.CargarFilasCamion;
import com.techtraining.cosechasapp.tasks.EliminarImagenes;
import com.techtraining.cosechasapp.tasks.FinalizarDespacho;

public class LlenadoCamion extends AppCompatActivity {
    public ListView lvFilas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_llenado_camion);
        setTitle(R.string.llenado_camion);
        lvFilas = findViewById(R.id.lvFilas);
    }
    public void cargarDatos() {
        new CargarFilasCamion(LlenadoCamion.this).execute();
    }
    @Override
    protected void onResume() {
        cargarDatos();
        super.onResume();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_llenado, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_finalizar:
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                new FinalizarDespacho(LlenadoCamion.this).execute();
                                break;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(getString(R.string.desea_finalizar_despacho)).setPositiveButton(getString(R.string.si), dialogClickListener)
                        .setNegativeButton(getString(R.string.no), dialogClickListener).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}