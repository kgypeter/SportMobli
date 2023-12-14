package com.example.sportmobli.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sportmobli.R;
import com.example.sportmobli.adapter.DietHistoryRecyclerAdapter;
import com.example.sportmobli.model.DietHistory;

import java.util.ArrayList;

public class DietHistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_history);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewDietHistory);

        ArrayList<DietHistory> dietHistoryList = getDietHistoryList();

        DietHistoryRecyclerAdapter adapter = new DietHistoryRecyclerAdapter(dietHistoryList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    private ArrayList<DietHistory> getDietHistoryList() {
        ArrayList<DietHistory> dietHistoryList = new ArrayList<>();
        return dietHistoryList;
    }
}
