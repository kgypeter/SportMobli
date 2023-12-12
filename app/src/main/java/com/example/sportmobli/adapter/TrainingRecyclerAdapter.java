package com.example.sportmobli.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
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
import com.example.sportmobli.model.TrainingSession;
import com.example.sportmobli.util.AppPreferences;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class TrainingRecyclerAdapter extends RecyclerView.Adapter<TrainingRecyclerAdapter.TrainingViewHolder> {

    private final List<TrainingSession> trainingSessions;
    private final List<TrainingSession> filteredList;
    private OnItemClickListener listener;


    public TrainingRecyclerAdapter(List<TrainingSession> trainingSessions) {

        this.trainingSessions = trainingSessions;
        this.filteredList = new ArrayList<>(trainingSessions); // Initialize with all items
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public TrainingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.training_session, parent, false);

        return new TrainingViewHolder(parent.getContext(), view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull TrainingViewHolder holder, int position) {
        if (isNoItemFound()) {
            holder.sessionNameTextView.setText("No item found");
            holder.sessionDurationTextView.setText("");
            holder.hideButtons(); // Hide buttons when showing "No item found"
            holder.itemView.setClickable(false);
        } else {
            TrainingSession trainingSession = filteredList.get(position);
            holder.bind(trainingSession);
            holder.showButtons(); // Show buttons when displaying regular list items

            holder.itemView.setOnClickListener(view -> {
                if (listener != null) {
                    listener.onItemClick(position);
                }
            });
            holder.itemView.setClickable(true);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filterList(String query) {
        Log.d("Adapter", "Filtering with query: " + query);
        filteredList.clear();
        String filterPattern = query.toLowerCase().trim();

        for (TrainingSession item : trainingSessions) {
            if (item.getName().toLowerCase().contains(filterPattern)) {
                filteredList.add(item);
            }
        }

        notifyDataSetChanged(); // Notify adapter of data change
        Log.d("Adapter", "Filtered list size: " + filteredList.size());
    }

    @Override
    public int getItemCount() {
        if (isNoItemFound()) {
            return 1; // Display only the "No item found" message
        } else {
            return filteredList.size();
        }
    }

    public boolean isNoItemFound() {
        return filteredList.isEmpty(); // Check if filtered list is empty
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public static class TrainingViewHolder extends RecyclerView.ViewHolder {
        private final TextView sessionNameTextView;
        private final TextView sessionDurationTextView;
        private final Context parentContext;
        private FirebaseDatabase db;
        private DatabaseReference trainingSessionReference;


        public TrainingViewHolder(Context parentContext, View itemView) {
            super(itemView);
            this.parentContext = parentContext;
            sessionNameTextView = itemView.findViewById(R.id.sessionNameTextView);
            sessionDurationTextView = itemView.findViewById(R.id.sessionDurationTextView);
        }

        public void hideButtons() {
            itemView.findViewById(R.id.deleteButton).setVisibility(View.GONE);
        }

        public void showButtons() {
            itemView.findViewById(R.id.deleteButton).setVisibility(View.VISIBLE);
        }

        @SuppressLint({"SetTextI18n", "DefaultLocale"})
        public void bind(TrainingSession session) {
            sessionNameTextView.setText(session.getName());
            String currentUsername = AppPreferences.getUsername(parentContext);
            db = FirebaseDatabase.getInstance();
            trainingSessionReference = db.getReference("TrainingSession");

            // Calculate total duration including exercise duration and rest time
            float totalDurationSeconds = 0;
            for (Exercise exercise : session.getExercises()) {
                totalDurationSeconds += exercise.getDuration() + exercise.getRestTime();
            }

            // Convert total duration from seconds to minutes
            float totalDurationMinutes = totalDurationSeconds / 60;

            // Display total duration in minutes with appropriate formatting
            sessionDurationTextView.setText("Total Duration: " + String.format("%.2f min", totalDurationMinutes));

            //Add button listeners
            Button deleteButton = itemView.findViewById(R.id.deleteButton);

            deleteButton.setOnClickListener(view -> {
                if (session.getOwner().equals("public")) {
                    MessageDialog.showAlertDialog(parentContext, "Oops", "This is a public session. You can only delete sessions you own.");
                } else {
                    ConfirmationDialog.show(parentContext, "Confirm", "Are you sure you want to delete this?",
                            () -> trainingSessionReference.child(session.getName()).removeValue().addOnSuccessListener(task -> {
                                Toast.makeText(parentContext, "Session deleted", Toast.LENGTH_SHORT).show();
                            }).addOnFailureListener(task -> {
                                Toast.makeText(parentContext, "Error deleting session!", Toast.LENGTH_SHORT).show();

                            }));
                }
            });


        }


    }
}
