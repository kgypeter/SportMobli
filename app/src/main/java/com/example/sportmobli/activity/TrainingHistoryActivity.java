package com.example.sportmobli.activity;

import android.os.Bundle;
import android.widget.Toast;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                startAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(TrainingHistoryActivity.this, "Failed to retrieve data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private List<TrainingHistoryDisplay> extractTrainingHistory(DataSnapshot snapshot) {
        List<TrainingHistoryDisplay> trainingHistoryDisplays = new ArrayList<>();
        for (DataSnapshot historySnapshot : snapshot.getChildren()) {
            String date = DateUtil.extractDateFromSnapshot(historySnapshot, "addedDate");
            String sessionName = historySnapshot.child("sessionName").getValue(String.class);
            String totalTime = historySnapshot.child("totalTime").getValue(String.class);

            TrainingHistoryDisplay trainingHistoryDisplay = new TrainingHistoryDisplay();
            Map<String, Double> hrSeries = extractHrSeries(historySnapshot.child("hrHistory"));
            trainingHistoryDisplay.setAddedDate(date);
            trainingHistoryDisplay.setSessionName(sessionName);
            trainingHistoryDisplay.setTotalTime(totalTime);
            trainingHistoryDisplay.setHrHistory(hrSeries);
            trainingHistoryDisplays.add(trainingHistoryDisplay);
        }
        return trainingHistoryDisplays;
    }

    private Map<String, Double> extractHrSeries(DataSnapshot snapshot) {
        Map<String, Double> hrHistory = new HashMap<>();
        for (DataSnapshot entrySnapshot : snapshot.getChildren()) {
            String key = entrySnapshot.getKey();
            Double value = entrySnapshot.getValue(Double.class);
            hrHistory.put(key, value);
        }
        return hrHistory;
    }

    private void startAdapter() {
        RecyclerView recyclerView = findViewById(R.id.recyclerViewTrainingHistory);
        adapter = new TrainingHistoryRecyclerAdapter(trainingHistoryList);

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

    }
}

