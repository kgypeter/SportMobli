package com.example.sportmobli.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sportmobli.R;
import com.example.sportmobli.adapter.TrainingRecyclerAdapter;
import com.example.sportmobli.dialogs.AddTrainingDialog;
import com.example.sportmobli.model.Exercise;
import com.example.sportmobli.model.TrainingSession;
import com.example.sportmobli.model.TrainingSessionDTO;
import com.example.sportmobli.util.AppPreferences;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


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
    private TrainingRecyclerAdapter adapter;

    private DatabaseReference trainingSessionReference;


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

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        getSessionsFromDatabase();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        privateTrainingSessions = new ArrayList<>();
        publicTrainingSessions = new ArrayList<>();
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        trainingSessionReference = db.getReference("TrainingSession");


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);
        getSessionsFromDatabase();

        Button lolButton = findViewById(R.id.button3);
        Button dietButton = findViewById(R.id.button5);
        Button userProfileButton = findViewById(R.id.button6);
        Button trackingButton = findViewById(R.id.button7);
        ImageView addButton = findViewById(R.id.addButton);

        lolButton.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), Home.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        });

        dietButton.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), DietActivity.class);
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

        addButton.setOnClickListener(view -> openAlertDialog());


        EditText searchEditText = findViewById(R.id.searchEditText);
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

        AddTrainingDialog.show(this, "Add a new session", this::addSessionToDatabase);
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

    public void addSessionToDatabase(TrainingSessionDTO newTrainingSession) {
        String currentUsername = AppPreferences.getUsername(this);
        trainingSessionReference.child(currentUsername).child(newTrainingSession.getName()).setValue(newTrainingSession).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Toast.makeText(this, "Error creating new session!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Session saved.", Toast.LENGTH_SHORT).show();

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
        adapter = new TrainingRecyclerAdapter(trainingSessions, () -> getSessionsFromDatabase());
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(Training.this));
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(Training.this);
    }

    public List<TrainingSession> extractTrainingSessions(DataSnapshot snapshot) {
        List<TrainingSession> trainingSessions = new ArrayList<>();
        for (DataSnapshot sessionSnapshot : snapshot.getChildren()) {
            TrainingSession trainingSession = new TrainingSession();
            trainingSession.setName(sessionSnapshot.child("name").getValue(String.class));
            trainingSession.setOwner(sessionSnapshot.child("owner").getValue(String.class));

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

    @Override
    public void onItemClick(int position) {
        TrainingSession clickedSession = privateTrainingSessions.get(position);

        Intent intent = new Intent(this, ExerciseListActivity.class);
        intent.putExtra("sessionName", clickedSession.getName());
        intent.putExtra("sessionOwner", clickedSession.getOwner());

        intent.putParcelableArrayListExtra("exercises", (ArrayList<? extends Parcelable>) clickedSession.getExercises());

        // Add transition animation
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    private void filterTrainingSessions(String query) {
        adapter.filterList(query);
    }
}



