package com.example.sportmobli.activity;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sportmobli.R;
import com.example.sportmobli.adapter.DietHistoryRecyclerAdapter;
import com.example.sportmobli.model.DietHistory;
import com.example.sportmobli.util.AppPreferences;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DietHistoryActivity extends AppCompatActivity {

    private DatabaseReference dietHistoryReference;
    private DietHistoryRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_history);

        dietHistoryReference = FirebaseDatabase.getInstance().getReference("DietHistory");

        RecyclerView recyclerView = findViewById(R.id.recyclerViewDietHistory);
        adapter = new DietHistoryRecyclerAdapter(new ArrayList<>());

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        fetchDietHistoryFromFirebase();
    }

    private void fetchDietHistoryFromFirebase() {
        String currentUsername = AppPreferences.getUsername(this);

        dietHistoryReference.child(currentUsername).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<DietHistory> dietHistoryList = new ArrayList<>();

                for (DataSnapshot historySnapshot : snapshot.getChildren()) {
                    DietHistory dietHistory = historySnapshot.getValue(DietHistory.class);
                    dietHistoryList.add(dietHistory);
                }

                updateAdapter(dietHistoryList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }

    private void updateAdapter(ArrayList<DietHistory> dietHistoryList) {
        // todo
//        adapter.setDietHistoryList(dietHistoryList);
    }
}
