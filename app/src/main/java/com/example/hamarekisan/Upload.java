package com.example.hamarekisan;
import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import java.text.*;
import java.util.*;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Upload extends AppCompatActivity {
    ImageView imageView;
    String confidence, result, percent;
    String userEmail;
    FirebaseFirestore db= FirebaseFirestore.getInstance();
    Uri uri;
    private FirebaseAuth firebaseAuth;
    private Uri imageUri;
    FirebaseUser user;
    String date;
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
        Bundle extras = getIntent().getExtras();
        imageUri = Uri.parse(extras.getString("uri"));

        user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        userEmail=user.getEmail();
        date = new SimpleDateFormat("dd-M-yyyy hh:mm:ss", Locale.getDefault()).format(new Date());
        System.out.println(date);
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
                            String doc=userId+" "+date;
                            db.collection("prediction").document(doc).set(prediction);
                            db.collection("users").document(userId).collection("PredictionHistory").document(date).set(prediction);
                        }
                     });
            }
            });
          }
        }
    }