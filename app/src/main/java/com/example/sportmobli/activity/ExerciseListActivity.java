package com.example.sportmobli.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sportmobli.R;
import com.example.sportmobli.adapter.ExerciseRecyclerAdapter;
import com.example.sportmobli.dialogs.AddExerciseDialog;
import com.example.sportmobli.model.Exercise;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ExerciseListActivity extends AppCompatActivity {
    FirebaseDatabase db;
    DatabaseReference trainingSessionReference;
    private String owner;
    private String sessionName;
    private RecyclerView recyclerView;
    private ExerciseRecyclerAdapter adapter;
    private ArrayList<Exercise> exercisesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        db = FirebaseDatabase.getInstance();
        trainingSessionReference = db.getReference("TrainingSession");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_list);

        recyclerView = findViewById(R.id.exerciseRecyclerView);

        Intent intent = getIntent();

        if (intent != null && intent.hasExtra("exercises")) {
            exercisesList = intent.getParcelableArrayListExtra("exercises"); // Assigning to class-level variable
            sessionName = intent.getExtras().get("sessionName").toString();
            owner = intent.getExtras().get("sessionOwner").toString();
            setupRecyclerView(exercisesList);
        }

        Button startButton = findViewById(R.id.startButton);
        ImageView addButton = findViewById(R.id.addButton);
        startButton.setOnClickListener(v -> {
            Intent chronometerIntent = new Intent(ExerciseListActivity.this, ChronometerActivity.class);
            chronometerIntent.putParcelableArrayListExtra("exercises", exercisesList);
            startActivity(chronometerIntent);
        });

        addButton.setOnClickListener(
                view -> {
                    AddExerciseDialog.show(this, "Add a new exercise", exercise -> addExerciseCallback(exercise));
                }
        );
    }

    private void addExerciseCallback(Exercise exercise) {
        trainingSessionReference.child(owner).child(sessionName).child("exercises").child(exercise.getName()).setValue(exercise).addOnSuccessListener(t -> {
            Toast.makeText(this, "Exercise saved successfully!", Toast.LENGTH_SHORT).show();
            getExercises();
        }).addOnFailureListener(t -> {
            Toast.makeText(this, "Error saving exercise!", Toast.LENGTH_SHORT).show();

        });
    }

    private void getExercises() {
        trainingSessionReference.child(owner).child(sessionName).child("exercises").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                exercisesList = extractExercises(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ExerciseListActivity.this, "Error fetching exercises!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private ArrayList<Exercise> extractExercises(DataSnapshot snapshot) {
        ArrayList<Exercise> exercises = new ArrayList<>();
        for (DataSnapshot exerciseSnapshot : snapshot.getChildren()) {
            Exercise exercise = exerciseSnapshot.getValue(Exercise.class);
            exercises.add(exercise);
        }
        return exercises;
    }

    private void setupRecyclerView(ArrayList<Exercise> exercisesList) {
        adapter = new ExerciseRecyclerAdapter(exercisesList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
