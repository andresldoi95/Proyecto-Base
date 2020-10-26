package com.techtraining.cosechasapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

public class ImagePreviewActivity extends AppCompatActivity {
    public static final String IMAGE_NAME = "imageName";
    private ImageView imgPreview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_preview);
        String path = getIntent().getStringExtra(IMAGE_NAME);
        imgPreview = findViewById(R.id.imgPreview);
        setTitle(path);
        imgPreview.setImageBitmap(BitmapFactory.decodeFile(path));
    }
}