package com.example.hamarekisan;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

public class FeedbackActivity extends AppCompatActivity {
    TextView tvFeedback;
    RatingBar rbStars;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        tvFeedback = findViewById(R.id.tvFeedback);
        rbStars = findViewById(R.id.rbStars);

        rbStars.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if(rating==0){
                    tvFeedback.setVisibility(View.INVISIBLE);
                }
                if(rating==1)
                {
                    tvFeedback.setVisibility(View.VISIBLE);
                    tvFeedback.setText("Very Dissatisfied");
                }
                else if(rating==2)
                {
                    tvFeedback.setVisibility(View.VISIBLE);
                    tvFeedback.setText("Dissatisfied");
                }
                else if(rating==3)
                {
                    tvFeedback.setVisibility(View.VISIBLE);
                    tvFeedback.setText("OK");
                }
                else if(rating==4)
                {
                    tvFeedback.setVisibility(View.VISIBLE);
                    tvFeedback.setText("Satisfied");
                }
                else if(rating==5)
                {
                    tvFeedback.setVisibility(View.VISIBLE);
                    tvFeedback.setText("Very Satisfied");
                }
                else
                {

                }
            }
        });
    }
}