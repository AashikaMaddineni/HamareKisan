package com.example.hamarekisan;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class UploadCameraImage extends AppCompatActivity {
    ImageView imageView;
    String confidence, result, percent;
    String userEmail;
    FirebaseFirestore db= FirebaseFirestore.getInstance();
    Uri uri;
    private FirebaseAuth firebaseAuth;
    private Uri imageUri;
    FirebaseUser user;
    String date;
    String imageEncoded="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_activity);
        // initialise the layout
        imageView = findViewById(R.id.images);
        result=getIntent().getStringExtra("result");
        confidence=getIntent().getStringExtra("confidence");
        percent=getIntent().getStringExtra("percent");

        imageEncoded=getIntent().getStringExtra("imageEncoded");
        System.out.println("imageEncoded upload "+imageEncoded);

        Bitmap bitmap = (Bitmap) getIntent().getParcelableExtra("image");
        imageView.setImageBitmap(bitmap);

        user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        userEmail=user.getEmail();
        date = new SimpleDateFormat("dd-M-yyyy hh:mm:ss", Locale.getDefault()).format(new Date());
            if (imageEncoded != null) {
                            Map<String, Object> prediction = new HashMap<>();
                            prediction.put("Uid", userId);
                            prediction.put("Prediction", result);
                            prediction.put("Image", imageEncoded.toString());
                            prediction.put("Confidence", percent);
                            prediction.put("Date", date);
                            prediction.put("UploadType", "Camera");
                            String doc=userId+" "+date;
                            db.collection("prediction").document(doc).set(prediction);
                            db.collection("users").document(userId).collection("PredictionHistory").document(date).set(prediction);
             }
            }
          }

