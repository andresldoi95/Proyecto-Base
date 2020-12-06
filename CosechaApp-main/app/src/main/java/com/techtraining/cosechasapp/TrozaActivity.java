package com.techtraining.cosechasapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.techtraining.cosechasapp.adapters.TrozaFotosAdapter;
import com.techtraining.cosechasapp.db.Troza;
import com.techtraining.cosechasapp.db.TrozaFotos;
import com.techtraining.cosechasapp.tasks.CargarDatosTroza;
import com.techtraining.cosechasapp.tasks.GuardarTroza;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TrozaActivity extends AppCompatActivity {
    private Button btnGuardarTroza;
    private Button btnFoto;
    public EditText etNumeroTrozas;
    public EditText etVolumenEstimado;
    private ListView listTrozaFotos;
    public TrozaFotosAdapter adapter;
    private static int READ_STORAGE = 1;
    private static int WRITE_STORAGE =2;
    private static int TAKE_PHOTO_REQUEST = 3;
    public ArrayList<TrozaFotos> currentFotos;
    public EditText etObservaciones;
    private boolean isHistorial = false;

    // public ImageView ivFoto;
    // private Button btnQuitarFoto;
    private boolean formularioValido() {
        if (etNumeroTrozas.getText().length() == 0) {
            Toast.makeText(this, R.string.numero_trozas_requerido, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (etVolumenEstimado.getText().length() == 0) {
            Toast.makeText(this, R.string.volumen_trozas_requerido, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_troza);
        // btnQuitarFoto = findViewById(R.id.btnQuitarFoto);
        isHistorial = getSharedPreferences(Helper.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).getBoolean(Helper.IS_HISTORIAL, false);
        btnGuardarTroza = findViewById(R.id.btnGuardarTroza);
        btnFoto = findViewById(R.id.btnFoto);
        etNumeroTrozas = findViewById(R.id.etNumeroTrozas);
        etVolumenEstimado = findViewById(R.id.etVolumenEstimado);
        etObservaciones = findViewById(R.id.etObservaciones);
        listTrozaFotos = findViewById(R.id.listTrozaFotos);
        listTrozaFotos.setOnTouchListener(new ListView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });
        currentFotos = new ArrayList<>();
        adapter = new TrozaFotosAdapter(currentFotos, this);
        listTrozaFotos.setAdapter(adapter);

        // ivFoto = findViewById(R.id.ivFotoTroza);
        btnGuardarTroza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(formularioValido()) {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    Troza troza = new Troza();
                                    troza.numeroTrozas = Double.parseDouble(etNumeroTrozas.getText().toString());
                                    troza.volumenEstimado = Double.parseDouble(etVolumenEstimado.getText().toString());
                                    troza.observaciones = etObservaciones.getText().toString();
                                    // troza.foto = currentFoto;
                                    new GuardarTroza(TrozaActivity.this, troza, currentFotos).execute();
                                    break;
                            }
                        }
                    };
                    AlertDialog.Builder builder = new AlertDialog.Builder(TrozaActivity.this);
                    builder.setMessage(getString(R.string.desea_finalizar_despacho)).setPositiveButton(getString(R.string.si), dialogClickListener)
                            .setNegativeButton(getString(R.string.no), dialogClickListener).show();
                }
            }
        });
        /*btnQuitarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentFoto = null;
            }
        });*/
        btnFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isWriteStoragePermissionGranted()) {
                    if(currentFotos.size() < Helper.MAX_PHOTOS_TROZA) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(TrozaActivity.this);
                        builder.setTitle(R.string.cargar_foto);
                        builder.setItems(new CharSequence[]
                                        {getString(R.string.tomar_foto), getString(R.string.desde_galeria)},
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (which) {
                                            case 0:
                                                tomarFoto();
                                                break;
                                            case 1:
                                                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                                                photoPickerIntent.setType("image/*");
                                                startActivityForResult(photoPickerIntent, 1);
                                                break;
                                        }
                                    }
                                });
                        builder.create().show();
                    }else{
                        Toast.makeText(
                                getBaseContext(),
                                getString(R.string.max_photos_message, Helper.MAX_PHOTOS_TROZA),
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
            }
        });

        if(isHistorial){
            btnGuardarTroza.setVisibility(View.GONE);
            btnGuardarTroza.setEnabled(false);
            btnFoto.setVisibility(View.GONE);
            btnFoto.setEnabled(false);
            etNumeroTrozas.setEnabled(false);
            etVolumenEstimado.setEnabled(false);
            etObservaciones.setEnabled(false);
            // listTrozaFotos.setEnabled(false);
        }
    }

    public  boolean isReadStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {

            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TrozaActivity.class.getName(),getString(R.string.permisos_aprobados));
                return true;
            } else {
                Log.v(TrozaActivity.class.getName(),getString(R.string.permisos_denegados));
                ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE}, READ_STORAGE);
                return false;
            }
        }
        else {
            Log.v(TrozaActivity.class.getName(),getString(R.string.permisos_aprobados));
            return true;
        }
    }

    private  boolean isWriteStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TrozaActivity.class.getName(),getString(R.string.permisos_aprobados));
                return true;
            } else {
                Log.v(TrozaActivity.class.getName(),getString(R.string.permisos_denegados));
                ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_STORAGE);
                return false;
            }
        }
        else {
            Log.v(TrozaActivity.class.getName(),getString(R.string.permisos_aprobados));
            return true;
        }
    }

    private void tomarFoto() {
        Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePicture.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Log.i("**", ex.getLocalizedMessage());
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(
                        this,
                        "com.techtraining.cosechasapp.fileprovider",
                        photoFile
                );
                takePicture.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePicture, TAKE_PHOTO_REQUEST);
            }
        }

        // startActivityForResult(takePicture, TAKE_PHOTO_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 2:
                boolean isPerpermissionGranted = false;
                if (grantResults.length > 0 && permissions.length==grantResults.length) {
                    for (int i = 0; i < permissions.length; i++){
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED){
                            isPerpermissionGranted=true;
                        }else{
                            isPerpermissionGranted=false;
                        }
                    }

                    Log.e(GaleriaFilaActivity.class.getName(), getString(R.string.permisos_aprobados));
                } else {
                    isPerpermissionGranted=true;
                    Log.e(GaleriaFilaActivity.class.getName(), getString(R.string.permisos_aprobados));
                }
                if (isPerpermissionGranted) {
                    tomarFoto();
                }
                break;
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmssSSS").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        // trozaFotos.foto = image.getAbsolutePath();
        SharedPreferences.Editor editor = getSharedPreferences(Helper.SHARED_PREFERENCES_NAME, MODE_PRIVATE).edit();
        editor.putString(Helper.newPhotoPath, image.getAbsolutePath());
        editor.commit();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 3:
                    String newPhotoPath = getSharedPreferences(Helper.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).getString(Helper.newPhotoPath, "");
                    if (resultCode == RESULT_OK && !newPhotoPath.equals("")) {
                        try{
                            TrozaFotos trozaFotos = new TrozaFotos();
                            trozaFotos.foto = newPhotoPath;
                            currentFotos.add(trozaFotos);
                            adapter.notifyDataSetChanged();
                            SharedPreferences.Editor editor = getSharedPreferences(Helper.SHARED_PREFERENCES_NAME, MODE_PRIVATE).edit();
                            editor.putString(Helper.newPhotoPath, "");
                            editor.commit();
                        }catch (Exception e){
                            Log.e("**", e.getLocalizedMessage());
                        }
                        /*Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        trozaFotos.foto = Environment.getExternalStorageDirectory().getAbsolutePath() +
                                "/" + getString(R.string.app_name);
                        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                        FileOutputStream fOut = null;
                        try {
                            File file = File.createTempFile(getString(R.string.app_name) + System.currentTimeMillis(), ".png", dir);
                            fOut = new FileOutputStream(file);
                            selectedImage.compress(Bitmap.CompressFormat.PNG, Helper.IMAGE_QUALITY, fOut);
                            fOut.flush();
                            fOut.close();
                            trozaFotos.foto = file.getPath();
                            currentFotos.add(trozaFotos);
                            adapter.notifyDataSetChanged();
                            // ivFoto.setImageBitmap(BitmapFactory.decodeFile(file.getPath()));
                        } catch (FileNotFoundException e) {
                            Log.e(TrozaActivity.class.getName(), e.getMessage());
                        } catch (IOException e) {
                            Log.e(TrozaActivity.class.getName(), e.getMessage());
                        }*/
                    }
                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage =  data.getData();
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                            File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                            FileOutputStream fOut = null;
                            try {
                                TrozaFotos trozaFotos = new TrozaFotos();
                                File file = File.createTempFile(getString(R.string.app_name) + System.currentTimeMillis(), ".png", dir);
                                fOut = new FileOutputStream(file);
                                bitmap.compress(Bitmap.CompressFormat.PNG, Helper.IMAGE_QUALITY, fOut);
                                fOut.flush();
                                fOut.close();
                                trozaFotos.foto = file.getPath();
                                currentFotos.add(trozaFotos);
                                adapter.notifyDataSetChanged();
                                // ivFoto.setImageBitmap(BitmapFactory.decodeFile(file.getPath()));
                            } catch (FileNotFoundException e) {
                                Log.e(TrozaActivity.class.getName(), e.getMessage());
                            } catch (IOException e) {
                                Log.e(TrozaActivity.class.getName(), e.getMessage());
                            }
                        } catch (IOException e) {
                            Log.i(TrozaActivity.class.getName(),  e.getMessage());
                        }
                    }
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        isHistorial = getSharedPreferences(Helper.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).getBoolean(Helper.IS_HISTORIAL, false);
        new CargarDatosTroza(this, adapter, listTrozaFotos).execute();
        super.onResume();
    }
}