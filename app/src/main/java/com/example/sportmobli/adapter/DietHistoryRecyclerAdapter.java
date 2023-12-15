package com.example.sportmobli.adapter;


import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sportmobli.R;
import com.example.sportmobli.model.DietHistoryDisplay;

import java.util.List;

public class DietHistoryRecyclerAdapter extends RecyclerView.Adapter<DietHistoryRecyclerAdapter.MyViewHolder> {

    private final List<DietHistoryDisplay> dietHistoryList;

    public DietHistoryRecyclerAdapter(List<DietHistoryDisplay> dietHistoryList) {

        this.dietHistoryList = dietHistoryList;
    }

    @NonNull
    @Override

    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.diet_history_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DietHistoryDisplay dietHistory = dietHistoryList.get(position);
        holder.historyDate.setText("Date: " + dietHistory.getDateAdded());
        holder.historyCalories.setText("Calories: " + dietHistory.getCalories());
        holder.historyProtein.setText("Protein: " + dietHistory.getProtein());
        holder.historyCarbs.setText("Carbs: " + dietHistory.getCarbohydrates());
        holder.historyFats.setText("Fats: " + dietHistory.getFats());
    }

    @Override
    public int getItemCount() {

        return dietHistoryList.size();

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView historyDate;
        public TextView historyCalories;
        public TextView historyProtein;
        public TextView historyCarbs;
        public TextView historyFats;

        public MyViewHolder(View view) {
            super(view);
            historyDate = view.findViewById(R.id.historyDate);
            historyCalories = view.findViewById(R.id.historyCalories);
            historyProtein = view.findViewById(R.id.historyProtein);
            historyCarbs = view.findViewById(R.id.historyCarbs);
            historyFats = view.findViewById(R.id.historyFats);
        }

    }
}