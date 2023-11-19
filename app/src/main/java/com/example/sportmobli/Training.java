package com.example.sportmobli;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


// todo - implement the functionality of crud operations and make data persistent in firebase DB
//  - make training session list items clickable, create the exercise class which is going to be the list in the given session
public class Training extends AppCompatActivity {

    private String name;
    private List<TrainingSession> trainingSessions;

    private RecyclerView recyclerView;

    private EditText searchEditText;

    private TrainingRecyclerAdapter adapter;

    public Training() {
    }

    public Training(String trainingName, List<TrainingSession> trainingSessions) {
        this.name = trainingName;
        this.trainingSessions = trainingSessions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public List<TrainingSession> getExercises() {
        return trainingSessions;
    }

    public void setExercises(List<TrainingSession> trainingSessions) {
        this.trainingSessions = trainingSessions;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);

        trainingSessions = new ArrayList<>(); // Initialize the list of training sessions

        // Locate buttons and set onClickListeners
        Button lolButton = findViewById(R.id.button3);
        Button dietButton = findViewById(R.id.button5);
        Button userProfileButton = findViewById(R.id.button6);
        Button trackingButton = findViewById(R.id.button7);

        // Set up onClickListeners for buttons
        lolButton.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), Home.class)));
        dietButton.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), Diet.class)));
        userProfileButton.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), UserProfile.class)));
        trackingButton.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), Tracking.class)));

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        adapter = new TrainingRecyclerAdapter(trainingSessions);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Initialize search EditText
        searchEditText = findViewById(R.id.searchEditText);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                filterTrainingSessions(s.toString());
            }
        });

        // Add some initial data for display
        addSampleExercises();
    }

    private void filterTrainingSessions(String query) {
        adapter.filterList(query); // Call the filterList method of the adapter
    }

    // Add sample exercises for testing
    @SuppressLint("NotifyDataSetChanged")
    private void addSampleExercises() {
        TrainingSession session1 = new TrainingSession("Session 1", 5.5f, 60);
        TrainingSession session2 = new TrainingSession("Session 2", 8, 45);
        TrainingSession session3 = new TrainingSession("Session 3", 15, 90);
        TrainingSession session4 = new TrainingSession("Session 4",  5.5f, 70);
        TrainingSession session5 = new TrainingSession("Session 5", 20, 80);
        TrainingSession session6 = new TrainingSession("Session 6", 12.5f, 55);
        TrainingSession session7 = new TrainingSession("Session 7", 45, 100);

        // Add them to the RecyclerView
        addExercise(session1);
        addExercise(session2);
        addExercise(session3);
        addExercise(session4);
        addExercise(session5);
        addExercise(session6);
        addExercise(session7);

        // Notify the adapter after adding exercises
        adapter.notifyDataSetChanged();

        // Log the size of trainingSessions after adding data
        Log.d("Training", "Number of sessions: " + trainingSessions.size());
    }

    // add a new TrainingSession to the list
    @SuppressLint("NotifyDataSetChanged")
    public void addExercise(TrainingSession trainingSession) {
        trainingSessions.add(trainingSession);
        adapter.filterList(""); // Refresh the filtered list
    }

}

