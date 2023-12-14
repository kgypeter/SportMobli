package com.example.sportmobli.model;

import com.androidplot.xy.SimpleXYSeries;

public class HRSeries {
    private SimpleXYSeries hrSeries;
    private  SimpleXYSeries rrSeries;


    public SimpleXYSeries getHrSeries() {
        return hrSeries;
    }

    public SimpleXYSeries getRrSeries() {
        return rrSeries;
    }

    public void setHrSeries(SimpleXYSeries hrSeries) {
        this.hrSeries = hrSeries;
    }

    public void setRrSeries(SimpleXYSeries rrSeries) {
        this.rrSeries = rrSeries;
    }
}
