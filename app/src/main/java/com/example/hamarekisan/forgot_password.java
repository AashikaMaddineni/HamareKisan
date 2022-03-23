package com.example.hamarekisan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class forgot_password extends AppCompatActivity {
    private EditText passwordEmail;
    private Button resetPassword;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pswd);
        passwordEmail= findViewById(R.id.etPasswordEmail);
        resetPassword= findViewById(R.id.btnPasswordReset);
        firebaseAuth=FirebaseAuth.getInstance();

        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String useremail=passwordEmail.getText().toString().trim();
                if(useremail.equals("")){
                    Toast.makeText(forgot_password.this,"Please enter your registered Email ID",Toast.LENGTH_SHORT).show();
                }
                else{
                    firebaseAuth.sendPasswordResetEmail(useremail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(forgot_password.this,"Password reset email sent",Toast.LENGTH_LONG).show();
                                finish();
                                startActivity(new Intent(forgot_password.this,LoginActivity .class));
                            }
                            else{
                                Toast.makeText(forgot_password.this,"Please Enter the Correct Email ID",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}
