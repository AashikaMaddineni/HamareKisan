package com.example.hamarekisan;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.Query;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UserMessageActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    BottomNavigationView bottomBar;
    LinearLayoutManager mLinearLayoutManager;
    RecyclerView mRecyclerView;
    FirebaseUser user;
    FirebaseFirestore db;
    FirestoreRecyclerAdapter<Prediction, ViewHolder> firestoreRecyclerAdapter;
    FirestoreRecyclerOptions<Prediction> options;
    String userId;
    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_message_page);
        mLinearLayoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,true);
        mLinearLayoutManager.setStackFromEnd(true);
        mRecyclerView = findViewById(R.id.recycler_view);
        db= FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();
        date = new SimpleDateFormat("dd-M-yyyy hh:mm:ss", Locale.getDefault()).format(new Date());
        showData();
        bottomBar = findViewById(R.id.bottomBar);
        bottomBar.setSelectedItemId(R.id.message);
        bottomBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.message:
                        return true;

                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), HomeScreen.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.user:
                        startActivity(new Intent(getApplicationContext(), UserDetailsActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }

                Toast.makeText(UserMessageActivity.this, item.getTitle(), Toast.LENGTH_SHORT).show();
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
            Toast.makeText(UserMessageActivity.this, item.getTitle(), Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), Settings.class));
        }
        return true;
    }


    private void showData() {
        Query query =db.collection("users").document(userId).collection("PredictionHistory");
        options = new FirestoreRecyclerOptions.Builder<Prediction>().setQuery(query, Prediction.class).build();
        firestoreRecyclerAdapter = new FirestoreRecyclerAdapter<Prediction, ViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Prediction model) {
                holder.setDetails(getApplicationContext(),  model.getPrediction(), model.getImage(), model.getConfidence(), model.getDate(), model.getUid());
            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_view, parent, false);
                ViewHolder viewHolder = new ViewHolder(itemView);
                viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Toast.makeText(UserMessageActivity.this, "hello", Toast.LENGTH_SHORT);
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                        Toast.makeText(UserMessageActivity.this, "Long Click", Toast.LENGTH_SHORT);
                    }
                });
                return viewHolder;
            }
        };
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        firestoreRecyclerAdapter.startListening();
        mRecyclerView.setAdapter(firestoreRecyclerAdapter);
    }

    protected void onStart() {
        super.onStart();
        if(firestoreRecyclerAdapter!=null){
            firestoreRecyclerAdapter.startListening();
        }
    }
}

