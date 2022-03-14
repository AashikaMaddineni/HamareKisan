package com.example.hamarekisan;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;


public class LoginActivity extends AppCompatActivity {
   private  static final String TAG="GoogleActivity";
   private static final int RC_SIGN_IN=9001;

    EditText editText1, editText2;
    SignInButton signInButton;
    GoogleSignInClient mGoogleSignInClient;
    Button button;
    TextView signup, forgot_password;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        button = findViewById(R.id.btn_login);
        forgot_password = findViewById(R.id.txtForgotPassword);
        editText1 = findViewById(R.id.etPasswordEmail);
        editText2 = findViewById(R.id.etPassword);
        signup = findViewById(R.id.txtSignUp);
        signInButton = findViewById(R.id.gsignin);

        firebaseAuth = FirebaseAuth.getInstance();


        signup.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                                          startActivity(i);
                                      }
                                  }

        );
        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, forgot_password.class);
                startActivity(i);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editText1.getText().toString().trim();
                String password = editText2.getText().toString().trim();
                if ((TextUtils.isEmpty(email)) || TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginActivity.this, "You should enter all the fields properly", Toast.LENGTH_LONG).show();
                    return;
                }
                firebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                   @Override
                                                   public void onComplete(@NonNull Task<AuthResult> task) {
                                                       if (task.isSuccessful()) {
                                                           final FirebaseUser users = firebaseAuth.getCurrentUser();
                                                           if (users.isEmailVerified()) {
                                                               Intent intent2 = new Intent(LoginActivity.this, HomeScreen.class);
                                                               startActivity(intent2);
                                                           } else {
                                                               final AlertDialog.Builder ad = new AlertDialog.Builder(LoginActivity.this);
                                                               ad.setTitle("Account Not Verified");
                                                               ad.setMessage("Please Verify your account using the verification link sent to your registered Email ID. Click on Resend Verification Link to get a new link.");
                                                               ad.setIcon(R.drawable.ic_error_black_24dp);
                                                               ad.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                   @Override
                                                                   public void onClick(DialogInterface dialogInterface, int i) {

                                                                   }
                                                               });
                                                               ad.setNegativeButton("Resend Verification Link", new DialogInterface.OnClickListener() {
                                                                   @Override
                                                                   public void onClick(DialogInterface dialogInterface, int i) {
                                                                       users.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {

                                                                           @Override
                                                                           public void onSuccess(Void aVoid) {
                                                                               Toast.makeText(LoginActivity.this, "Verification Link Sent", Toast.LENGTH_LONG).show();
                                                                           }
                                                                       }).addOnFailureListener(new OnFailureListener() {
                                                                           @Override
                                                                           public void onFailure(@NonNull Exception e) {
                                                                               Toast.makeText(LoginActivity.this, "Verification Link not Sent. Try Again!!", Toast.LENGTH_LONG).show();

                                                                           }
                                                                       });
                                                                   }
                                                               });
                                                               ad.show();
                                                               Toast.makeText(LoginActivity.this,"Your account is not Verified.",Toast.LENGTH_LONG).show();
                                                           }
                                                       }
                                                       else {
                                                           Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                       }
                                                   }
                                               }
                        );
            }
        });


        processrequest();
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    private void processrequest() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle"+account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                   Log.w(TAG, "google signin failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "SignInwithCredential:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), UserDetailsActivity.class));
                            finish();
                        } else {
                            Log.w(TAG, "SignInwithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Something Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
