package com.example.hamarekisan;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class RegisterActivity extends AppCompatActivity {

    EditText etEmail, etNumber, etName, etPassword, ConfirmPassword, etaddress;
    Button signup;
    ProgressBar progressBar;
    private FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView bottom_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        signup = findViewById(R.id.signup);
        etaddress = findViewById(R.id.etaddress);
        progressBar = findViewById(R.id.progressBar);
        etEmail = findViewById(R.id.etPasswordEmail);
        etNumber = findViewById(R.id.etNumber);
        etName = findViewById(R.id.etName);
        etPassword = findViewById(R.id.etPassword);
        ConfirmPassword = findViewById(R.id.ConfirmPassword);
        mAuth = FirebaseAuth.getInstance();
        bottom_login = findViewById(R.id.login);
        bottom_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          String email = etEmail.getText().toString().trim();
                                          String name = etName.getText().toString().trim();
                                          String pswd = etPassword.getText().toString().trim();
                                          String confrm = ConfirmPassword.getText().toString().trim();
                                          String phnum = etNumber.getText().toString().trim();
                                          String address = etaddress.getText().toString().trim();
                                          if ((TextUtils.isEmpty(email)) || TextUtils.isEmpty(pswd) || TextUtils.isEmpty(phnum) || (TextUtils.isEmpty(address))) {
                                              Toast.makeText(getApplicationContext(), "You should enter all the fields properly", Toast.LENGTH_LONG).show();
                                              return;
                                          }
                                          if (pswd.length() < 6) {
                                              Toast.makeText(getApplicationContext(), "Password is too Short", Toast.LENGTH_LONG).show();
                                              return;
                                          }

                                          progressBar.setVisibility(View.VISIBLE);

                                          if (pswd.equals(confrm)) {
                                              mAuth.createUserWithEmailAndPassword(email, pswd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                  @Override
                                                  public void onComplete(@NonNull Task<AuthResult> task) {
                                                      if (task.isSuccessful()) {
                                                          //send verification link
                                                          FirebaseUser users = mAuth.getCurrentUser();
                                                          users.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                              @Override
                                                              public void onSuccess(Void aVoid) {
                                                                      Map<String, Object> user = new HashMap<>();
                                                                      user.put("Full Name", name);
                                                                      user.put("Address", address);
                                                                      user.put("Email Address", email);
                                                                      user.put("Mobile Number", phnum);
                                                                      user.put("Password", pswd);
                                                                      db.collection("users")
                                                                              .document(users.getUid())
                                                                              .set(user)
                                                                              .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                  @Override
                                                                                  public void onSuccess(Void aVoid) {
                                                                                      FirebaseAuth.getInstance().signOut();
                                                                                      Toast.makeText(getApplicationContext(), "Registration Successful. Please verify your Email.", Toast.LENGTH_SHORT).show();
                                                                                      Intent intent2 = new Intent(RegisterActivity.this, LoginActivity.class);
                                                                                      startActivity(intent2);
                                                                                  }
                                                                              })
                                                                              .addOnFailureListener(new OnFailureListener() {
                                                                                  @Override
                                                                                  public void onFailure(@NonNull Exception e) {
                                                                                      Toast.makeText(getApplicationContext(), "Registration Failed. Please try again", Toast.LENGTH_SHORT).show();
                                                                                  }
                                                                              });

                                                              }
                                                          }).addOnFailureListener(new OnFailureListener() {
                                                              @Override
                                                              public void onFailure(@NonNull Exception e) {
                                                                  Log.d("tag", "OnFailure: EMail not sent" + e.getMessage());
                                                              }
                                                          });


                                                      }
                                                      else {
                                                          Toast.makeText(getApplicationContext(), "Authentication Failed (Try with different Mail ID)", Toast.LENGTH_LONG).show();
                                                          return;
                                                      }
                                                  }
                                              });
                                          }
                                          else {
                                              Toast.makeText(getApplicationContext(), "Password Doesn't Match", Toast.LENGTH_LONG).show();
                                          }

                                      }
                                  }
        );
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }
}




















