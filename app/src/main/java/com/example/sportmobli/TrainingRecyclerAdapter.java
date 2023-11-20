package com.example.sportmobli;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sportmobli.TrainingSession;

import java.util.ArrayList;
import java.util.List;

public class TrainingRecyclerAdapter extends RecyclerView.Adapter<TrainingRecyclerAdapter.TrainingViewHolder> {

    private List<TrainingSession> trainingSessions;
    private List<TrainingSession> filteredList;
    private OnItemClickListener listener;
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

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
        if (isNoItemFound()) {
            holder.sessionNameTextView.setText("No item found");
            holder.sessionDurationTextView.setText("");
            holder.itemView.setClickable(false);
        } else {
            TrainingSession trainingSession = filteredList.get(position);
            holder.bind(trainingSession);

            holder.itemView.setOnClickListener(view -> {
                if (listener != null) {
                    listener.onItemClick(position);
                }
            });
            holder.itemView.setClickable(true);
        }
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
        public void bind(TrainingSession session) {
            sessionNameTextView.setText(session.getName());

            // Calculate total duration including exercise duration and rest time
            float totalDuration = 0;
            for (Exercise exercise : session.getExercises()) {
                totalDuration += exercise.getDuration() + exercise.getRestTime();
            }
            sessionDurationTextView.setText("Total Duration: " + totalDuration + "s");

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
}
