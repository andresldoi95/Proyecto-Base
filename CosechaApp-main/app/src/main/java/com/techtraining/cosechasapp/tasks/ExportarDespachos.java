package com.techtraining.cosechasapp.tasks;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.techtraining.cosechasapp.DBManager;
import com.techtraining.cosechasapp.Helper;
import com.techtraining.cosechasapp.NetworkManager;
import com.techtraining.cosechasapp.db.AppDatabase;
import com.techtraining.cosechasapp.db.Cosecha;
import com.techtraining.cosechasapp.db.CosechaDao;
import com.techtraining.cosechasapp.db.FilaCosecha;
import com.techtraining.cosechasapp.db.ImagenFila;
import com.techtraining.cosechasapp.db.TrozaFotos;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExportarDespachos extends AsyncTask<Void, Void, Void> {
    private Context context;
    private AppDatabase appDatabase;
    private Cosecha cosecha;
    private NetworkManager networkManager;
    private List<TrozaFotos> trozaFotosList;
    private ArrayList<ImagenFila> imagenFilaList;

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
            imagenFilaList = new ArrayList<>();
            cosecha.filas = appDatabase.filaCosechaDao().loadByCosecha(cosecha.id);
            for (int i = 0; i < cosecha.filas.size(); i++) {
                FilaCosecha fila = cosecha.filas.get(i);
                fila.sueltos = appDatabase.filaSueltoDao().loadByFila(fila.id);
                fila.fotos = appDatabase.imagenFilaDao().loadByFila(fila.id);

                imagenFilaList.addAll(appDatabase.imagenFilaDao().loadByFila(fila.id));
            }
            cosecha.troza = appDatabase.trozaDao().loadByCosecha(cosecha.id);
            if(cosecha.troza != null){
                trozaFotosList = appDatabase.trozaFotosDao().loadAllByTrozaId(cosecha.troza.id);
            }
            cosecha.troza_fotos= trozaFotosList;
            String url = Helper.URL_API + "/despachos";
            Gson gson = new Gson();
            String json = gson.toJson(cosecha);
            try {
                JSONObject request = new JSONObject(json);
                JSONObject request_new = new JSONObject();
                Log.e( "request: ", request.toString());

                /*

                //FOTOS TROZAS
                try {
                    JSONArray jsonArrayTrozaFotos = request.getJSONArray("troza_fotos");
                    JSONObject jsonObjectTrozaFotos_new = new JSONObject();



                        for(int i2=0; i2<jsonArrayTrozaFotos.length(); i2++){
                            JSONObject jsonObjectFilaFotos = jsonArrayTrozaFotos.getJSONObject(i2);

                            if(!jsonObjectFilaFotos.getString("foto").equals("NULL") && !jsonObjectFilaFotos.getString("foto").equals("")){
                                Bitmap bitmap = null;
                                BitmapFactory.Options options = null;
                                options = new BitmapFactory.Options();
                                bitmap = BitmapFactory.decodeFile(jsonObjectFilaFotos.getString("foto"), options);
                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 40, stream);
                                byte[] byte_arr = stream.toByteArray();
                                String encodedImage = Base64.encodeToString(byte_arr, 0);

                                //Se actualiza el path
                                jsonObjectFilaFotos.put("foto",encodedImage);

                                //Se actualiza Fila Fotos

                            }


                            jsonObjectTrozaFotos_new.put(String.valueOf(i2),jsonObjectFilaFotos);


                        }

                    request.put("troza_fotos",jsonObjectTrozaFotos_new);

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
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 40, stream);
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

                 */



                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        new ActualizarCosechaExportada(context, cosecha).execute();

                        if(cosecha.troza != null){
                            if(trozaFotosList != null && trozaFotosList.size() > 0){
                                for (TrozaFotos trozaFotos:trozaFotosList){
                                    Map<String, String> params = new HashMap<>(2);
                                    params.put("troza_id", trozaFotos.trozaId);
                                    params.put("id", trozaFotos.id);

                                    new ExportarFotos(
                                            context,
                                            Helper.URL_API + "/trozaFotos",
                                            params,
                                            trozaFotos.foto,
                                            trozaFotos.id
                                    ).execute();
                                }
                            }
                        }

                        if(imagenFilaList != null && imagenFilaList.size() > 0){
                            for(ImagenFila imagenFila:imagenFilaList){
                                Map<String, String> params = new HashMap<>(1);
                                params.put("fila_id", imagenFila.filaId);

                                new ExportarFotos(
                                        context,
                                        Helper.URL_API + "/fotos",
                                        params,
                                        imagenFila.path,
                                        imagenFila.id
                                ).execute();
                            }
                        }
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
