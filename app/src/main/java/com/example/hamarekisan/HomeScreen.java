package com.example.hamarekisan;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Base64;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.hamarekisan.ml.Model;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class HomeScreen extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    BottomNavigationView bottomBar;
    Button save, viewmore;
    TextView confidence, textv;
    Button result;
    FusedLocationProviderClient fusedLocationProviderClient;
    ImageView imageView,tomato;
    ImageButton picture,select;
    private int IMAGE_MEAN = 0;
    private float IMAGE_STD = 1;
    int imageSize = 224;
    Bitmap Imagebitmap;
    String confidencevalue;
    FirebaseFirestore db;
    String conf = "";
    String res="";
    String percent="";
    Uri imageuri;
    String imageEncoded="";
    byte[] bytes;
    int choice=0;
    String url="";


    String[] classes = {"Bacterial_spot",
            "Early_blight",
            "Late_blight",
            "Leaf_Mold",
            "Mosaic_virus",
            "Septoria_leaf_spot",
            "Spider_mites Two-spotted_spider_mite",
            "Target_Spot",
            "Tomato_healthy",
            "Yellow_Leaf_Curl_Virus"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        save=findViewById(R.id.save);
        viewmore=findViewById(R.id.viewmore);
        result = findViewById(R.id.result);
        confidence = findViewById(R.id.confidence);
        imageView = findViewById(R.id.imageView);
        picture = findViewById(R.id.button);
        select = findViewById(R.id.button2);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(HomeScreen.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(HomeScreen.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(HomeScreen.this
                    , new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
            ActivityCompat.requestPermissions(HomeScreen.this
                    , new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 44);
        }

        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launch camera if we have permission
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, 1);
                    } else {
                        //Request camera permission if we don't have it.
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
                    }
                }
            }
        });


        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launch camera if we have permission
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                        startActivityForResult(Intent.createChooser(intent, "Select"),2);
                    }
                    else{
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 200);
                    }
                }

            }
        });

        // bottom bar navigation

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
                        return true;

                    case R.id.user:
                        startActivity(new Intent(getApplicationContext(), UserDetailsActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }

                Toast.makeText(HomeScreen.this, item.getTitle(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.settings){
            Toast.makeText(HomeScreen.this, item.getTitle(), Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), Settings.class));
        }
        return true;
    }

    @SuppressLint("MissingPermission")
    private void getLOaction() {

        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null) {
                    try {
                        Geocoder geocoder = new Geocoder(HomeScreen.this, Locale.getDefault());
                        List<Address> address = geocoder.getFromLocation(
                                location.getLatitude(), location.getLongitude(), 1
                        );

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    public void classifyImage(Bitmap image) {
        try {
            Model model = Model.newInstance(getApplicationContext());

            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3);
            byteBuffer.order(ByteOrder.nativeOrder());
            int [] intValues = new int[imageSize * imageSize];
            image.getPixels(intValues, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());
            int pixel = 0;
            for(int i = 0; i < imageSize; i++){
                for(int j = 0; j < imageSize; j++){
                    int val = intValues[pixel++]; // RGB
                    byteBuffer.putFloat((((val >> 16) & 0xFF)-IMAGE_MEAN)/IMAGE_STD);
                    byteBuffer.putFloat((((val >> 8) & 0xFF)-IMAGE_MEAN)/IMAGE_STD);
                    byteBuffer.putFloat((((val) & 0xFF)-IMAGE_MEAN)/IMAGE_STD);
                }
            }

            inputFeature0.loadBuffer(byteBuffer);
            Model.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            float[] confidences = outputFeature0.getFloatArray();
            // find the index of the class with the biggest confidence.
            int maxPos = 0;
            float maxConfidence = 0;
            for(int i = 0; i < confidences.length; i++){
                if(confidences[i] > maxConfidence){
                    maxConfidence = confidences[i];
                    maxPos = i;
                }
            }
            String s="";
            result.setText(classes[maxPos]);
            result.setVisibility(View.VISIBLE);
            for(int i = 0; i < classes.length; i++){
                if(classes[maxPos]== classes[i])
                {
                    s += String.format("%s: %.1f%%\n", "Confidence", confidences[i] * 100);
                    percent+= String.valueOf(confidences[i] * 100);
                }
            }
            conf=s;
            res=classes[maxPos];
            confidence.setText(s);
            confidence.setVisibility(View.VISIBLE);
            model.close();
            viewmore.setVisibility(View.VISIBLE);
            save.setVisibility(View.VISIBLE);
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(choice==1){
                        Intent i = new Intent(HomeScreen.this, UploadCameraImage.class);
                        i.putExtra("image", Imagebitmap);
                        i.putExtra("result", res);
                        i.putExtra("confidence", conf);
                        i.putExtra("percent", percent);
                        i.putExtra("imageEncoded", imageEncoded);
                        startActivity(i);
                    }
                    if(choice==2) {
                        Intent i = new Intent(HomeScreen.this, Upload.class);
                        i.putExtra("image", Imagebitmap);
                        i.putExtra("result", res);
                        i.putExtra("confidence", conf);
                        i.putExtra("percent", percent);
                        i.putExtra("uri", imageuri.toString());
                        startActivity(i);
                    }
                }
                });

            viewmore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch(res){
                        case "Bacterial_spot":
                            Intent a = new Intent(HomeScreen.this, WebviewActivity.class);
                            url="file:///android_asset/Bacterial Spot.html".toString();
                            a.putExtra("url",url);
                            startActivity(a);
                            break;
                        case "Early_blight":
                            Intent b = new Intent(HomeScreen.this, WebviewActivity.class);
                            url="file:///android_asset/Early Blight.html".toString();
                            b.putExtra("url",url);
                            startActivity(b);
                            break;
                        case "Late_blight":
                            Intent c = new Intent(HomeScreen.this, WebviewActivity.class);
                            url="file:///android_asset/Late Blight.html".toString();
                            c.putExtra("url",url);
                            startActivity(c);
                            break;
                        case "Leaf_Mold":
                            Intent d = new Intent(HomeScreen.this, WebviewActivity.class);
                            url="file:///android_asset/Leaf Mold.html".toString();
                            d.putExtra("url",url);
                            startActivity(d);
                            break;
                        case "Mosaic_virus":
                            Intent e = new Intent(HomeScreen.this, WebviewActivity.class);
                            url="file:///android_asset/Mosaic virus.html".toString();
                            e.putExtra("url",url);
                            startActivity(e);
                            break;
                        case "Septoria_leaf_spot":
                            Intent f = new Intent(HomeScreen.this, WebviewActivity.class);
                            url="file:///android_asset/Septoria Leaf Spot.html".toString();
                            f.putExtra("url",url);
                            startActivity(f);
                            break;
                        case "Spider_mites Two-spotted_spider_mite":
                            Intent g = new Intent(HomeScreen.this, WebviewActivity.class);
                            url="file:///android_asset/Spider Mites Two-spotted Spider Mite.html".toString();
                            g.putExtra("url",url);
                            startActivity(g);
                            break;
                        case "Target_Spot":
                            Intent h = new Intent(HomeScreen.this, WebviewActivity.class);
                            url="file:///android_asset/Target Spot.html".toString();
                            h.putExtra("url",url);
                            startActivity(h);
                            break;
                        case "Yellow_Leaf_Curl_Virus":
                            Intent i = new Intent(HomeScreen.this, WebviewActivity.class);
                            url="file:///android_asset/Yellow Leaf Curl Virus.html".toString();
                            i.putExtra("url",url);
                            startActivity(i);
                            break;
                        case "Tomato_healthy":
                            Intent j = new Intent(HomeScreen.this, WebviewActivity.class);
                            url="file:///android_asset/healthy.html".toString();
                            j.putExtra("url",url);
                            startActivity(j);
                            break;
                    }
                }
                });

        } catch (IOException e) {
            // TODO Handle the exception
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            imageuri=data.getData();
            if(requestCode == 1 ){
                choice=1;
                Bitmap image = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.PNG, 100, stream);
                bytes=stream.toByteArray();
                imageEncoded = Base64.encodeToString(bytes, Base64.DEFAULT);
                System.out.println("bytes home "+bytes);
                System.out.println("imageencode home "+imageEncoded);
                int dimension = Math.min(image.getWidth(), image.getHeight());
                image = ThumbnailUtils.extractThumbnail(image, dimension, dimension);
                Imagebitmap=image;
                imageView.setImageBitmap(image);
                image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
                classifyImage(image);
            }
            if(requestCode == 2){
                choice=2;
                Uri datapath = data.getData();
                try {
                    Bitmap image=MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(),datapath);
                    int dimension = Math.min(image.getWidth(), image.getHeight());
                    image = ThumbnailUtils.extractThumbnail(image, dimension, dimension);
                    Imagebitmap=image;
                    imageView.setImageBitmap(image);
                    image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
                    classifyImage(image);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }
    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();
        firebaseAuth.getInstance().signOut();
        Intent i = new Intent(HomeScreen.this, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        getSharedPreferences("preferenceName",0).edit().clear().commit();
    }




}