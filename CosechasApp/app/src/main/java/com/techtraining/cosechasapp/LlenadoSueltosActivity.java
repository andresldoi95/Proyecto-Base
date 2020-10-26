package com.techtraining.cosechasapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.techtraining.cosechasapp.db.FilaSuelto;
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
                SharedPreferences.Editor editor = getSharedPreferences(Helper.SHARED_PREFERENCES_NAME, MODE_PRIVATE).edit();
                editor.putString(Helper.CURRENT_ITEM_SUELTO_NAME, null);
                editor.commit();
                Intent intent = new Intent(LlenadoSueltosActivity.this, ItemSueltosActivity.class);
                intent.putExtra(Helper.INDICE_NAME, lvSueltos.getCount());
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}