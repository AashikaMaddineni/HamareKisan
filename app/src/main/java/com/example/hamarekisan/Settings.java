package com.example.hamarekisan;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class Settings extends AppCompatActivity {
    TextView editProfile, AccountSettings, About, Feedback, ManageAccounts, Logout;
    private FirebaseAuth firebaseAuth;
    BottomNavigationView bottomBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        editProfile=findViewById(R.id.edit_profile);
        AccountSettings=findViewById(R.id.account_settings);
        About=findViewById(R.id.About);
        Feedback=findViewById(R.id.feedback);
        ManageAccounts=findViewById(R.id.manage_account);
        Logout=findViewById(R.id.logout);
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
             public void onClick(View v) {
                Intent i = new Intent(Settings.this, UserDetailsActivity.class);
                startActivity(i);
              }
               }
        );
        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.getInstance().signOut();
                Intent i = new Intent(Settings.this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                getSharedPreferences("preferenceName",0).edit().clear().commit();
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
                        startActivity(new Intent(getApplicationContext(), UserDetailsActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                Toast.makeText(Settings.this, item.getTitle(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

}

