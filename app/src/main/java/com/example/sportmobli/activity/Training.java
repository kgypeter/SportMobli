package com.example.sportmobli.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sportmobli.R;
import com.example.sportmobli.adapter.TrainingRecyclerAdapter;
import com.example.sportmobli.model.Diet;
import com.example.sportmobli.model.Exercise;
import com.example.sportmobli.model.TrainingSession;
import com.example.sportmobli.util.AppPreferences;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


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

    private List<TrainingSession> privateTrainingSessions;
    private List<TrainingSession> publicTrainingSessions;
    private RecyclerView recyclerView;
    private EditText searchEditText;
    private TrainingRecyclerAdapter adapter;

    private FirebaseDatabase db;
    private DatabaseReference trainingSessionReference;

    private SharedPreferences sharedPreferences;

    public Training() {
    }

    public Training(String trainingName, List<TrainingSession> trainingSessions) {
        this.name = trainingName;
        this.privateTrainingSessions = trainingSessions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TrainingSession> getExercises() {
        return privateTrainingSessions;
    }

    public void setExercises(List<TrainingSession> trainingSessions) {
        this.privateTrainingSessions = trainingSessions;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        privateTrainingSessions = new ArrayList<>();
        publicTrainingSessions = new ArrayList<>();
        db = FirebaseDatabase.getInstance();
        trainingSessionReference = db.getReference("TrainingSession");
        sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);
        getSessionsFromDatabase();
//        initializeTrainingSessions();

        Button lolButton = findViewById(R.id.button3);
        Button dietButton = findViewById(R.id.button5);
        Button userProfileButton = findViewById(R.id.button6);
        Button trackingButton = findViewById(R.id.button7);
        ImageView addButton = findViewById(R.id.addButton);

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

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAlertDialog();
            }
        });


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

    private void openAlertDialog() {
        // Create an AlertDialog.Builder instance
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Set the dialog title, message, and other properties
        builder.setTitle("Alert Dialog")
                .setMessage("This is a simple alert dialog.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        // Create and show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void getSessionsFromDatabase() {

        trainingSessionReference.child("public").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                publicTrainingSessions = extractTrainingSessions(snapshot);
                getPrivateSessions();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void getPrivateSessions() {
        String currentUsername = AppPreferences.getUsername(Training.this);
        trainingSessionReference.child(currentUsername).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                privateTrainingSessions = extractTrainingSessions(snapshot);
                startAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void startAdapter() {
        List<TrainingSession> trainingSessions = privateTrainingSessions;
        trainingSessions.addAll(publicTrainingSessions);
        adapter = new TrainingRecyclerAdapter(trainingSessions);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(Training.this));
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(Training.this);
    }

    public List<TrainingSession> extractTrainingSessions(DataSnapshot snapshot) {
        List<TrainingSession> trainingSessions = new ArrayList<>();
        for (DataSnapshot sessionSnapshot : snapshot.getChildren()) {
            TrainingSession trainingSession = new TrainingSession();
            trainingSession.setName(sessionSnapshot.child("name").getValue(String.class));

            List<Exercise> exercises = new ArrayList<>();
            for (DataSnapshot exerciseSnapshot : sessionSnapshot.child("exercises").getChildren()) {
                Exercise exercise = exerciseSnapshot.getValue(Exercise.class);
                exercises.add(exercise);
            }
            trainingSession.setExercises(exercises);
            trainingSessions.add(trainingSession);
        }
        return trainingSessions;
    }

    // Populate training with sessions and exercises for each session
    @SuppressLint("NotifyDataSetChanged")
    private void initializeTrainingSessions() {
        List<Exercise> session1Exercises = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            session1Exercises.add(new Exercise("Exercise " + i, 20, 30));
        }

        TrainingSession session1 = new TrainingSession("Session 1");
        session1.setExercises(session1Exercises);

        privateTrainingSessions.add(session1);

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
            privateTrainingSessions.add(session);
        }

        Random random = new Random();
        List<Exercise> session11Exercises = new ArrayList<>();
        for (int exerciseNumber = 1; exerciseNumber <= 10; exerciseNumber++) {
            int duration = random.nextInt(4) + 3; // Generates random duration between 3 and 6 seconds
            int rest = random.nextInt(4) + 3; // Generates random rest time between 3 and 6 seconds

            session11Exercises.add(new Exercise("Exercise " + exerciseNumber, duration, rest));
        }
        TrainingSession session11 = new TrainingSession("Session 11");
        session11.setExercises(session11Exercises);
        privateTrainingSessions.add(session11);
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClick(int position) {
        TrainingSession clickedSession = privateTrainingSessions.get(position);

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



