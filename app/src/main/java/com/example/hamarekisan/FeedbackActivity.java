package com.example.hamarekisan;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.HashMap;
import java.util.Map;


public class FeedbackActivity extends AppCompatActivity {

    TextView feedback;
    RatingBar rbStars;
    Button submit;
    float rate=0;
    FirebaseFirestore db;
    private FirebaseAuth mAuth;
    Map<String,Object> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        feedback = findViewById(R.id.tvFeedback);
        rbStars = findViewById(R.id.rbStars);
        submit=findViewById(R.id.btnSend);
        mAuth = FirebaseAuth.getInstance();

         map=new HashMap<>();

        rbStars.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                rate=rating;
                if(rating==0){
                    feedback.setVisibility(View.INVISIBLE);
                }
                if(rating==1 && rating==1.5)
                {
                    feedback.setVisibility(View.VISIBLE);
                    feedback.setText("Very Dissatisfied");
                }
                else if(rating==2 && rating==2.5)
                {
                    feedback.setVisibility(View.VISIBLE);
                    feedback.setText("Dissatisfied");
                }
                else if(rating==3 && rating==3.5)
                {
                    feedback.setVisibility(View.VISIBLE);
                    feedback.setText("OK");
                }
                else if(rating==4 && rating==4.5)
                {
                    feedback.setVisibility(View.VISIBLE);
                    feedback.setText("Satisfied");
                }
                else if(rating==5)
                {
                    feedback.setVisibility(View.VISIBLE);
                    feedback.setText("Very Satisfied");
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(feedback.toString().isEmpty()==false && rate!=0){
                    upload(); }
                else {
                    feedback.setError("please enter feedback", Drawable.createFromPath("ic_error_black_24dp"));
                }
            }
        });

    }

    private void upload(){
        FirebaseUser users = mAuth.getCurrentUser();
        Map<String, Object> fb = new HashMap<>();
        fb.put("Email", users.getEmail());
        fb.put("Rating", rate);
        fb.put("Feedback", feedback);
        fb.put("Created", Timestamp.now());
        db.collection("feedback").document(users.getUid()).set(fb);
        Toast.makeText(getApplicationContext(), "Upload success", Toast.LENGTH_LONG).show();
    }
}


