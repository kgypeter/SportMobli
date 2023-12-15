package com.example.sportmobli.model;

import java.util.Map;

public class TrainingHistoryDisplay {

    private String sessionName;
    private String totalTime;
    private String addedDate;
    private Map<String, Double> hrHistory;


    public TrainingHistoryDisplay() {
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

    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

    public String getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(String addedDate) {
        this.addedDate = addedDate;
    }
}
