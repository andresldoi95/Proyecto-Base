package com.techtraining.cosechasapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.techtraining.cosechasapp.db.Troza;
import com.techtraining.cosechasapp.tasks.CargarDatosTroza;
import com.techtraining.cosechasapp.tasks.GuardarFoto;
import com.techtraining.cosechasapp.tasks.GuardarTroza;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class TrozaActivity extends AppCompatActivity {
    private Button btnGuardarTroza;
    private Button btnFoto;
    public EditText etNumeroTrozas;
    public EditText etVolumenEstimado;
    private static int READ_STORAGE = 1;
    private static int WRITE_STORAGE =2;
    private static int TAKE_PHOTO_REQUEST = 3;
    public String currentFoto;
    public EditText etObservaciones;
    public ImageView ivFoto;
    private Button btnQuitarFoto;
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
        btnQuitarFoto = findViewById(R.id.btnQuitarFoto);
        btnGuardarTroza = findViewById(R.id.btnGuardarTroza);
        btnFoto = findViewById(R.id.btnFoto);
        etNumeroTrozas = findViewById(R.id.etNumeroTrozas);
        etVolumenEstimado = findViewById(R.id.etVolumenEstimado);
        etObservaciones = findViewById(R.id.etObservaciones);
        ivFoto = findViewById(R.id.ivFotoTroza);
        btnGuardarTroza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(formularioValido()) {
                    Troza troza = new Troza();
                    troza.numeroTrozas = Double.parseDouble(etNumeroTrozas.getText().toString());
                    troza.volumenEstimado = Double.parseDouble(etVolumenEstimado.getText().toString());
                    troza.observaciones = etObservaciones.getText().toString();
                    troza.foto = currentFoto;
                    new GuardarTroza(TrozaActivity.this, troza).execute();
                }
            }
        });
        btnQuitarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentFoto = null;
            }
        });
        btnFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isWriteStoragePermissionGranted()) {
                    tomarFoto();
                }
            }
        });
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
        startActivityForResult(takePicture, TAKE_PHOTO_REQUEST);
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

                    Log.e(GaleriaFila.class.getName(), getString(R.string.permisos_aprobados));
                } else {
                    isPerpermissionGranted=true;
                    Log.e(GaleriaFila.class.getName(), getString(R.string.permisos_aprobados));
                }
                if (isPerpermissionGranted) {
                    tomarFoto();
                }
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 3:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        currentFoto = Environment.getExternalStorageDirectory().getAbsolutePath() +
                                "/" + getString(R.string.app_name);
                        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                        FileOutputStream fOut = null;
                        try {
                            File file = File.createTempFile(getString(R.string.app_name) + System.currentTimeMillis(), ".png", dir);
                            fOut = new FileOutputStream(file);
                            selectedImage.compress(Bitmap.CompressFormat.PNG, Helper.IMAGE_QUALITY, fOut);
                            fOut.flush();
                            fOut.close();
                            currentFoto = file.getPath();
                            ivFoto.setImageBitmap(BitmapFactory.decodeFile(file.getPath()));
                        } catch (FileNotFoundException e) {
                            Log.e(TrozaActivity.class.getName(), e.getMessage());
                        } catch (IOException e) {
                            Log.e(TrozaActivity.class.getName(), e.getMessage());
                        }
                    }
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    protected void onResume() {
        new CargarDatosTroza(this).execute();
        super.onResume();
    }
}