package com.example.sportmobli.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sportmobli.R;
import com.example.sportmobli.model.TrainingHistory;

import java.util.List;

public class TrainingHistoryRecyclerAdapter extends RecyclerView.Adapter<TrainingHistoryRecyclerAdapter.TrainingHistoryViewHolder> {
    private final List<TrainingHistory> trainingHistoryList;

    public TrainingHistoryRecyclerAdapter(List<TrainingHistory> trainingHistoryList) {
        this.trainingHistoryList = trainingHistoryList;
    }

    @NonNull
    @Override
    public TrainingHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.diet_history_item, parent, false);
        return new TrainingHistoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TrainingHistoryViewHolder holder, int position) {
        TrainingHistory trainingHistory = trainingHistoryList.get(position);
        holder.bind(trainingHistory);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class TrainingHistoryViewHolder extends RecyclerView.ViewHolder {

        public TrainingHistoryViewHolder(final View view) {
            super(view);
        }

        public void bind(TrainingHistory trainingHistory) {

        }
    }
}
