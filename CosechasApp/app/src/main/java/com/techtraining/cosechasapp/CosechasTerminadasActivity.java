package com.techtraining.cosechasapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.techtraining.cosechasapp.adapters.CosechasTerminadasAdapter;
import com.techtraining.cosechasapp.db.Cosecha;
import com.techtraining.cosechasapp.tasks.CargarCosechasTerminadas;
import com.techtraining.cosechasapp.tasks.ExportarDespachos;
import com.techtraining.cosechasapp.tasks.FinalizarDespacho;

import java.util.ArrayList;

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
        CosechasTerminadasAdapter cosechasTerminadasAdapter = (CosechasTerminadasAdapter) lvCosechas.getAdapter();
        final ArrayList<Cosecha> cosechas = new ArrayList<>();
        for (int i = 0; i < cosechasTerminadasAdapter.getCount(); i++) {
            Cosecha cosecha = (Cosecha) cosechasTerminadasAdapter.getItem(i);
            if (cosecha.seleccionado) {
                cosechas.add(cosecha);
            }
        }
        if (cosechas.size() > 0) {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            for (int i = 0; i < cosechas.size(); i++) {
                                new ExportarDespachos(CosechasTerminadasActivity.this, cosechas.get(i)).execute();
                            }
                            Toast.makeText(CosechasTerminadasActivity.this, R.string.despachos_exportados, Toast.LENGTH_SHORT).show();
                            finish();
                            break;
                    }
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(getString(R.string.desea_exportar_despachos)).setPositiveButton(getString(R.string.si), dialogClickListener)
                    .setNegativeButton(getString(R.string.no), dialogClickListener).show();
        }
        else {
            Toast.makeText(this, R.string.debe_seleccionar_despachos, Toast.LENGTH_SHORT).show();
        }
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