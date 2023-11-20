package com.example.sportmobli;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ExerciseListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ExerciseRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_list);

        recyclerView = findViewById(R.id.exerciseRecyclerView);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("exercises")) {
            ArrayList<Exercise> exercisesList = intent.getParcelableArrayListExtra("exercises");
            setupRecyclerView(exercisesList);
        }
    }

    private void setupRecyclerView(ArrayList<Exercise> exercisesList) {
        adapter = new ExerciseRecyclerAdapter(exercisesList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
