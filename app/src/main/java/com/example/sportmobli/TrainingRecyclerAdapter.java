package com.example.sportmobli;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sportmobli.TrainingSession;

import java.util.ArrayList;
import java.util.List;

public class TrainingRecyclerAdapter extends RecyclerView.Adapter<TrainingRecyclerAdapter.TrainingViewHolder> {

    private List<TrainingSession> trainingSessions;
    private List<TrainingSession> filteredList; // To store filtered items

    public TrainingRecyclerAdapter(List<TrainingSession> trainingSessions) {
        this.trainingSessions = trainingSessions;
        this.filteredList = new ArrayList<>(trainingSessions); // Initialize with all items
    }

    @NonNull
    @Override
    public TrainingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.training_session, parent, false);
        return new TrainingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrainingViewHolder holder, int position) {
        TrainingSession trainingSession = filteredList.get(position); // Get item from filtered list
        holder.bind(trainingSession);
    }

    @Override
    public int getItemCount() {
        return filteredList.size(); // Use filtered list size for item count
    }

    public static class TrainingViewHolder extends RecyclerView.ViewHolder {
        private TextView sessionNameTextView;
        private TextView sessionDurationTextView;

        public TrainingViewHolder(View itemView) {
            super(itemView);
            sessionNameTextView = itemView.findViewById(R.id.sessionNameTextView);
            sessionDurationTextView = itemView.findViewById(R.id.sessionDurationTextView);
        }

        @SuppressLint("SetTextI18n")
        public void bind(TrainingSession trainingSession) {
            sessionNameTextView.setText(trainingSession.getName());
            sessionDurationTextView.setText("Duration: " + trainingSession.getDuration() + " minutes");
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

    public boolean isNoItemFound() {
        return filteredList.isEmpty(); // Check if filtered list is empty
    }
}
