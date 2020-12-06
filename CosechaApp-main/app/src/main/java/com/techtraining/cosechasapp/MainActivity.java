package com.techtraining.cosechasapp;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.techtraining.cosechasapp.notifications.AlarmReceiver;
import com.techtraining.cosechasapp.tasks.ImportarDatos;
import com.techtraining.cosechasapp.tasks.InsertUpdateUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    // private DrawerLayout drawerLayout;
    // private NavigationView navigationView;
    // private ActionBarDrawerToggle toggle;
    private AppCompatButton btnActualizar;
    private AppCompatButton btnNuevaCosecha;
    private AppCompatButton btnCosechasTerminadas;
    private AppCompatButton btnCerrarSesion;
    private AppCompatButton btnCosechasHistorial;
    private NotificationManager mNotificationManager;

    private void importar() {
        new ImportarDatos(MainActivity.this).execute();
    }
    private void logout() {
        SharedPreferences sharedPreferences = getSharedPreferences(Helper.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Helper.USER_TOKEN_NAME, null);
        editor.commit();
        Toast.makeText(MainActivity.this, R.string.logged_out, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
    private void nuevaCosecha() {
        final SharedPreferences.Editor editor = getSharedPreferences(Helper.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).edit();
        editor.putBoolean(Helper.IS_HISTORIAL, false);
        editor.commit();
        Intent intentNuevaCosecha = new Intent(MainActivity.this, NuevaCosechaActivity.class);
        startActivity(intentNuevaCosecha);
    }
    private void historialCosecha(){
        Intent intentNuevaCosecha = new Intent(MainActivity.this, HistorialCosechaActivity.class);
        startActivity(intentNuevaCosecha);
    }
    private void cosechasTerminadas() {
        Intent intentCosechasTerminadas = new Intent(MainActivity.this, CosechasTerminadasActivity.class);
        startActivity(intentCosechasTerminadas);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // drawerLayout=findViewById(R.id.drawer_layout);
        // navigationView=findViewById(R.id.nav_view);
        // toggle = new ActionBarDrawerToggle(this, drawerLayout,R.string.open, R.string.close);
        // drawerLayout.addDrawerListener(toggle);
        btnActualizar = findViewById(R.id.btnActualizar);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        btnCosechasTerminadas = findViewById(R.id.btnCosechasTerminadas);
        btnNuevaCosecha = findViewById(R.id.btnNuevaCosecha);
        btnCosechasHistorial = findViewById(R.id.btnCosechasHistorial);
        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                importar();
            }
        });
        btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
        btnCosechasTerminadas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cosechasTerminadas();
            }
        });
        btnNuevaCosecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nuevaCosecha();
            }
        });
        btnCosechasHistorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                historialCosecha();
            }
        });
        // toggle.syncState();
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /*navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                switch (id) {
                    case R.id.nav_logout:
                        logout();
                        break;
                    case R.id.nav_update_db:
                        importar();
                        break;
                    case R.id.nav_nueva_cosecha:
                        nuevaCosecha();
                        break;
                    case R.id.nav_cosechas_terminadas:
                        cosechasTerminadas();
                        break;
                }
                if(drawerLayout.isDrawerOpen(GravityCompat.START))
                    drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });*/
        final SharedPreferences preferences = getSharedPreferences(Helper.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
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
                        int currentId = response.getInt("id");
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putInt(Helper.CURRENT_ID, currentId);
                        editor.commit();
                        new InsertUpdateUser(getApplicationContext(), currentId, nombre, response.getString("email")).execute();
                        String primeraVez = preferences.getString(Helper.FIRST_TIME_NAME, "N");
                        if (primeraVez.equals("N")) {
                            importar();
                            editor.putString(Helper.FIRST_TIME_NAME, "S");
                            editor.commit();
                        }
                    }
                    catch (JSONException ex) {
                        Log.e(MainActivity.class.getName(), ex.getMessage());
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
        handleNotifications();
    }

    private void handleNotifications(){
        mNotificationManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);
        // Set up the Notification Broadcast Intent.
        Intent notifyIntent = new Intent(this, AlarmReceiver.class);
        final PendingIntent notifyPendingIntent = PendingIntent.getBroadcast
                (this, Helper.NOTIFICATION_ID, notifyIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
        final AlarmManager alarmManager = (AlarmManager) getSystemService
                (ALARM_SERVICE);
        long repeatInterval = Helper.NOTIFICATION_TIMER;
        long triggerTime = SystemClock.elapsedRealtime()
                + repeatInterval;
        if (alarmManager != null) {
            alarmManager.setInexactRepeating
                    (AlarmManager.ELAPSED_REALTIME_WAKEUP,
                            triggerTime, repeatInterval,
                            notifyPendingIntent);
        }
        createNotificationChannel();
    }

    /**
     * Creates a Notification channel, for OREO and higher.
     */
    public void createNotificationChannel() {
        mNotificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >=
                android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel
                    (Helper.PRIMARY_CHANNEL_ID,
                            getString(R.string.notification_channel_description),
                            NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription(getString(R.string.notification_channel_description));
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
    }

    @Override
    public void onBackPressed(){
        /*if(drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else
            finish();*/
        finish();
    }
}