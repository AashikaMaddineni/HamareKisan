package com.example.hamarekisan;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.*;
import java.util.*;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Upload extends AppCompatActivity {
    ImageView imageView;
    String confidence, result, percent;
    BottomNavigationView bottomBar;
    String userEmail;
    FirebaseFirestore db= FirebaseFirestore.getInstance();
    private Uri imageUri;
    FirebaseUser user;
    String date;
    TextView res,conf,preddate;
    Button viewmore;
    String url="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_activity);
        // initialise the layout
        imageView = findViewById(R.id.imageView);
        result=getIntent().getStringExtra("result");
        confidence=getIntent().getStringExtra("confidence");
        percent=getIntent().getStringExtra("percent");
        viewmore=findViewById(R.id.viewmore);
        res=findViewById(R.id.result);
        conf=findViewById(R.id.confidence);
        preddate=findViewById(R.id.preddate);
        Bitmap bitmap = (Bitmap) getIntent().getParcelableExtra("image");
        imageView.setImageBitmap(bitmap);
        Bundle extras = getIntent().getExtras();
        imageUri = Uri.parse(extras.getString("uri"));
        System.out.println("ImageUri"+imageUri);
        user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        userEmail=user.getEmail();
        date = new SimpleDateFormat("dd-M-yyyy hh:mm:ss", Locale.getDefault()).format(new Date());

        // assiginig values to display on screen
        res.setText(result);
        conf.setText(percent+"%");
        preddate.setText(date);

        if (imageUri != null) {
            StorageReference reference = FirebaseStorage.getInstance().getReference("/prediction");
            final StorageReference fileRef = reference.child(userEmail).child(date);
            fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Map<String, Object> prediction = new HashMap<>();
                            prediction.put("Uid", userId);
                            prediction.put("Prediction", result);
                            prediction.put("Image", uri.toString());
                            prediction.put("Confidence", percent);
                            prediction.put("Date", date);
                            prediction.put("UploadType", "Storage");
                            String doc=userId+" "+date;
                            db.collection("prediction").document(doc).set(prediction);
                            db.collection("users").document(userId).collection("PredictionHistory").document(date).set(prediction);
                        }
                     });
            }
            });
          }



        viewmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch(result){
                    case "Bacterial_spot":
                        Intent a = new Intent(Upload.this, WebviewActivity.class);
                        url="file:///android_asset/Bacterial Spot.html".toString();
                        a.putExtra("url",url);
                        startActivity(a);
                        break;
                    case "Early_blight":
                        Intent b = new Intent(Upload.this, WebviewActivity.class);
                        url="file:///android_asset/Early Blight.html".toString();
                        b.putExtra("url",url);
                        startActivity(b);
                        break;
                    case "Late_blight":
                        Intent c = new Intent(Upload.this, WebviewActivity.class);
                        url="file:///android_asset/Late Blight.html".toString();
                        c.putExtra("url",url);
                        startActivity(c);
                        break;
                    case "Leaf_Mold":
                        Intent d = new Intent(Upload.this, WebviewActivity.class);
                        url="file:///android_asset/Leaf Mold.html".toString();
                        d.putExtra("url",url);
                        startActivity(d);
                        break;
                    case "Mosaic_virus":
                        Intent e = new Intent(Upload.this, WebviewActivity.class);
                        url="file:///android_asset/Mosaic virus.html".toString();
                        e.putExtra("url",url);
                        startActivity(e);
                        break;
                    case "Septoria_leaf_spot":
                        Intent f = new Intent(Upload.this, WebviewActivity.class);
                        url="file:///android_asset/Septoria Leaf Spot.html".toString();
                        f.putExtra("url",url);
                        startActivity(f);
                        break;
                    case "Spider_mites Two-spotted_spider_mite":
                        Intent g = new Intent(Upload.this, WebviewActivity.class);
                        url="file:///android_asset/Spider Mites Two-spotted Spider Mite.html".toString();
                        g.putExtra("url",url);
                        startActivity(g);
                        break;
                    case "Target_Spot":
                        Intent h = new Intent(Upload.this, WebviewActivity.class);
                        url="file:///android_asset/Target Spot.html".toString();
                        h.putExtra("url",url);
                        startActivity(h);
                        break;
                    case "Yellow_Leaf_Curl_Virus":
                        Intent i = new Intent(Upload.this, WebviewActivity.class);
                        url="file:///android_asset/Yellow Leaf Curl Virus.html".toString();
                        i.putExtra("url",url);
                        startActivity(i);
                        break;
                    case "Tomato_healthy":
                        Intent j = new Intent(Upload.this, WebviewActivity.class);
                        url="file:///android_asset/healthy.html".toString();
                        j.putExtra("url",url);
                        startActivity(j);
                        break;
                }
            }
        });


        bottomBar = findViewById(R.id.bottomBar);
        bottomBar.setSelectedItemId(R.id.home);
        bottomBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.message:
                        startActivity(new Intent(getApplicationContext(), UserMessageActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), HomeScreen.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.user:
                        startActivity(new Intent(getApplicationContext(), UserDetailsActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }

                Toast.makeText(Upload.this, item.getTitle(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });


        }
    }