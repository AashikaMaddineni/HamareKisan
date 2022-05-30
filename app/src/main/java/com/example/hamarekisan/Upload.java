package com.example.hamarekisan;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

public class Upload extends AppCompatActivity {
    ImageView imageView;
    String confidence, result, percent;
    int imagevalue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_activity);
        // initialise the layout
        imageView = findViewById(R.id.images);
        result=getIntent().getStringExtra("result");
        confidence=getIntent().getStringExtra("confidence");
        percent=getIntent().getStringExtra("percent");
        System.out.println(result+" "+percent);
        Bitmap bitmap = (Bitmap) getIntent().getParcelableExtra("image");
        imageView.setImageBitmap(bitmap);
    }
}