package com.example.sportmobli.model;

import java.time.LocalDateTime;
import java.util.Map;

public class TrainingHistory {
    private String sessionName;
    private String owner;
    private LocalDateTime addedDate;

    private float totalTime;

    private Map<String, Double> hrHistory;

    public TrainingHistory() {
    }

    public Map<String, Double> getHrHistory() {
        return hrHistory;
    }

    public void setHrHistory(Map<String, Double> hrHistory) {
        this.hrHistory = hrHistory;
    }

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public LocalDateTime getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(LocalDateTime addedDate) {
        this.addedDate = addedDate;
    }

    public float getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(float totalTime) {
        this.totalTime = totalTime;
    }
}
