package com.example.sportmobli.activity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sportmobli.R;
import com.example.sportmobli.adapter.TrainingHistoryRecyclerAdapter;
import com.example.sportmobli.model.TrainingHistoryDisplay;
import com.example.sportmobli.util.AppPreferences;
import com.example.sportmobli.util.DateUtil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TrainingHistoryActivity extends AppCompatActivity {


    private List<TrainingHistoryDisplay> trainingHistoryList;

    private FirebaseDatabase db;
    private DatabaseReference dietHistoryReference;
    private TrainingHistoryRecyclerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseDatabase.getInstance();
        dietHistoryReference = db.getReference("TrainingHistory");
        setContentView(R.layout.activity_training_history);
    }

    private void getData() {
        String currentUsername = AppPreferences.getUsername(this);
        dietHistoryReference.child(currentUsername).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                trainingHistoryList = extractTrainingHistory(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private List<TrainingHistoryDisplay> extractTrainingHistory(DataSnapshot snapshot) {
        List<TrainingHistoryDisplay> trainingHistoryDisplays = new ArrayList<>();
        for (DataSnapshot historySnapshot : snapshot.getChildren()) {
            String date = DateUtil.extractDateFromSnapshot(historySnapshot);
            String sessionName = snapshot.child("sessionName").getValue(String.class);
            String totalTime = snapshot.child("totalTime").getValue(String.class);
            TrainingHistoryDisplay trainingHistoryDisplay = new TrainingHistoryDisplay();
            trainingHistoryDisplay.setAddedDate(date);
            trainingHistoryDisplay.setSessionName(sessionName);
            trainingHistoryDisplay.setTotalTime(totalTime);
            trainingHistoryDisplays.add(trainingHistoryDisplay);
        }
        return trainingHistoryDisplays;
    }

    private void startAdapter() {
        RecyclerView recyclerView = findViewById(R.id.recyclerViewTrainingHistory);
        adapter = new TrainingHistoryRecyclerAdapter(trainingHistoryList);

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

    }
}
