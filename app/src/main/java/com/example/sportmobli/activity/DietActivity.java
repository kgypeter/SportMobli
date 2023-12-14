package com.example.sportmobli.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sportmobli.R;
import com.example.sportmobli.adapter.DietRecyclerAdapter;
import com.example.sportmobli.dialogs.ConfirmationDialog;
import com.example.sportmobli.model.DietHistory;
import com.example.sportmobli.model.Victual;
import com.example.sportmobli.util.AppPreferences;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// todo - implement the functionality of crud operations and make data persistent in firebase DB
//  - hide delete and edit buttons when there is no search result for a session
public class DietActivity extends AppCompatActivity {

    private DatabaseReference victualReference;
    private DatabaseReference dietHistoryReference;
    private List<Victual> victualList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        victualReference = db.getReference("victuals");
        dietHistoryReference = db.getReference("DietHistory");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet);

        Log.d("DietActivity", "onCreate: Diet activity started");

        try {
            Button lolButton = findViewById(R.id.button3);
            Button trainingButton = findViewById(R.id.button4);
            Button userProfileButton = findViewById(R.id.button6);
            Button trackingButton = findViewById(R.id.button7);
            Button saveMealButton = findViewById(R.id.saveMealButton);

            trainingButton.setOnClickListener(view -> {
                Intent intent = new Intent(getApplicationContext(), Training.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            });

            userProfileButton.setOnClickListener(view -> {
                Intent intent = new Intent(getApplicationContext(), UserProfile.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            });

            trackingButton.setOnClickListener(view -> {
                Intent intent = new Intent(getApplicationContext(), Tracking.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            });

            lolButton.setOnClickListener(view -> {
                Intent intent = new Intent(getApplicationContext(), Home.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            });
            saveMealButton.setOnClickListener(view -> ConfirmationDialog.show(this, "Add meal to your history.", "Make sure all macros are correct!", this::saveMeal));

            getFoodList();

        } catch (Exception e) {
            Log.e("DietActivity", "Error in onCreate: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private void getFoodList() {
        victualReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                victualList = extractFoodList(snapshot);
                startAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void saveMeal() {
        float totalCarbs = 0;
        float totalProtein = 0;
        float totalFats = 0;
        float totalCalories = 0;
        for (Victual victual : victualList) {
            totalCalories += victual.getCalories() * victual.getTotalGrams() / 100;
            totalCarbs += victual.getCarbohydrates() * victual.getTotalGrams() / 100;
            totalProtein += victual.getProtein() * victual.getTotalGrams() / 100;
            totalFats += victual.getFats() * victual.getTotalGrams() / 100;
            victual.setTotalGrams(0);
        }
        String currentUsername = AppPreferences.getUsername(this);
        LocalDateTime addDate = LocalDateTime.now();

        DietHistory dietHistory = new DietHistory();
        dietHistory.setCalories(totalCalories);
        dietHistory.setCarbohydrates(totalCarbs);
        dietHistory.setFats(totalFats);
        dietHistory.setProtein(totalProtein);
        dietHistory.setDateAdded(addDate);
        String uuid = UUID.randomUUID().toString();
        dietHistoryReference.child(currentUsername).child(uuid).setValue(dietHistory)
                .addOnSuccessListener(task -> Toast.makeText(this, "Entry saved successfully", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(task -> Toast.makeText(this, "Error saving diet entry!", Toast.LENGTH_SHORT).show());
    }

    private List<Victual> extractFoodList(DataSnapshot snapshot) {
        List<Victual> victualList = new ArrayList<>();
        for (DataSnapshot victualSnapshot : snapshot.getChildren()) {
            Victual victual = victualSnapshot.getValue(Victual.class);
            victualList.add(victual);
        }
        return victualList;
    }

    public void startAdapter() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        Log.d("DietActivity", "RecyclerView is not null");
        TextView totalGramsTextView = findViewById(R.id.totalGramsTextView);
        TextView totalCaloriesTextView = findViewById(R.id.totalCaloriesTextView);
        TextView totalProteinTextView = findViewById(R.id.totalProteinTextView);
        TextView totalCarbsTextView = findViewById(R.id.totalCarbsTextView);
        TextView totalFatsTextView = findViewById(R.id.totalFatsTextView);

        DietRecyclerAdapter adapter = new DietRecyclerAdapter(victualList, totalGramsTextView,
                totalCaloriesTextView, totalProteinTextView, totalCarbsTextView, totalFatsTextView,
                () -> getFoodList());

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        // Set up the search functionality
        EditText searchEditText = findViewById(R.id.searchEditText);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Check if the input is empty
                if (charSequence.toString().isEmpty()) {
                    // If empty, you can reset the filter or perform other actions
                    adapter.resetFilter();
                } else {
                    // Only filter if the query is not empty
                    adapter.filterList(charSequence.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Check if the input is empty
                if (editable.toString().isEmpty()) {
                    // If empty, reset the filter
                    adapter.resetFilter();
                } else {
                    // If not empty, filter the list based on the input
                    adapter.filterList(editable.toString());
                }
            }
        });

    }


}
