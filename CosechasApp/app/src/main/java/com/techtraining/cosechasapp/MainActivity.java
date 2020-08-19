package com.techtraining.cosechasapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
}