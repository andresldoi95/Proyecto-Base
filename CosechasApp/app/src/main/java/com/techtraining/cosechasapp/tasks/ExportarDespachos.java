package com.techtraining.cosechasapp.tasks;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Base64;
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
import org.json.JSONTokener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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
                fila.fotos = appDatabase.imagenFilaDao().loadByFila(fila.id);
            }
            cosecha.troza = appDatabase.trozaDao().loadByCosecha(cosecha.id);
            String url = Helper.URL_API + "/despachos";
            Gson gson = new Gson();
            String json = gson.toJson(cosecha);


            try {
                JSONObject request = new JSONObject(json);
                JSONObject request_new = new JSONObject();
                Log.e( "request: ", request.toString());

                //FOTOS TROZAS
                try {
                    JSONObject jsonObjectTroza = request.getJSONObject("troza");
                    if(!jsonObjectTroza.getString("foto").equals("NULL") && !jsonObjectTroza.getString("foto").equals("")){
                        Bitmap bitmap = null;
                        BitmapFactory.Options options = null;
                        options = new BitmapFactory.Options();
                        bitmap = BitmapFactory.decodeFile(jsonObjectTroza.getString("foto"), options);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byte[] byte_arr = stream.toByteArray();
                        String encodedImage = Base64.encodeToString(byte_arr, 0);
                        jsonObjectTroza.put("foto",encodedImage);
                        request.put("troza",jsonObjectTroza);
                    }
                } catch (Exception e) {
                    Log.e( "Exception FOTO TROZA", e.toString());
                }
                //FOTOS FILAS
                try {

                    JSONArray jsonArrayFila = request.getJSONArray("filas");

                    JSONObject jsonObjectFila_new = new JSONObject();



                    for(int i=0; i<jsonArrayFila.length(); i++){
                        JSONObject jsonObjectFila = jsonArrayFila.getJSONObject(i);


                        JSONArray jsonArrayFilaFotos = jsonObjectFila.getJSONArray("fotos");
                        JSONObject jsonObjectFilaFotos_new = new JSONObject();



                        for(int i2=0; i2<jsonArrayFilaFotos.length(); i2++){
                            JSONObject jsonObjectFilaFotos = jsonArrayFilaFotos.getJSONObject(i2);

                            if(!jsonObjectFilaFotos.getString("path").equals("NULL") && !jsonObjectFilaFotos.getString("path").equals("")){
                                Bitmap bitmap = null;
                                BitmapFactory.Options options = null;
                                options = new BitmapFactory.Options();
                                bitmap = BitmapFactory.decodeFile(jsonObjectFilaFotos.getString("path"), options);
                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                                byte[] byte_arr = stream.toByteArray();
                                String encodedImage = Base64.encodeToString(byte_arr, 0);

                                //Se actualiza el path
                                jsonObjectFilaFotos.put("path",encodedImage);

                                //Se actualiza Fila Fotos

                            }


                            jsonObjectFilaFotos_new.put(String.valueOf(i2),jsonObjectFilaFotos);


                        }

                        jsonObjectFila.put("fotos",jsonObjectFilaFotos_new);



                        jsonObjectFila_new.put(String.valueOf(i),jsonObjectFila);

                    }

                    Log.e( "jsonObjectFila_new:", jsonObjectFila_new.toString());


                    request.put("filas",jsonObjectFila_new);








                } catch (Exception e) {
                    Log.e( "Exception FOTO FILA", e.toString());
                }



                Log.e( "request NEW: ", request.toString());

                //return null;









                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        new ActualizarCosechaExportada(context, cosecha).execute();
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
