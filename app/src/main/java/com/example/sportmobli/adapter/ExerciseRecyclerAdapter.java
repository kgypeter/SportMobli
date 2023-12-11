package com.example.sportmobli.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sportmobli.R;
import com.example.sportmobli.model.Exercise;

import java.util.ArrayList;
import java.util.List;

public class ExerciseRecyclerAdapter extends RecyclerView.Adapter<ExerciseRecyclerAdapter.ExerciseViewHolder> {

    private final ArrayList<Exercise> exercisesList;

    public ExerciseRecyclerAdapter(List<Exercise> exerciseList) {
        this.exercisesList = new ArrayList<>(exerciseList);
    }

    public ArrayList<Exercise> getExercisesList() {
        return exercisesList;
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
        private final TextView exerciseNameTextView;
        private final TextView exerciseDurationTextView;
        private final TextView exerciseRestTimeTextView;

        public ExerciseViewHolder(View itemView) {
            super(itemView);
            exerciseNameTextView = itemView.findViewById(R.id.exerciseNameTextView);
            exerciseDurationTextView = itemView.findViewById(R.id.exerciseDurationTextView);
            exerciseRestTimeTextView = itemView.findViewById(R.id.exerciseRestTimeTextView);
        }

        @SuppressLint("SetTextI18n")
        public void bind(Exercise exercise) {
            exerciseNameTextView.setText(exercise.getName());
            exerciseDurationTextView.setText("Duration: " + exercise.getDuration() + " seconds");
            exerciseRestTimeTextView.setText("Rest Time: " + exercise.getRestTime() + " seconds");
        }
    }

//    @SuppressLint("NotifyDataSetChanged")
//    public void updateExerciseList(List<Exercise> newExercises) {
//        exercisesList.clear(); // Clear the existing list
//        exercisesList.addAll(newExercises); // Add updated exercises
//        notifyDataSetChanged(); // Notify RecyclerView about the change
//    }
}
