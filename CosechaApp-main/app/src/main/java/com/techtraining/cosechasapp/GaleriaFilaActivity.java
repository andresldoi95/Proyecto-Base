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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.Toast;

import com.techtraining.cosechasapp.adapters.ImagenGaleriaAdapter;
import com.techtraining.cosechasapp.db.ImagenFila;
import com.techtraining.cosechasapp.tasks.CargarFotosGaleria;
import com.techtraining.cosechasapp.tasks.EliminarImagenes;
import com.techtraining.cosechasapp.tasks.GuardarFoto;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class GaleriaFilaActivity extends AppCompatActivity {
    public GridView grvGaleria;
    private static int READ_STORAGE = 1;
    private static int WRITE_STORAGE =2;
    private static int TAKE_PHOTO_REQUEST = 3;
    private boolean isHistorial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galeria_fila);
        grvGaleria = findViewById(R.id.grvGaleria);
        setTitle(R.string.galeria);
        isHistorial = getSharedPreferences(Helper.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).getBoolean(Helper.IS_HISTORIAL, false);
    }

    private  boolean isReadStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {

            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(GaleriaFilaActivity.class.getName(),getString(R.string.permisos_aprobados));
                return true;
            } else {
                Log.v(GaleriaFilaActivity.class.getName(),getString(R.string.permisos_denegados));
                ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE}, READ_STORAGE);
                return false;
            }
        }
        else {
            Log.v(GaleriaFilaActivity.class.getName(),getString(R.string.permisos_aprobados));
            return true;
        }
    }

    private  boolean isWriteStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {

            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(GaleriaFilaActivity.class.getName(),getString(R.string.permisos_aprobados));
                return true;
            } else {
                Log.v(GaleriaFilaActivity.class.getName(),getString(R.string.permisos_denegados));
                ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_STORAGE);
                return false;
            }
        }
        else {
            Log.v(GaleriaFilaActivity.class.getName(),getString(R.string.permisos_aprobados));
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

                    Log.e(GaleriaFilaActivity.class.getName(), getString(R.string.permisos_aprobados));
                } else {
                    isPerpermissionForAllGranted=true;
                    Log.e(GaleriaFilaActivity.class.getName(), getString(R.string.permisos_aprobados));
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

    @Override
    protected void onResume() {
        isHistorial = getSharedPreferences(Helper.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).getBoolean(Helper.IS_HISTORIAL, false);
        if (isReadStoragePermissionGranted()) {
            cargarFotos();
        }
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_galeria, menu);
        if(isHistorial){
            for (int i = 0; i < menu.size(); i++)
                menu.getItem(i).setVisible(false);
        }
        return true;
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

    private void selectImage() {
        final CharSequence[] options = { getString(R.string.tomar_foto), getString(R.string.desde_galeria), getString(R.string.cancelar) };

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
                else if (options[item].equals(getString(R.string.desde_galeria))) {
                    /*Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , 1);*/
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, 1);
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
                            new EliminarImagenes(GaleriaFilaActivity.this, imagenes).execute();
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
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage =  data.getData();
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                            File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                            FileOutputStream fOut = null;
                            try {
                                File file = File.createTempFile(getString(R.string.app_name) + System.currentTimeMillis(), ".png", dir);
                                fOut = new FileOutputStream(file);
                                bitmap.compress(Bitmap.CompressFormat.PNG, Helper.IMAGE_QUALITY, fOut);
                                fOut.flush();
                                fOut.close();
                                new GuardarFoto(this, file.getPath()).execute();
                            } catch (FileNotFoundException e) {
                                Log.e(GaleriaFilaActivity.class.getName(), e.getMessage());
                            } catch (IOException e) {
                                Log.e(GaleriaFilaActivity.class.getName(), e.getMessage());
                            }
                        } catch (IOException e) {
                            Log.i(GaleriaFilaActivity.class.getName(),  e.getMessage());
                        }
                    }
                    break;
                case 3:
                    String newPhotoPath = getSharedPreferences(Helper.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).getString(Helper.newPhotoPath, "");
                    if (resultCode == RESULT_OK && !newPhotoPath.equals("")) {
                        try{
                            new GuardarFoto(this, newPhotoPath).execute();
                            SharedPreferences.Editor editor = getSharedPreferences(Helper.SHARED_PREFERENCES_NAME, MODE_PRIVATE).edit();
                            editor.putString(Helper.newPhotoPath, "");
                            editor.commit();
                        }catch (Exception e){
                            Log.e("**", e.getLocalizedMessage());
                        }
                        /*
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
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
                         */
                    }
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}