package com.example.sportmobli.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sportmobli.R;
import com.example.sportmobli.model.Victual;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class DietRecyclerAdapter extends RecyclerView.Adapter<DietRecyclerAdapter.MyViewHolder> {

    private final List<Victual> foodList;
    private final List<Victual> filteredList;
    private final TextView totalGramsTextView;
    private final TextView totalCaloriesTextView;
    private final TextView totalProteinTextView;
    private final TextView totalCarbsTextView;
    private final TextView totalFatsTextView;
    private final DietRecyclerAdapter.FetchDataListener fetchDataListener;
    private FirebaseDatabase db;
    private DatabaseReference dietHistoryReference;

    public DietRecyclerAdapter(List<Victual> foodList, TextView totalGramsTextView,
                               TextView totalCaloriesTextView, TextView totalProteinTextView,
                               TextView totalCarbsTextView, TextView totalFatsTextView,
                               DietRecyclerAdapter.FetchDataListener fetchDataListener) {
        // This needs to be initialised so it can be filled by the callback
        this.foodList = foodList;
        this.totalGramsTextView = totalGramsTextView;
        this.totalCaloriesTextView = totalCaloriesTextView;
        this.totalProteinTextView = totalProteinTextView;
        this.totalCarbsTextView = totalCarbsTextView;
        this.totalFatsTextView = totalFatsTextView;
        this.filteredList = new ArrayList<>(foodList);
        this.fetchDataListener = fetchDataListener;

    }

    @SuppressLint("NotifyDataSetChanged")
    public void resetFilter() {
        // Restore the original, unfiltered list
        filteredList.clear();
        filteredList.addAll(foodList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.victuals, parent, false);
        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (isNoItemFound()) {
            holder.nameTxt.setText("No item found");
            holder.caloriesTxt.setText("");
            holder.proteinTxt.setText("");
            holder.carbsTxt.setText("");
            holder.fatsTxt.setText("");
            holder.hideButtons(); // Hide buttons when showing "No item found"
            holder.itemView.setClickable(false);
        } else {
            Victual diet = filteredList.get(position);
            holder.nameTxt.setText(diet.getFoodName());
            holder.caloriesTxt.setText("Calories: " + diet.getCalories());
            holder.proteinTxt.setText("Protein: " + diet.getProtein() + "g");
            holder.carbsTxt.setText("Carbs: " + diet.getCarbohydrates() + "g");
            holder.fatsTxt.setText("Fats: " + diet.getFats() + "g");
            holder.showButtons(); // Show buttons when displaying regular list items
            holder.itemView.setClickable(true);
        }
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filterList(String query) {
        filteredList.clear();
        String filterPattern = query.toLowerCase().trim();

        for (Victual item : foodList) {
            if (item.getFoodName().toLowerCase().contains(filterPattern)) {
                filteredList.add(item);
            }
        }

        if (filteredList.isEmpty() && !query.isEmpty()) {
            // If there are no matches and the query is not empty, add a dummy item
            filteredList.add(new Victual("", 0, 0, 0, 0));
        }

        notifyDataSetChanged(); // Notify the adapter that the data set has changed
    }

    public boolean isNoItemFound() {
        // Check if the filtered list contains only the dummy item
        return filteredList.size() == 1 && filteredList.get(0).getFoodName().isEmpty();
    }

    public interface FetchDataListener {
        void fetchData();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameTxt;
        private final TextView caloriesTxt;
        private final TextView proteinTxt;
        private final TextView carbsTxt;
        private final TextView fatsTxt;

        private Victual clickedDiet;

        public MyViewHolder(final View view) {
            super(view);
            nameTxt = view.findViewById(R.id.textView5);
            caloriesTxt = view.findViewById(R.id.caloriesTextView);
            proteinTxt = view.findViewById(R.id.proteinTextView);
            carbsTxt = view.findViewById(R.id.carbsTextView);
            fatsTxt = view.findViewById(R.id.fatsTextView);

            view.setOnClickListener(v -> {
                // Handle click event, show a window to enter grams
                clickedDiet = foodList.get(getAdapterPosition());
                showDetailsDialog(clickedDiet);
            });
        }

        public void hideButtons() {
            itemView.findViewById(R.id.deleteButton).setVisibility(View.GONE);
        }

        public void showButtons() {
            itemView.findViewById(R.id.deleteButton).setVisibility(View.VISIBLE);
        }

        @SuppressLint("SetTextI18n")
        private void showDetailsDialog(Victual diet) {
            AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
            builder.setTitle("Add quantity in grams");

            // Inflate a custom layout for the details
            View detailsView = LayoutInflater.from(itemView.getContext()).inflate(R.layout.diet_details_layout, null);
            TextView nameTxt = detailsView.findViewById(R.id.nameTextView);
            TextView caloriesTxt = detailsView.findViewById(R.id.caloriesTextView);
            TextView proteinTxt = detailsView.findViewById(R.id.proteinTextView);
            TextView carbsTxt = detailsView.findViewById(R.id.carbsTextView);
            TextView fatsTxt = detailsView.findViewById(R.id.fatsTextView);
            EditText gramsInput = detailsView.findViewById(R.id.gramsEditText);

            nameTxt.setText(diet.getFoodName());
            caloriesTxt.setText("Calories: " + diet.getCalories());
            proteinTxt.setText("Protein: " + diet.getProtein() + "g");
            carbsTxt.setText("Carbs: " + diet.getCarbohydrates() + "g");
            fatsTxt.setText("Fats: " + diet.getFats() + "g");
            TextView addQuantityLabel = detailsView.findViewById(R.id.addQuantityLabel);
            addQuantityLabel.setText("Add quantity");
            gramsInput.setHint("Enter grams");
            gramsInput.setText(String.valueOf(diet.getTotalGrams()));  // Set the existing grams

            float originalGrams = diet.getTotalGrams();  // Keep track of the original quantity

            builder.setView(detailsView);

            builder.setPositiveButton("OK", (dialog, which) -> {
                // Update the total grams based on the modified quantity
                try {
                    float newGrams = Float.parseFloat(gramsInput.getText().toString());
                    float gramsDifference = newGrams - originalGrams;  // Calculate the difference
                    diet.addToTotalGrams(gramsDifference);
                    updateTotalGrams();  // Update total grams text view
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    Toast.makeText(itemView.getContext(), "Invalid input. Please enter a valid number.", Toast.LENGTH_SHORT).show();
                }
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

            builder.show();
        }

        @SuppressLint({"SetTextI18n", "DefaultLocale"})
        private void updateTotalGrams() {
            float totalCalories = 0;
            float totalProtein = 0;
            float totalCarbs = 0;
            float totalFats = 0;
            float total = 0;

            for (Victual victual : foodList) {
                // Increment the respective totals based on the multiplied values
                totalCalories += (victual.getCalories() / 100) * victual.getTotalGrams();
                totalProtein += (victual.getProtein() / 100) * victual.getTotalGrams();
                totalCarbs += (victual.getCarbohydrates() / 100) * victual.getTotalGrams();
                totalFats += (victual.getFats() / 100) * victual.getTotalGrams();
                total += victual.getTotalGrams();
            }

            // Update the respective TextViews with the calculated totals
            totalGramsTextView.setText(String.format("Total: %.2f g", total));
            totalCaloriesTextView.setText(String.format("Calories: %.2f g", totalCalories));
            totalProteinTextView.setText(String.format("Protein: %.2f g", totalProtein));
            totalCarbsTextView.setText(String.format("Carbs: %.2f g", totalCarbs));
            totalFatsTextView.setText(String.format("Fats: %.2f g", totalFats));
        }

    }
}
