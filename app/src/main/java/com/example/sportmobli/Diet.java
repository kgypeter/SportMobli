package com.example.sportmobli;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class Diet extends AppCompatActivity {

    private String foodName;
    private float calories;
    private float protein;
    private float carbohydrates;
    private float fats;
    private float totalGrams;

    public Diet() {
    }

    public Diet(String foodName, float calories, float protein, float carbohydrates, float fats) {
        this.foodName = foodName;
        this.calories = calories;
        this.protein = protein;
        this.carbohydrates = carbohydrates;
        this.fats = fats;
        this.totalGrams = 0;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public float getCalories() {
        return calories;
    }

    public void setCalories(float calories) {
        this.calories = calories;
    }

    public float getProtein() {
        return protein;
    }

    public void setProtein(float protein) {
        this.protein = protein;
    }

    public float getCarbohydrates() {
        return carbohydrates;
    }

    public void setCarbohydrates(float carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    public float getFats() {
        return fats;
    }

    public void setFats(float fats) {
        this.fats = fats;
    }

    public float getTotalGrams() {
        return totalGrams;
    }

    public void addToTotalGrams(float grams) {
        this.totalGrams += grams;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet);

        Log.d("DietActivity", "onCreate: Diet activity started");

        try {
            Button lolButton = findViewById(R.id.button3);
            Button trainingButton = findViewById(R.id.button4);
            Button userProfileButton = findViewById(R.id.button6);
            Button trackingButton = findViewById(R.id.button7);

            // OnClickListeners for the buttons
            lolButton.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), Home.class)));
            trainingButton.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), Training.class)));
            userProfileButton.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), UserProfile.class)));
            trackingButton.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), Tracking.class)));

            // Set up for the RecyclerView and Adapter
            RecyclerView recyclerView = findViewById(R.id.recyclerView);
            if (recyclerView == null) {
                Log.e("DietActivity", "RecyclerView is null");
            } else {
                Log.d("DietActivity", "RecyclerView is not null");
                TextView totalGramsTextView = findViewById(R.id.totalGramsTextView);
                RecyclerAdapter adapter = new RecyclerAdapter(getFoodList(), totalGramsTextView);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(adapter);

                // Set up the search functionality
                EditText searchEditText = findViewById(R.id.searchEditText);
                searchEditText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        // Check if the input is empty
                        if (charSequence.toString().isEmpty()) {
                            // If empty, you can reset the filter or perform other actions
                            adapter.resetFilter();
                        } else {
                            // Only filter if the query is not empty
                            adapter.filterList(charSequence.toString());
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        // Check if the input is empty
                        if (editable.toString().isEmpty()) {
                            // If empty, reset the filter
                            adapter.resetFilter();
                        } else {
                            // If not empty, filter the list based on the input
                            adapter.filterList(editable.toString());
                        }
                    }
                });

            }
        } catch (Exception e) {
            Log.e("DietActivity", "Error in onCreate: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private ArrayList<Diet> getFoodList() {
        ArrayList<Diet> foodList = new ArrayList<>();
        // Example items with additional attributes
        foodList.add(new Diet("Rice", 130, 2.7f, 28, 0.3f));
        foodList.add(new Diet("Bread", 85, 2.7f, 15, 1));
        foodList.add(new Diet("Chicken Egg", 68, 5.5f, 0.6f, 4.8f));
        foodList.add(new Diet("Milk", 42, 3.4f, 5, 1.4f));
        foodList.add(new Diet("Carrot", 41, 0.9f, 10, 0.2f));
        foodList.add(new Diet("Apple", 52, 0.3f, 14, 0.2f));
        foodList.add(new Diet("Banana", 105, 1.3f, 27, 0.3f));
        foodList.add(new Diet("Broccoli", 55, 3.7f, 11, 0.6f));
        foodList.add(new Diet("Salmon", 206, 22, 0, 13));
        foodList.add(new Diet("Spinach", 23, 2.9f, 3.6f, 0.4f));
        foodList.add(new Diet("Orange", 43, 1, 9, 0.2f));
        foodList.add(new Diet("Quinoa", 120, 4.1f, 21, 1.9f));
        foodList.add(new Diet("Tomato", 22, 1.1f, 5.5f, 0.2f));
        foodList.add(new Diet("Avocado", 160, 2, 9, 15));
        foodList.add(new Diet("Almonds", 579, 21, 22, 49));

        return foodList;
    }


}
