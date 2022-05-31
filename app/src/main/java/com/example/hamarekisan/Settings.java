package com.example.hamarekisan;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class Settings extends AppCompatActivity {
    TextView editProfile, AccountSettings,ManageAccounts, About, PrivacyPolicy, TermsConditions, Feedback, AddAccount, Logout;
    private FirebaseAuth firebaseAuth;
    String url="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        editProfile=findViewById(R.id.edit_profile);
        AccountSettings=findViewById(R.id.account_settings);
        ManageAccounts=findViewById(R.id.manage_account);
        About=findViewById(R.id.About);
        PrivacyPolicy=findViewById(R.id.privacypolicy);
        TermsConditions=findViewById(R.id.terms);
        Feedback=findViewById(R.id.feedback);
        ManageAccounts=findViewById(R.id.manage_account);
        AddAccount=findViewById(R.id.addaccount);
        Logout=findViewById(R.id.logout);

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
             public void onClick(View v) {
                Intent i = new Intent(Settings.this, UserEditProfile.class);
                startActivity(i);
              }
               }
        );

        About.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                 Intent i = new Intent(Settings.this, WebviewActivity.class);
                 url="https://aashikamaddineni.blogspot.com/2022/04/about-hamarekisan-app.html".toString();
                 i.putExtra("url",url);
                 startActivity(i);
                   }
             }
        );

        PrivacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Settings.this, WebviewActivity.class);
                url="https://aashikamaddineni.blogspot.com/2022/04/hamarekisan-app.html".toString();
                i.putExtra("url",url);
                startActivity(i);
            }
        });

        TermsConditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Settings.this, WebviewActivity.class);
                url="https://aashikamaddineni.blogspot.com/2022/04/hamarekisan-app_2.html".toString();
                i.putExtra("url",url);
                startActivity(i);
            }
        });

        Feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Settings.this, FeedbackActivity.class);
                startActivity(i);
            }
        });
        AddAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Settings.this, RegisterActivity.class);
                startActivity(i);
            }
        });

        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.getInstance().signOut();
                Intent i = new Intent(Settings.this, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                getSharedPreferences("preferenceName",0).edit().clear().commit();
            }
        });


    }

}

