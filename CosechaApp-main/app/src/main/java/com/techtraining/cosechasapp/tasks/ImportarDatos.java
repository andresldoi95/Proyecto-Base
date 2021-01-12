package com.techtraining.cosechasapp.tasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
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
                    error.printStackTrace();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(Helper.AUTH_HEADER, Helper.AUTH_TYPE + sharedPreferences.getString(Helper.USER_TOKEN_NAME, null));
                return params;
            }
        };
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
            Helper.DEFAULT_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
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
                    error.printStackTrace();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(Helper.AUTH_HEADER, Helper.AUTH_TYPE + sharedPreferences.getString(Helper.USER_TOKEN_NAME, null));
                return params;
            }
        };
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                Helper.DEFAULT_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
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
                    error.printStackTrace();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(Helper.AUTH_HEADER, Helper.AUTH_TYPE + sharedPreferences.getString(Helper.USER_TOKEN_NAME, null));
                return params;
            }
        };
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                Helper.DEFAULT_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
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
                    error.printStackTrace();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(Helper.AUTH_HEADER, Helper.AUTH_TYPE + sharedPreferences.getString(Helper.USER_TOKEN_NAME, null));
                return params;
            }
        };
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                Helper.DEFAULT_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        networkManager.addToRequestQueue(jsonArrayRequest);
    }
    private void importarAserradores() {
        String url = Helper.URL_API + "/aserradores/all";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                new ImportarAserradores(context, response).execute();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String mensaje = error.getMessage();
                if (mensaje != null)
                    Log.e(ImportarDatos.class.getName(), mensaje);
                else
                    error.printStackTrace();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(Helper.AUTH_HEADER, Helper.AUTH_TYPE + sharedPreferences.getString(Helper.USER_TOKEN_NAME, null));
                return params;
            }
        };
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                Helper.DEFAULT_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        networkManager.addToRequestQueue(jsonArrayRequest);
    }
    private void importarDestinos() {
        String url = Helper.URL_API + "/destinos/all";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                new ImportarDestinos(context, response).execute();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String mensaje = error.getMessage();
                if (mensaje != null)
                    Log.e(ImportarDatos.class.getName(), mensaje);
                else
                    error.printStackTrace();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(Helper.AUTH_HEADER, Helper.AUTH_TYPE + sharedPreferences.getString(Helper.USER_TOKEN_NAME, null));
                return params;
            }
        };
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                Helper.DEFAULT_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        networkManager.addToRequestQueue(jsonArrayRequest);
    }
    private void importarEspesores() {
        String url = Helper.URL_API + "/espesores/all";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                new ImportarEspesores(context, response).execute();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String mensaje = error.getMessage();
                if (mensaje != null)
                    Log.e(ImportarDatos.class.getName(), mensaje);
                else
                    error.printStackTrace();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(Helper.AUTH_HEADER, Helper.AUTH_TYPE + sharedPreferences.getString(Helper.USER_TOKEN_NAME, null));
                return params;
            }
        };
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                Helper.DEFAULT_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        networkManager.addToRequestQueue(jsonArrayRequest);
    }
    private void importarLargos() {
        String url = Helper.URL_API + "/largos/all";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                new ImportarLargos(context, response).execute();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String mensaje = error.getMessage();
                if (mensaje != null)
                    Log.e(ImportarDatos.class.getName(), mensaje);
                else
                    error.printStackTrace();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(Helper.AUTH_HEADER, Helper.AUTH_TYPE + sharedPreferences.getString(Helper.USER_TOKEN_NAME, null));
                return params;
            }
        };
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                Helper.DEFAULT_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        networkManager.addToRequestQueue(jsonArrayRequest);
    }
    private void importarMateriales() {
        String url = Helper.URL_API + "/materiales/all";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                new ImportarMateriales(context, response).execute();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String mensaje = error.getMessage();
                if (mensaje != null)
                    Log.e(ImportarDatos.class.getName(), mensaje);
                else
                    error.printStackTrace();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(Helper.AUTH_HEADER, Helper.AUTH_TYPE + sharedPreferences.getString(Helper.USER_TOKEN_NAME, null));
                return params;
            }
        };
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                Helper.DEFAULT_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        networkManager.addToRequestQueue(jsonArrayRequest);
    }
    private void importarCodigosAserradores() {
        String url = Helper.URL_API + "/codigos-aserradores/all";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                new ImportarCodigosAserradores(context, response).execute();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String mensaje = error.getMessage();
                if (mensaje != null)
                    Log.e(ImportarDatos.class.getName(), mensaje);
                else
                    error.printStackTrace();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(Helper.AUTH_HEADER, Helper.AUTH_TYPE + sharedPreferences.getString(Helper.USER_TOKEN_NAME, null));
                return params;
            }
        };
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                Helper.DEFAULT_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        networkManager.addToRequestQueue(jsonArrayRequest);
    }
    private void importarParametros() {
        String url = Helper.URL_API + "/parametros/all";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                new ImportarParametros(context, response).execute();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String mensaje = error.getMessage();
                if (mensaje != null)
                    Log.e(ImportarDatos.class.getName(), mensaje);
                else
                    error.printStackTrace();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(Helper.AUTH_HEADER, Helper.AUTH_TYPE + sharedPreferences.getString(Helper.USER_TOKEN_NAME, null));
                return params;
            }
        };
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                Helper.DEFAULT_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        networkManager.addToRequestQueue(jsonArrayRequest);
    }
    private void importarTiposMadera() {
        String url = Helper.URL_API + "/tipos-madera/all";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                new ImportarTiposMadera(context, response).execute();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String mensaje = error.getMessage();
                if (mensaje != null)
                    Log.e(ImportarDatos.class.getName(), mensaje);
                else
                    error.printStackTrace();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(Helper.AUTH_HEADER, Helper.AUTH_TYPE + sharedPreferences.getString(Helper.USER_TOKEN_NAME, null));
                return params;
            }
        };
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                Helper.DEFAULT_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        networkManager.addToRequestQueue(jsonArrayRequest);
    }
    private void importarFormatosEntrega() {
        String url = Helper.URL_API + "/formatos-entrega/all";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                new ImportarFormatosEntrega(context, response).execute();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String mensaje = error.getMessage();
                if (mensaje != null)
                    Log.e(ImportarDatos.class.getName(), mensaje);
                else
                    error.printStackTrace();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(Helper.AUTH_HEADER, Helper.AUTH_TYPE + sharedPreferences.getString(Helper.USER_TOKEN_NAME, null));
                return params;
            }
        };
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                Helper.DEFAULT_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        networkManager.addToRequestQueue(jsonArrayRequest);
    }
    private void importarTiposBulto() {
        String url = Helper.URL_API + "/tipos-bulto/all";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                new ImportarTiposBulto(context, response).execute();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String mensaje = error.getMessage();
                if (mensaje != null)
                    Log.e(ImportarDatos.class.getName(), mensaje);
                else
                    error.printStackTrace();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(Helper.AUTH_HEADER, Helper.AUTH_TYPE + sharedPreferences.getString(Helper.USER_TOKEN_NAME, null));
                return params;
            }
        };
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                Helper.DEFAULT_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        networkManager.addToRequestQueue(jsonArrayRequest);
    }
    private void importarOrigenesMaderaAnios() {
        String url = Helper.URL_API + "/origenes-madera-anios/all";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                new ImportarOrigenesMaderaAnios(context, response).execute();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String mensaje = error.getMessage();
                if (mensaje != null)
                    Log.e(ImportarDatos.class.getName(), mensaje);
                else
                    error.printStackTrace();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(Helper.AUTH_HEADER, Helper.AUTH_TYPE + sharedPreferences.getString(Helper.USER_TOKEN_NAME, null));
                return params;
            }
        };
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                Helper.DEFAULT_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        networkManager.addToRequestQueue(jsonArrayRequest);
    }
    private void importarOrigenesMadera() {
        String url = Helper.URL_API + "/origenes-madera/all";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                new ImportarOrigenesMadera(context, response).execute();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String mensaje = error.getMessage();
                if (mensaje != null)
                    Log.e(ImportarDatos.class.getName(), mensaje);
                else
                    error.printStackTrace();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(Helper.AUTH_HEADER, Helper.AUTH_TYPE + sharedPreferences.getString(Helper.USER_TOKEN_NAME, null));
                return params;
            }
        };
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                Helper.DEFAULT_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        networkManager.addToRequestQueue(jsonArrayRequest);
    }
    private void importarTarifas() {
        String url = Helper.URL_API + "/tarifas/all";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                new ImportarTarifas(context, response).execute();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String mensaje = error.getMessage();
                if (mensaje != null)
                    Log.e(ImportarDatos.class.getName(), mensaje);
                else
                    error.printStackTrace();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(Helper.AUTH_HEADER, Helper.AUTH_TYPE + sharedPreferences.getString(Helper.USER_TOKEN_NAME, null));
                return params;
            }
        };
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                Helper.DEFAULT_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        networkManager.addToRequestQueue(jsonArrayRequest);
    }
    private void importarOrigenesHacienda() {
        String url = Helper.URL_API + "/origenes-hacienda/all";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                new ImportarOrigenesHacienda(context, response).execute();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String mensaje = error.getMessage();
                if (mensaje != null)
                    Log.e(ImportarDatos.class.getName(), mensaje);
                else
                    error.printStackTrace();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(Helper.AUTH_HEADER, Helper.AUTH_TYPE + sharedPreferences.getString(Helper.USER_TOKEN_NAME, null));
                return params;
            }
        };
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                Helper.DEFAULT_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        networkManager.addToRequestQueue(jsonArrayRequest);
    }
    @Override
    protected Void doInBackground(Void... voids) {
        sharedPreferences = context.getSharedPreferences(Helper.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        importarEmpresas();
        importarAserradores();
        importarDestinos();
        importarEspesores();
        importarLargos();
        importarMateriales();
        importarCodigosAserradores();
        importarTiposMadera();
        importarFormatosEntrega();
        importarOrigenesMadera();
        importarOrigenesMaderaAnios();
        importarTiposBulto();
        importarTarifas();
        importarOrigenesHacienda();
        importarCamiones();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Toast.makeText(context, R.string.importacion_finalizada, Toast.LENGTH_SHORT).show();
    }
}
