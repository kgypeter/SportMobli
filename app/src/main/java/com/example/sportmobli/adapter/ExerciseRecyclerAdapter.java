package com.example.sportmobli.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sportmobli.R;
import com.example.sportmobli.dialogs.ConfirmationDialog;
import com.example.sportmobli.dialogs.MessageDialog;
import com.example.sportmobli.model.Exercise;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ExerciseRecyclerAdapter extends RecyclerView.Adapter<ExerciseRecyclerAdapter.ExerciseViewHolder> {

    private final ArrayList<Exercise> exercisesList;
    private final String owner;
    private final String sessionName;
    private final DatabaseReference trainingSessionReference;
    private Context parentContext;


    public ExerciseRecyclerAdapter(List<Exercise> exerciseList, String owner, String sessionName) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        trainingSessionReference = db.getReference("TrainingSession");
        this.exercisesList = new ArrayList<>(exerciseList);
        this.sessionName = sessionName;
        this.owner = owner;
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.parentContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.exercise_item, parent, false);

        return new ExerciseViewHolder(view, this::deleteExerciseCallback);

    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        Exercise exercise = exercisesList.get(position);
        holder.bind(exercise);
    }

    public void deleteExerciseCallback(Exercise exercise) {

        if (owner.equals("public")) {
            MessageDialog.showAlertDialog(parentContext, "Oops", "This exercise belongs to a public session. You can only modify private sessions.");
        } else {
            ConfirmationDialog.show(parentContext, "Delete exercise.", "Are you sure you want to delete this?", () -> trainingSessionReference.child(owner).child(sessionName).child("exercises").child(exercise.getName()).removeValue().addOnSuccessListener(task -> Toast.makeText(parentContext, "Exercise deleted", Toast.LENGTH_SHORT).show()).addOnFailureListener(task -> Toast.makeText(parentContext, "Delete failure", Toast.LENGTH_SHORT).show()));
        }
    }

    @Override
    public int getItemCount() {
        return exercisesList.size();
    }

    public static class ExerciseViewHolder extends RecyclerView.ViewHolder {
        private final TextView exerciseNameTextView;
        private final TextView exerciseDurationTextView;
        private final TextView exerciseRestTimeTextView;
        private final DeleteExerciseListener deleteExerciseListener;
        Button deleteButton;


        public ExerciseViewHolder(View itemView, DeleteExerciseListener deleteExerciseListener) {
            super(itemView);

            exerciseNameTextView = itemView.findViewById(R.id.exerciseNameTextView);
            exerciseDurationTextView = itemView.findViewById(R.id.exerciseDurationTextView);
            exerciseRestTimeTextView = itemView.findViewById(R.id.exerciseRestTimeTextView);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            this.deleteExerciseListener = deleteExerciseListener;


        }

        @SuppressLint("SetTextI18n")
        public void bind(Exercise exercise) {
            exerciseNameTextView.setText(exercise.getName());
            exerciseDurationTextView.setText("Duration: " + exercise.getDuration() + " seconds");
            exerciseRestTimeTextView.setText("Rest Time: " + exercise.getRestTime() + " seconds");
            deleteButton.setOnClickListener(view -> deleteExerciseListener.deleteExercise(exercise));
        }

        public interface DeleteExerciseListener {
            void deleteExercise(Exercise exercise);
        }
    }


}
