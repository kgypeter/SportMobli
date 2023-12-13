package com.example.sportmobli.model;

// ? when a new user logs in the other necessary information should be asked like: gender, age, weight, height
// to fill in the user profile page and these attributes should take part of the User OR the user profile page can be
// filled in after login these attributes (gender, age, weight, height) taking part of the UserProfile Object instead of User object

// todo - add the four additional attributes and send them to the user profile
public class User {
// From the user page i want to send the gender, age, height, weight to the User Profile page grid under the corresponding images
    private String username;
    private String password;
    private String gender;
    private int age;
    private float height;
    private float weight;

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }
}
