package com.example.sportmobli.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sportmobli.R;
import com.example.sportmobli.adapter.DietHistoryRecyclerAdapter;
import com.example.sportmobli.model.DietHistoryDisplay;
import com.example.sportmobli.util.AppPreferences;
import com.example.sportmobli.util.DateUtil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DietHistoryActivity extends AppCompatActivity {

    List<DietHistoryDisplay> dietHistoryList;
    private DatabaseReference dietHistoryReference;
    private DietHistoryRecyclerAdapter adapter;

    public DietHistoryActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_history);

        dietHistoryReference = FirebaseDatabase.getInstance().getReference("DietHistory");


        fetchDietHistoryFromFirebase();
    }

    private void fetchDietHistoryFromFirebase() {
        String currentUsername = AppPreferences.getUsername(this);

        dietHistoryReference.child(currentUsername).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dietHistoryList = extractHistoryList(snapshot);
                updateAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DietHistoryActivity.this, "Failed to fetch diet history: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private List<DietHistoryDisplay> extractHistoryList(DataSnapshot snapshot) {
        ArrayList<DietHistoryDisplay> dietHistoryList = new ArrayList<>();

        for (DataSnapshot historySnapshot : snapshot.getChildren()) {

            Integer protein = Math.round(historySnapshot.child("protein").getValue(Float.class));
            Integer carbohydrate = Math.round(historySnapshot.child("carbohydrates").getValue(Float.class));
            Integer fats = Math.round(historySnapshot.child("fats").getValue(Float.class));
            Integer calories = Math.round(historySnapshot.child("calories").getValue(Float.class));

            String date = DateUtil.extractDateFromSnapshot(historySnapshot, "dateAdded");

            DietHistoryDisplay dietHistoryDisplay = new DietHistoryDisplay();
            dietHistoryDisplay.setDateAdded(date);
            dietHistoryDisplay.setCalories(calories);
            dietHistoryDisplay.setProtein(protein);
            dietHistoryDisplay.setFats(fats);
            dietHistoryDisplay.setCarbohydrates(carbohydrate);

            dietHistoryList.add(dietHistoryDisplay);
        }
        return dietHistoryList;
    }

    private void updateAdapter() {

        RecyclerView recyclerView = findViewById(R.id.recyclerViewDietHistory);
        adapter = new DietHistoryRecyclerAdapter(dietHistoryList);

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }
}
