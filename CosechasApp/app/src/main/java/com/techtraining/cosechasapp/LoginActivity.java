package com.techtraining.cosechasapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    private EditText etUsername;
    private EditText etPassword;
    private AppCompatButton btnLogin;
    private boolean formularioValido() {
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();
        boolean valido = true;
        if (username.length() == 0)
        {
            valido = false;
            etUsername.setError(getString(R.string.username_required));
        }
        if (password.length() == 0) {
            valido = false;
            etPassword.setError(getString(R.string.password_required));
        }
        return valido;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (formularioValido()) {
                    String url = Helper.URL_OAUTH2;
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("client_id", Helper.CLIENT_ID);
                        jsonObject.put("grant_type", Helper.GRANT_TYPE);
                        jsonObject.put("client_secret", Helper.CLIENT_SECRET);
                        jsonObject.put("username", etUsername.getText().toString());
                        jsonObject.put("password", etPassword.getText().toString());
                    }
                    catch (JSONException ex) {
                        Log.e(LoginActivity.class.getName(), ex.getMessage());
                    }
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                            url, jsonObject, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String token = response.getString("access_token");
                                SharedPreferences sharedPreferences = getSharedPreferences(Helper.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(Helper.USER_TOKEN_NAME, token);
                                editor.commit();
                                Toast.makeText(LoginActivity.this, R.string.acceso_exitoso, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            catch (JSONException ex) {
                                Toast.makeText(LoginActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            String errorMessage = error.getMessage();
                            if (errorMessage != null)
                                Log.e(LoginActivity.class.getName(), errorMessage);
                            NetworkResponse networkResponse = error.networkResponse;
                            if (networkResponse != null) {
                                Toast.makeText(LoginActivity.this, R.string.invalid_credentials, Toast.LENGTH_SHORT).show();
                            }
                            else if (errorMessage != null)
                                Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(LoginActivity.this, R.string.error_generico, Toast.LENGTH_SHORT).show();
                        }
                    });
                    NetworkManager.getInstance(LoginActivity.this).addToRequestQueue(jsonObjectRequest);
                }
            }
        });
    }
}