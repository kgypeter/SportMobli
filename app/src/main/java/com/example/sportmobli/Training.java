package com.example.sportmobli;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
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
//  - hide delete and edit buttons when there is no search result for a session

/**
 * Parcelable is an Android interface used for efficient serialization and deserialization
 * of objects. It's optimized for passing complex data structures between components
 * (like activities or services). Implementing Parcelable in a class allows objects
 * of that class to be packed into Intents and transferred between different parts
 * of an Android application.
 */
public class Training extends AppCompatActivity implements TrainingRecyclerAdapter.OnItemClickListener {

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

        trainingSessions = new ArrayList<>();
        initializeTrainingSessions();

        Button lolButton = findViewById(R.id.button3);
        Button dietButton = findViewById(R.id.button5);
        Button userProfileButton = findViewById(R.id.button6);
        Button trackingButton = findViewById(R.id.button7);

        lolButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Home.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });

        dietButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Diet.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });

        userProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UserProfile.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });

        trackingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Tracking.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });


        adapter = new TrainingRecyclerAdapter(trainingSessions);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);

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
    }

    @SuppressLint("NotifyDataSetChanged")
    private void initializeTrainingSessions() {
        List<Exercise> session1Exercises = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            session1Exercises.add(new Exercise("Exercise " + i, 20, 30));
        }

        TrainingSession session1 = new TrainingSession("Session 1");
        session1.setExercises(session1Exercises);

        trainingSessions.add(session1);

        for (int sessionNumber = 2; sessionNumber <= 6; sessionNumber++) {
            List<Exercise> sessionExercises = new ArrayList<>();
            for (int exerciseNumber = 1; exerciseNumber <= 10; exerciseNumber++) {
                if (sessionNumber % 2 == 0) {
                    sessionExercises.add(new Exercise("Exercise " + exerciseNumber, 60, 10));
                } else {
                    sessionExercises.add(new Exercise("Exercise " + exerciseNumber, 35, 10));

                }
            }
            TrainingSession session = new TrainingSession("Session " + sessionNumber);
            session.setExercises(sessionExercises);
            trainingSessions.add(session);
        }

        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClick(int position) {
        TrainingSession clickedSession = trainingSessions.get(position);

        Intent intent = new Intent(this, ExerciseListActivity.class);
        intent.putParcelableArrayListExtra("exercises", (ArrayList<? extends Parcelable>) clickedSession.getExercises());

        // Add transition animation
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    private void filterTrainingSessions(String query) {
        adapter.filterList(query);
    }
}



