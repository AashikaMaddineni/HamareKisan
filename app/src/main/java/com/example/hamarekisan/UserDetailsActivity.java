package com.example.hamarekisan;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;


public class UserDetailsActivity extends AppCompatActivity {
    TextView etEmail, etNumber, etName, etaddress, etaboutyou;
    String userEmail;
    ImageView image;
    private FirebaseAuth firebaseAuth;
    BottomNavigationView bottomBar;
    FirebaseFirestore db;
    public static final String TAG = "";
    private static int RESULT_LOAD_IMAGE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details_page);
        etEmail = findViewById(R.id.etPasswordEmail);
        etNumber = findViewById(R.id.etNumber);
        etaddress = findViewById(R.id.etaddress);
        etName = findViewById(R.id.etName);
        etaboutyou = findViewById(R.id.etAboutyou);
        ImageView image1 = findViewById(R.id.img);


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
                                if (document.getString("Email Address").equals(userEmail)) {
                                    etEmail.setText(document.getString("Email Address"));
                                    etName.setText(document.getString("Full Name"));
                                    etNumber.setText(document.getString("Mobile Number"));
                                    etaddress.setText(document.getString("Address"));
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
                        return true;
                }
                Toast.makeText(UserDetailsActivity.this, item.getTitle(), Toast.LENGTH_SHORT).show();
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
            Toast.makeText(UserDetailsActivity.this, item.getTitle(), Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), Settings.class));
        }
        return true;
    }

    private void selectImage () {
        final CharSequence[] options = {"Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(UserDetailsActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, RESULT_LOAD_IMAGE);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Bundle bundle = data.getExtras();
            Bitmap bitmapImage = (Bitmap) bundle.get("data");
            image.setImageBitmap(bitmapImage);
        }
    }


}
