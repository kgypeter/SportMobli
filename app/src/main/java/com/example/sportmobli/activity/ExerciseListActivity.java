package com.example.sportmobli.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.sportmobli.R;
import com.example.sportmobli.adapter.ExerciseRecyclerAdapter;
import com.example.sportmobli.model.Exercise;

import java.util.ArrayList;

public class ExerciseListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ExerciseRecyclerAdapter adapter;

    private ArrayList<Exercise> exercisesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_list);

        recyclerView = findViewById(R.id.exerciseRecyclerView);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("exercises")) {
            exercisesList = intent.getParcelableArrayListExtra("exercises"); // Assigning to class-level variable
            setupRecyclerView(exercisesList);
        }

        Button startButton = findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chronometerIntent = new Intent(ExerciseListActivity.this, ChronometerActivity.class);
                chronometerIntent.putParcelableArrayListExtra("exercises", exercisesList);
                startActivity(chronometerIntent);
            }
        });
    }

    private void setupRecyclerView(ArrayList<Exercise> exercisesList) {
        adapter = new ExerciseRecyclerAdapter(exercisesList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
