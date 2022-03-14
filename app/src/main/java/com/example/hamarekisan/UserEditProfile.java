package com.example.hamarekisan;

import android.Manifest;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;


public class UserEditProfile extends AppCompatActivity {
    EditText etEmail, etNumber, etName, etaddress, etaboutyou, etlocation;
    Button done, editimage;
    String userEmail;
    FirebaseFirestore db;
    ImageView image1;
    private Uri imageUri;
    Uri url;
    private FirebaseAuth firebaseAuth;
    BottomNavigationView bottomBar;
    public static final String TAG = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_edit_profile);
        etEmail = findViewById(R.id.etPasswordEmail);
        etNumber = findViewById(R.id.etNumber);
        etName = findViewById(R.id.etName);
        etaddress = findViewById(R.id.etaddress);
        etaboutyou = findViewById(R.id.etAboutyou);
        etlocation = findViewById(R.id.etlocation);
        done = findViewById(R.id.done);
        image1=findViewById(R.id.img);
        editimage = findViewById(R.id.editimage);
        editimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString().trim();
                String name = etName.getText().toString().trim();
                String phnum = etNumber.getText().toString().trim();
                String regno = etaddress.getText().toString().trim();
                String location=etlocation.getText().toString().trim();
                String aboutyou=etaboutyou.getText().toString().trim();
                if ((TextUtils.isEmpty(email)) || TextUtils.isEmpty(name) || TextUtils.isEmpty(phnum) || (TextUtils.isEmpty(regno))) {
                    Toast.makeText(getApplicationContext(), "You should enter all the fields properly", Toast.LENGTH_LONG).show();
                    return;
                }
                if (email.equals(userEmail)) {
                    db = FirebaseFirestore.getInstance();
                    db.collection("/user/mentee/mentee_details")
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            DocumentReference docRef = document.getReference();
                                            if (document.getString("Email Address").equals(userEmail)) {
                                                if ((!TextUtils.isEmpty(email)) || (!TextUtils.isEmpty(name)) || (!TextUtils.isEmpty(phnum)) || (!TextUtils.isEmpty(regno))){
                                                    if (imageUri != null) {
                                                        StorageReference reference = FirebaseStorage.getInstance().getReference("/UserPhotos/mentee/");
                                                        final StorageReference fileRef = reference.child(userEmail);
                                                        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                            @Override
                                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                    @Override
                                                                    public void onSuccess(Uri uri) {
                                                                        url = uri;
                                                                        Toast.makeText(UserEditProfile.this, "Uploading to Storage!! " , Toast.LENGTH_SHORT).show();
                                                                        Map<String, Object> user = new HashMap<>();
                                                                        user.put("Full Name", name);
                                                                        user.put("Reg No", regno);
                                                                        user.put("Email Address", email);
                                                                        user.put("Mobile Number", phnum);
                                                                        if(TextUtils.isEmpty(aboutyou)) {
                                                                            user.put("Aboutyou", "About You");
                                                                        }
                                                                        else{
                                                                            user.put("Aboutyou", aboutyou);
                                                                        }
                                                                        if(TextUtils.isEmpty(location)) {
                                                                            user.put("Location", "Location");
                                                                        }
                                                                        else{
                                                                            user.put("Location", location);
                                                                        }
                                                                        user.put("Image", url.toString());
                                                                        db.collection("/user/mentee/mentee_details")
                                                                                .document(email).update(user);
                                                                        Toast.makeText(getApplicationContext(), "Upload success", Toast.LENGTH_LONG).show();
                                                                    }
                                                                });
                                                            }
                                                        });
                                                    }
                                                    else{
                                                        Map<String, Object> user = new HashMap<>();
                                                        user.put("Full Name", name);
                                                        user.put("Reg No", regno);
                                                        user.put("Email Address", email);
                                                        user.put("Mobile Number", phnum);
                                                        if(TextUtils.isEmpty(aboutyou)) {
                                                            user.put("Aboutyou", "About You");
                                                        }
                                                        else{
                                                            user.put("Aboutyou", aboutyou);
                                                        }
                                                        if(TextUtils.isEmpty(location)) {
                                                            user.put("Location", "Location");
                                                        }
                                                        else{
                                                            user.put("Location", location);
                                                        }
                                                        db.collection("/user/mentee/mentee_details")
                                                                .document(email).update(user);
                                                        Toast.makeText(getApplicationContext(), "Upload success", Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                                    else {
                                                        Toast.makeText(getApplicationContext(), "Unsuccesful fields", Toast.LENGTH_LONG).show();
                                                    }

                                            }
                                        }
                                    } else {
                                        Log.w(TAG, "Error getting documents.", task.getException());
                                    }

                                }
                            });
                }
                else {
                    Toast.makeText(getApplicationContext(), "Password Doesn't Match", Toast.LENGTH_LONG).show();
                }
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userEmail = user.getEmail();
        } else {
            userEmail = null;
        }
        db = FirebaseFirestore.getInstance();
        db.collection("/users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                System.out.println(user.getUid());
                                if (document.getId().equals(firebaseAuth.getUid())) {
                                    etEmail.setText(document.getString("Email Address"));
                                    etName.setText(document.getString("Full Name"));
                                    etNumber.setText(document.getString("Mobile Number"));
                                    etaddress.setText(document.getString("Reg No"));
                                    etlocation.setText(document.getString("Location"));
                                    etaboutyou.setText(document.getString("Aboutyou"));
                                    String img_url = (String) document.get("Image");
                                    Picasso.get().load(img_url).resize(150,150).centerCrop().into(image1);
                                }
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }

                    }
                });



        bottomBar = findViewById(R.id.bottomBar);
        bottomBar.setSelectedItemId(R.id.user);
        bottomBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.message:
                        startActivity(new Intent(getApplicationContext(), UserMessageActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), HomeScreen.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.user:
                        startActivity(new Intent(getApplicationContext(), UserEditProfile.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                Toast.makeText(UserEditProfile.this, item.getTitle(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void selectImage () {
        final CharSequence[] options = {"Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(UserEditProfile.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Choose from Gallery")) {
                    if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                        if(ContextCompat.checkSelfPermission(UserEditProfile.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                          Toast.makeText(UserEditProfile.this,"Permission Denied",Toast.LENGTH_SHORT ).show();
                            ActivityCompat.requestPermissions(UserEditProfile.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                        }
                        else{
                            ChooseImage();
                        }
                    }
                    else{
                        ChooseImage();
                    }
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void ChooseImage(){
        Toast.makeText(UserEditProfile.this, "Choose Image", Toast.LENGTH_SHORT).show();
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent , 2);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode ==2 && resultCode == RESULT_OK && data != null){
            imageUri = data.getData();
            Picasso.get().load(imageUri).resize(150,150).centerCrop().into(image1);
        }
    }



    private String getFileExtension(Uri mUri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));
    }
}

































