package com.example.sportmobli.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.androidplot.xy.XYPlot;
import com.example.sportmobli.R;
import com.example.sportmobli.model.TrainingHistoryDisplay;
import com.example.sportmobli.util.HRPlotter;

import java.util.List;
import java.util.Map;

public class TrainingHistoryRecyclerAdapter extends RecyclerView.Adapter<TrainingHistoryRecyclerAdapter.TrainingHistoryViewHolder> {
    private final List<TrainingHistoryDisplay> trainingHistoryList;

    public TrainingHistoryRecyclerAdapter(List<TrainingHistoryDisplay> trainingHistoryList) {
        this.trainingHistoryList = trainingHistoryList;
    }

    @NonNull
    @Override
    public TrainingHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.training_history_item, parent, false);
        return new TrainingHistoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TrainingHistoryViewHolder holder, int position) {
        TrainingHistoryDisplay trainingHistory = trainingHistoryList.get(position);
        holder.bind(trainingHistory);
    }

    @Override
    public int getItemCount() {
        return trainingHistoryList.size();
    }

    public class TrainingHistoryViewHolder extends RecyclerView.ViewHolder {
        private final TextView historySessionName;
        private final TextView historyTotalTime;
        private final TextView historyAddedDate;
        private final HRPlotter plotter;
        private final XYPlot plot;

        public TrainingHistoryViewHolder(final View view) {
            super(view);
            historySessionName = view.findViewById(R.id.historySessionName);
            historyTotalTime = view.findViewById(R.id.historyTotalTime);
            historyAddedDate = view.findViewById(R.id.historyAddedDate);

            plotter = new HRPlotter();
            plot = view.findViewById(R.id.historyHrPlot);
            plotter.setListener(plot);
            plot.addSeries(plotter.series.getHrSeries(), plotter.hrFormatter);
        }

        @SuppressLint("SetTextI18n")
        public void bind(TrainingHistoryDisplay trainingHistory) {
            historySessionName.setText("Session Name: " + trainingHistory.getSessionName());
            historyTotalTime.setText("Total Time: " + trainingHistory.getTotalTime());
            historyAddedDate.setText("Added Date: " + trainingHistory.getAddedDate());
            Map<String, Double> hrSeries = trainingHistory.getHrHistory();
            if(hrSeries != null){
                for(int i = 5; i < hrSeries.size(); i++){
                    double value = hrSeries.get(String.valueOf(i));
                    plotter.addValuesManual((long) i, value);
                }
            }
        }
    }
}
