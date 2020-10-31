package com.techtraining.cosechasapp.tasks;

import android.content.Context;
import android.content.Intent;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.google.gson.Gson;
import com.techtraining.cosechasapp.DBManager;
import com.techtraining.cosechasapp.Helper;
import com.techtraining.cosechasapp.LlenadoCamion;
import com.techtraining.cosechasapp.MainActivity;
import com.techtraining.cosechasapp.NetworkManager;
import com.techtraining.cosechasapp.R;
import com.techtraining.cosechasapp.db.AppDatabase;
import com.techtraining.cosechasapp.db.Cosecha;
import com.techtraining.cosechasapp.db.CosechaDao;
import com.techtraining.cosechasapp.db.FilaCosecha;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ExportarDespachos extends AsyncTask<Void, Void, Void> {
    private Context context;
    private AppDatabase appDatabase;
    private Cosecha cosecha;
    private NetworkManager networkManager;
    public ExportarDespachos(Context context, Cosecha cosecha) {
        this.context = context;
        this.cosecha = cosecha;
        networkManager = NetworkManager.getInstance(context);
    }
    @Override
    protected Void doInBackground(Void... voids) {
        appDatabase = DBManager.getInstance(context);
        final CosechaDao cosechaDao = appDatabase.cosechaDao();
        if (cosecha != null) {
            cosecha.filas = appDatabase.filaCosechaDao().loadByCosecha(cosecha.id);
            for (int i = 0; i < cosecha.filas.size(); i++) {
                FilaCosecha fila = cosecha.filas.get(i);
                fila.sueltos = appDatabase.filaSueltoDao().loadByFila(fila.id);
            }
            String url = Helper.URL_API + "/despachos";
            Gson gson = new Gson();
            String json = gson.toJson(cosecha);
            try {
                JSONObject request = new JSONObject(json);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        cosecha.estado = "E";
                        cosechaDao.update(cosecha);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String mensaje = error.getMessage();
                        if (mensaje != null)
                            Log.e(ExportarDespachos.class.getName(), mensaje);
                        else
                            error.printStackTrace();
                    }
                }){
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put(Helper.AUTH_HEADER, Helper.AUTH_TYPE + context.getSharedPreferences(Helper.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).getString(Helper.USER_TOKEN_NAME, null));
                        return params;
                    }
                };
                jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                        Helper.DEFAULT_TIMEOUT,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                ));
                networkManager.addToRequestQueue(jsonObjectRequest);
            }
            catch (JSONException ex) {
                Log.e(ExportarDespachos.class.getName(), ex.getMessage());
            }

        }
        return null;
    }
}
