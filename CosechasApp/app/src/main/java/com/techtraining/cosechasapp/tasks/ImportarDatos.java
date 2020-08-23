package com.techtraining.cosechasapp.tasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.techtraining.cosechasapp.Helper;
import com.techtraining.cosechasapp.NetworkManager;
import com.techtraining.cosechasapp.R;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

public class ImportarDatos extends AsyncTask<Void, Void, Void> {
    private Context context;
    private NetworkManager networkManager;
    private SharedPreferences sharedPreferences;
    public ImportarDatos(Context context) {
        this.context = context;
        networkManager = NetworkManager.getInstance(context);
    }

    @Override
    protected void onPreExecute() {
        Toast.makeText(context, R.string.importando_datos, Toast.LENGTH_SHORT).show();
    }

    private void importarEmpresas() {
        String url = Helper.URL_API + "/empresas/all";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                new ImportarEmpresas(context, response).execute();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String mensaje = error.getMessage();
                if (mensaje != null)
                    Log.e(ImportarDatos.class.getName(), mensaje);
                else
                    Log.e(ImportarDatos.class.getName(), context.getString(R.string.error_generico));
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(Helper.AUTH_HEADER, Helper.AUTH_TYPE + sharedPreferences.getString(Helper.USER_TOKEN_NAME, null));
                return params;
            }
        };
        networkManager.addToRequestQueue(jsonArrayRequest);
    }
    private void importarCamiones() {
        String url = Helper.URL_API + "/camiones/all";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                new ImportarCamiones(context, response).execute();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String mensaje = error.getMessage();
                if (mensaje != null)
                    Log.e(ImportarDatos.class.getName(), mensaje);
                else
                    Log.e(ImportarDatos.class.getName(), context.getString(R.string.error_generico));
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(Helper.AUTH_HEADER, Helper.AUTH_TYPE + sharedPreferences.getString(Helper.USER_TOKEN_NAME, null));
                return params;
            }
        };
        networkManager.addToRequestQueue(jsonArrayRequest);
    }
    private void importarControladores() {
        String url = Helper.URL_API + "/controladores/all";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                new ImportarControladores(context, response).execute();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String mensaje = error.getMessage();
                if (mensaje != null)
                    Log.e(ImportarDatos.class.getName(), mensaje);
                else
                    Log.e(ImportarDatos.class.getName(), context.getString(R.string.error_generico));
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(Helper.AUTH_HEADER, Helper.AUTH_TYPE + sharedPreferences.getString(Helper.USER_TOKEN_NAME, null));
                return params;
            }
        };
        networkManager.addToRequestQueue(jsonArrayRequest);
    }
    private void importarProcedencias() {
        String url = Helper.URL_API + "/procedencias/all";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                new ImportarProcedencias(context, response).execute();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String mensaje = error.getMessage();
                if (mensaje != null)
                    Log.e(ImportarDatos.class.getName(), mensaje);
                else
                    Log.e(ImportarDatos.class.getName(), context.getString(R.string.error_generico));
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(Helper.AUTH_HEADER, Helper.AUTH_TYPE + sharedPreferences.getString(Helper.USER_TOKEN_NAME, null));
                return params;
            }
        };
        networkManager.addToRequestQueue(jsonArrayRequest);
    }
    @Override
    protected Void doInBackground(Void... voids) {
        sharedPreferences = context.getSharedPreferences(Helper.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        importarEmpresas();
        importarControladores();
        importarProcedencias();
        importarCamiones();
        return null;
    }
}
