package com.example.sportmobli.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class TrainingSession implements Parcelable {
    public static final Creator<TrainingSession> CREATOR = new Creator<TrainingSession>() {
        @Override
        public TrainingSession createFromParcel(Parcel in) {
            return new TrainingSession(in);
        }

        @Override
        public TrainingSession[] newArray(int size) {
            return new TrainingSession[size];
        }
    };
    private String name;
    private List<Exercise> exercises;
    private float duration;
    private String owner;

    protected TrainingSession(Parcel in) {
        name = in.readString();
        duration = in.readFloat();

    }

    public TrainingSession(String name) {
        this.name = name;
        this.duration = 0;
        this.exercises = new ArrayList<>();
        this.owner = "public";
    }

    public TrainingSession(String name, String owner) {
        this.name = name;
        this.duration = 0;
        this.exercises = new ArrayList<>();
        this.owner = owner;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeFloat(duration);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getDuration() {
        return duration;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }

    public List<Exercise> getExercises() {
        return exercises;
    }

    public void setExercises(List<Exercise> exercises) {
        this.exercises = exercises;
    }

    // add an exercise to the sessison
    public void addExercise(Exercise exercise) {
        exercises.add(exercise);
    }

    // Calculate total duration of all exercises in the session
    public float getTotalDuration() {
        float totalDuration = 0;
        for (Exercise exercise : exercises) {
            totalDuration += exercise.getDuration();
        }
        return totalDuration;
    }

}
