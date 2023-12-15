package com.example.sportmobli.model;

public class Victual {

    private String foodName;
    private float calories;
    private float protein;
    private float carbohydrates;
    private float fats;
    private float totalGrams;


    public Victual() {
    }

    public Victual(String foodName, float calories, float protein, float carbohydrates, float fats) {
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

    public void setTotalGrams(float grams) {
        this.totalGrams = grams;
    }

    public void addToTotalGrams(float grams) {
        this.totalGrams += grams;
    }
}
