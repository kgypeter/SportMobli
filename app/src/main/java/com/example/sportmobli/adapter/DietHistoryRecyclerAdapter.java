package com.example.sportmobli.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sportmobli.R;
import com.example.sportmobli.model.DietHistory;

import java.util.List;

public class DietHistoryRecyclerAdapter extends RecyclerView.Adapter<DietHistoryRecyclerAdapter.DietHistoryViewHolder> {
    private final List<DietHistory> dietHistoryList;

    public DietHistoryRecyclerAdapter(List<DietHistory> dietHistoryList) {
        this.dietHistoryList = dietHistoryList;
    }

    @NonNull
    @Override
    public DietHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.diet_history_item, parent, false);
        return new DietHistoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DietHistoryViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class DietHistoryViewHolder extends RecyclerView.ViewHolder {

        public DietHistoryViewHolder(final View view) {
            super(view);

        }
    }
}
