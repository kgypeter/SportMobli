package com.example.sportmobli.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.androidplot.xy.XYPlot;
import com.example.sportmobli.R;
import com.example.sportmobli.activity.PlotActivity;
import com.example.sportmobli.model.TrainingHistoryDisplay;
import com.example.sportmobli.util.HRPlotter;

import java.util.List;
import java.util.Map;

public class TrainingHistoryRecyclerAdapter extends RecyclerView.Adapter<TrainingHistoryRecyclerAdapter.TrainingHistoryViewHolder> {
    private final List<TrainingHistoryDisplay> trainingHistoryList;
    private final Context context;

    public TrainingHistoryRecyclerAdapter(List<TrainingHistoryDisplay> trainingHistoryList, Context context) {
        this.trainingHistoryList = trainingHistoryList;
        this.context = context;
    }

    @NonNull
    @Override
    public TrainingHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.training_history_item, parent, false);
        return new TrainingHistoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TrainingHistoryViewHolder holder, final int position) {
        TrainingHistoryDisplay trainingHistory = trainingHistoryList.get(position);
        holder.bind(trainingHistory);

        // Handle item clicks
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an Intent to start the new activity
                Intent intent = new Intent(context, PlotActivity.class);

                // Pass data to the new activity
                Bundle bundle = new Bundle();
                Map<String, Double> history = trainingHistory.getHrHistory();
                for (String key : history.keySet()){
                    bundle.putDouble(key,history.get(key) );
                }
                intent.putExtra("hrSeries", bundle);

                // Start the new activity
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return trainingHistoryList.size();
    }

    public class TrainingHistoryViewHolder extends RecyclerView.ViewHolder {
        private final TextView historySessionName;
        private final TextView historyTotalTime;
        private final TextView historyAddedDate;
        private final XYPlot plot;

        public TrainingHistoryViewHolder(final View view) {
            super(view);
            historySessionName = view.findViewById(R.id.historySessionName);
            historyTotalTime = view.findViewById(R.id.historyTotalTime);
            historyAddedDate = view.findViewById(R.id.historyAddedDate);
            plot = view.findViewById(R.id.hr_view_plot);

        }

        @SuppressLint("SetTextI18n")
        public void bind(TrainingHistoryDisplay trainingHistory) {
            historySessionName.setText("Session Name: " + trainingHistory.getSessionName());
            historyTotalTime.setText("Total Time: " + trainingHistory.getTotalTime());
            historyAddedDate.setText("Added Date: " + trainingHistory.getAddedDate());

        }
    }
}
