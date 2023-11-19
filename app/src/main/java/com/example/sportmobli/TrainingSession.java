package com.example.sportmobli;

public class TrainingSession {
    private String name;
    private float duration; // Duration of the exercise in seconds
    private float restTime; // Rest time after the exercise in seconds

    public TrainingSession(String name, float duration, float restTime) {
        this.name = name;
        this.duration = duration;
        this.restTime = restTime;
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

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public float getRestTime() {
        return restTime;
    }

    public void setRestTime(int restTime) {
        this.restTime = restTime;
    }
}
