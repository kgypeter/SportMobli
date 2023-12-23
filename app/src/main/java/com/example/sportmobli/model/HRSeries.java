package com.example.sportmobli.model;

import com.androidplot.xy.SimpleXYSeries;

import java.util.ArrayList;
import java.util.List;

public class HRSeries {
    private SimpleXYSeries hrSeries;
    private SimpleXYSeries rrSeries;
    private List<Double> xHrVals = new ArrayList<>();
    private List<Double> yHrVals = new ArrayList<>();
    private List<Double> xRrVals = new ArrayList<>();
    private List<Double> yRrVals = new ArrayList<>();


    public SimpleXYSeries getHrSeries() {
        return hrSeries;
    }

    public void setHrSeries(SimpleXYSeries hrSeries) {
        this.hrSeries = hrSeries;
    }

    public SimpleXYSeries getRrSeries() {
        return rrSeries;
    }

    public void setRrSeries(SimpleXYSeries rrSeries) {
        this.rrSeries = rrSeries;
    }

    public List<Double> getxHrVals() {
        return xHrVals;
    }

    public void setxHrVals(List<Double> xHrVals) {
        this.xHrVals = xHrVals;
    }

    public List<Double> getyHrVals() {
        return yHrVals;
    }

    public void setyHrVals(List<Double> yHrVals) {
        this.yHrVals = yHrVals;
    }

    public List<Double> getxRrVals() {
        return xRrVals;
    }

    public void setxRrVals(List<Double> xRrVals) {
        this.xRrVals = xRrVals;
    }

    public List<Double> getyRrVals() {
        return yRrVals;
    }

    public void setyRrVals(List<Double> yRrVals) {
        this.yRrVals = yRrVals;
    }
}
