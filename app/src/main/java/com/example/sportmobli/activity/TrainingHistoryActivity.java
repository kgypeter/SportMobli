package com.example.sportmobli.activity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sportmobli.R;
import com.example.sportmobli.model.DietHistory;
import com.example.sportmobli.util.AppPreferences;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TrainingHistoryActivity extends AppCompatActivity {


    private List<DietHistory> dietHistoryList;

    private FirebaseDatabase db;
    private DatabaseReference dietHistoryReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseDatabase.getInstance();
        dietHistoryReference = db.getReference("DietHistory");
        setContentView(R.layout.activity_training_history);
    }

    private void getData() {
        String currentUsername = AppPreferences.getUsername(this);
        dietHistoryReference.child(currentUsername).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dietHistoryList = extractDietHistory(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private List<DietHistory> extractDietHistory(DataSnapshot snapshot) {
        List<DietHistory> dietHistories = new ArrayList<>();
        for (DataSnapshot historySnapshot : snapshot.getChildren()) {
            DietHistory dietHistory = historySnapshot.getValue(DietHistory.class);
            dietHistories.add(dietHistory);
        }
        return dietHistories;
    }
}
