package com.techtraining.cosechasapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private Menu menu;
    private ProgressBar spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);
        toggle = new ActionBarDrawerToggle(this, drawerLayout,R.string.open, R.string.close);
        spinner = findViewById(R.id.progressBar);
        spinner.setVisibility(View.INVISIBLE);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                switch (id) {
                    case R.id.nav_logout:
                        SharedPreferences sharedPreferences = getSharedPreferences(Helper.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(Helper.USER_TOKEN_NAME, null);
                        editor.commit();
                        Toast.makeText(MainActivity.this, R.string.logged_out, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.nav_update_db:
                        spinner.setVisibility(View.VISIBLE);
                        break;
                }
                if(drawerLayout.isDrawerOpen(GravityCompat.START))
                    drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        SharedPreferences preferences = getSharedPreferences(Helper.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        final String token = preferences.getString(Helper.USER_TOKEN_NAME, null);
        if (token == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
        else {
            String url = Helper.URL_API + "/usuario/datos";
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                    url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        String nombre = response.getString("name");
                        Toast.makeText(MainActivity.this, getString(R.string.welcome) + " " + nombre, Toast.LENGTH_SHORT).show();
                    }
                    catch (JSONException ex) {
                        Toast.makeText(MainActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    String errorMessage = error.getMessage();
                    if (errorMessage != null)
                        Log.e(LoginActivity.class.getName(), errorMessage);
                    if (errorMessage != null)
                        Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(MainActivity.this, R.string.error_generico, Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put(Helper.AUTH_HEADER, Helper.AUTH_TYPE + token);
                    return params;
                }
            };
            NetworkManager.getInstance(MainActivity.this).addToRequestQueue(jsonObjectRequest);
        }
    }
    @Override
    public void onBackPressed(){
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(toggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }
}