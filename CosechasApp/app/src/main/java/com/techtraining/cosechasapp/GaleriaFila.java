package com.techtraining.cosechasapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.Toast;

import com.techtraining.cosechasapp.adapters.ImagenGaleriaAdapter;
import com.techtraining.cosechasapp.db.FilaCosecha;
import com.techtraining.cosechasapp.db.ImagenFila;
import com.techtraining.cosechasapp.tasks.CargarFotosGaleria;
import com.techtraining.cosechasapp.tasks.EliminarImagenes;
import com.techtraining.cosechasapp.tasks.GuardarFoto;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class GaleriaFila extends AppCompatActivity {
    public GridView grvGaleria;
    private static int READ_STORAGE = 1;
    private static int WRITE_STORAGE =2;
    private static int TAKE_PHOTO_REQUEST = 3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galeria_fila);
        grvGaleria = findViewById(R.id.grvGaleria);
        setTitle(R.string.galeria);
    }

    private  boolean isReadStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {

            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(GaleriaFila.class.getName(),getString(R.string.permisos_aprobados));
                return true;
            } else {
                Log.v(GaleriaFila.class.getName(),getString(R.string.permisos_denegados));
                ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE}, READ_STORAGE);
                return false;
            }
        }
        else {
            Log.v(GaleriaFila.class.getName(),getString(R.string.permisos_aprobados));
            return true;
        }
    }
    private  boolean isWriteStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {

            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(GaleriaFila.class.getName(),getString(R.string.permisos_aprobados));
                return true;
            } else {
                Log.v(GaleriaFila.class.getName(),getString(R.string.permisos_denegados));
                ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_STORAGE);
                return false;
            }
        }
        else {
            Log.v(GaleriaFila.class.getName(),getString(R.string.permisos_aprobados));
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1:
                boolean isPerpermissionForAllGranted = false;
                if (grantResults.length > 0 && permissions.length==grantResults.length) {
                    for (int i = 0; i < permissions.length; i++){
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED){
                            isPerpermissionForAllGranted=true;
                        }else{
                            isPerpermissionForAllGranted=false;
                        }
                    }

                    Log.e(GaleriaFila.class.getName(), getString(R.string.permisos_aprobados));
                } else {
                    isPerpermissionForAllGranted=true;
                    Log.e(GaleriaFila.class.getName(), getString(R.string.permisos_aprobados));
                }
                if(isPerpermissionForAllGranted){
                    cargarFotos();
                }
            break;
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
    protected void onResume() {
        if (isReadStoragePermissionGranted()) {
            cargarFotos();
        }
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_galeria, menu);
        return true;
    }
    private void tomarFoto() {
        Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePicture, TAKE_PHOTO_REQUEST);
    }
    private void selectImage() {
        final CharSequence[] options = { getString(R.string.tomar_foto),getString(R.string.cancelar) };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.cargar_foto));

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals(getString(R.string.tomar_foto))) {
                    if (isWriteStoragePermissionGranted())
                        tomarFoto();
                } else if (options[item].equals(getString(R.string.cancelar))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    private void eliminarFotos() {
        ImagenGaleriaAdapter imagenGaleriaAdapter = (ImagenGaleriaAdapter) grvGaleria.getAdapter();
        final ArrayList<ImagenFila> imagenes = new ArrayList<>();
        for (int i = 0; i < imagenGaleriaAdapter.getCount(); i++) {
            ImagenFila imagenFila = (ImagenFila) imagenGaleriaAdapter.getItem(i);
            if (imagenFila.selected)
                imagenes.add(imagenFila);
        }
        if (imagenes.size() > 0) {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            new EliminarImagenes(GaleriaFila.this, imagenes).execute();
                            break;
                    }
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(getString(R.string.desea_eliminar_imagenes)).setPositiveButton(getString(R.string.si), dialogClickListener)
                    .setNegativeButton(getString(R.string.no), dialogClickListener).show();

        }
        else {
            Toast.makeText(this, R.string.no_imagenes_eliminar, Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_nueva_foto:
                selectImage();
                return true;
            case R.id.nav_eliminar_fotos:
                eliminarFotos();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void cargarFotos() {
        new CargarFotosGaleria(this).execute();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 3:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                                "/" + getString(R.string.app_name);
                        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

                        FileOutputStream fOut = null;
                        try {
                            File file = File.createTempFile(getString(R.string.app_name) + System.currentTimeMillis(), ".png", dir);
                            fOut = new FileOutputStream(file);
                            selectedImage.compress(Bitmap.CompressFormat.PNG, Helper.IMAGE_QUALITY, fOut);
                            fOut.flush();
                            fOut.close();
                            new GuardarFoto(this, file.getPath()).execute();
                        } catch (FileNotFoundException e) {
                            Log.e(GaleriaFila.class.getName(), e.getMessage());
                        } catch (IOException e) {
                            Log.e(GaleriaFila.class.getName(), e.getMessage());
                        }
                    }
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}