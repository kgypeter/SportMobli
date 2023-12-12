package com.example.sportmobli.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sportmobli.R;
import com.example.sportmobli.adapter.ExerciseRecyclerAdapter;
import com.example.sportmobli.model.Exercise;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ExerciseListActivity extends AppCompatActivity {
    FirebaseDatabase db;
    DatabaseReference exercisesReference;
    private RecyclerView recyclerView;
    private ExerciseRecyclerAdapter adapter;
    private ArrayList<Exercise> exercisesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        db = FirebaseDatabase.getInstance();
        exercisesReference = db.getReference("");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_list);

        recyclerView = findViewById(R.id.exerciseRecyclerView);

        Intent intent = getIntent();

//        exercisesReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DataSnapshot> task) {
//                if (!task.isSuccessful()) {
//                    Toast.makeText(ExerciseListActivity.this, "Invalid username or password!", Toast.LENGTH_SHORT).show();
//                } else {
//                    List<TrainingSession> trainingSessions = task.getResult().getValue(List.class);
//                    
//                }
//            }
//        });
        if (intent != null && intent.hasExtra("exercises")) {
            exercisesList = intent.getParcelableArrayListExtra("exercises"); // Assigning to class-level variable
            String owner = intent.getParcelableExtra("owner");
            String sessionName = intent.getParcelableExtra("sessionName");
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

                }
        );
    }

    private void setupRecyclerView(ArrayList<Exercise> exercisesList) {
        adapter = new ExerciseRecyclerAdapter(exercisesList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
