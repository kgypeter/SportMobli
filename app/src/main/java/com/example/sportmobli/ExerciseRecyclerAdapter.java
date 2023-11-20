package com.example.sportmobli;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class ExerciseRecyclerAdapter extends RecyclerView.Adapter<ExerciseRecyclerAdapter.ExerciseViewHolder> {

    private RecyclerView recyclerView;
    private ExerciseRecyclerAdapter adapter;
    private ArrayList<Exercise> exercisesList;

    public ExerciseRecyclerAdapter(List<Exercise> exerciseList) {
        this.exercisesList = (ArrayList<Exercise>) exerciseList;
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.exercise_item, parent, false);
        return new ExerciseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        Exercise exercise = exercisesList.get(position);
        holder.bind(exercise);
    }

    @Override
    public int getItemCount() {
        return exercisesList.size();
    }

    public static class ExerciseViewHolder extends RecyclerView.ViewHolder {
        private TextView exerciseNameTextView;
        private TextView exerciseDurationTextView;

        public ExerciseViewHolder(View itemView) {
            super(itemView);
            exerciseNameTextView = itemView.findViewById(R.id.exerciseNameTextView);
            exerciseDurationTextView = itemView.findViewById(R.id.exerciseDurationTextView);
        }

        public void bind(Exercise exercise) {
            exerciseNameTextView.setText(exercise.getName());
            exerciseDurationTextView.setText("Duration: " + exercise.getDuration() + " seconds");
        }
    }

    // Method to update the exercise list without blocking the RecyclerView
    private void updateExerciseList(List<Exercise> newExercises) {
        exercisesList.clear(); // Clear the existing list
        exercisesList.addAll(newExercises); // Add updated exercises

        if (adapter != null) {
            adapter.updateExerciseList(exercisesList); // Update the adapter
        }
    }
}