package com.techtraining.cosechasapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import com.techtraining.cosechasapp.tasks.CargarFilasCamion;
import com.techtraining.cosechasapp.tasks.FinalizarDespacho;

public class LlenadoCamionActivity extends AppCompatActivity {
    public ListView lvFilas;
    private boolean isHistorial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isHistorial = getSharedPreferences(Helper.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).getBoolean(Helper.IS_HISTORIAL, false);
        setContentView(R.layout.activity_llenado_camion);
        setTitle(R.string.llenado_camion);
        lvFilas = findViewById(R.id.lvFilas);
    }
    public void cargarDatos() {
        new CargarFilasCamion(LlenadoCamionActivity.this).execute();
    }
    @Override
    protected void onResume() {
        cargarDatos();
        isHistorial = getSharedPreferences(Helper.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).getBoolean(Helper.IS_HISTORIAL, false);
        super.onResume();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_llenado, menu);

        if(isHistorial){
            for (int i = 0; i < menu.size(); i++)
                menu.getItem(i).setVisible(false);
        }
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
                                new FinalizarDespacho(LlenadoCamionActivity.this).execute();
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