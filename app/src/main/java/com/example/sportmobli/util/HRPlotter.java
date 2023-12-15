package com.example.sportmobli.util;

import android.graphics.Color;

import com.androidplot.PlotListener;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeriesFormatter;
import com.example.sportmobli.model.HRSeries;
import com.polar.sdk.api.model.PolarHrData;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HRPlotter {
    private static final String TAG = "TimePlotter";
    private static final int NVALS = 5; // 5 min
    private static final double RR_SCALE = 0.1;

    private XYPlot listener;
    public  XYSeriesFormatter<?> hrFormatter;
    public  XYSeriesFormatter<?> rrFormatter;
    public HRSeries series;

    public HRPlotter() {
        series = new HRSeries();
        Date now = new Date();
        double endTime = now.getTime();
        double startTime = endTime - NVALS * 1000;
        double delta = (endTime - startTime) / (NVALS - 1);

//         Specify initial values to keep it from auto sizing
        for (int i = 0; i < NVALS; i++) {
            series.getxHrVals().add(i, startTime + i * delta);
            series.getyHrVals().add(i, 0.0);

        }
        hrFormatter = new LineAndPointFormatter(Color.RED, null, null, null);
        ((LineAndPointFormatter) hrFormatter).setLegendIconEnabled(false);
        series.setHrSeries(new SimpleXYSeries(series.getxHrVals(), series.getyHrVals(), "HR"));
        rrFormatter = new LineAndPointFormatter(Color.BLUE, null, null, null);
        ((LineAndPointFormatter) rrFormatter).setLegendIconEnabled(false);
        series.setRrSeries(new SimpleXYSeries(series.getxRrVals(), series.getyRrVals(), "RR"));
    }

    public void addValues(PolarHrData.PolarHrSample polarHrData) {
        Date now = new Date();
        long time = now.getTime();


        series.getxHrVals().add((double) time);
        series.getyHrVals().add((double) polarHrData.getHr());
        series.getHrSeries().addLast(time, polarHrData.getHr());


        if (listener != null) {
            listener.redraw();
        }
    }


    public void reset(){
        series = new HRSeries();
        series.setxHrVals(new ArrayList<>());
        series.setyHrVals(new ArrayList<>());

        Date now = new Date();
        double endTime = now.getTime();
        double startTime = endTime - NVALS * 1000;
        double delta = (endTime - startTime) / (NVALS - 1);

        // Specify initial values to keep it from auto sizing
        for (int i = 0; i < NVALS; i++) {
            series.getxHrVals().add(i, startTime + i * delta);
            series.getyHrVals().add(i, 60.0);

        }
        hrFormatter = new LineAndPointFormatter(Color.RED, null, null, null);
        ((LineAndPointFormatter) hrFormatter).setLegendIconEnabled(false);
        series.setHrSeries(new SimpleXYSeries(series.getxHrVals(), series.getyHrVals(), "HR"));
        rrFormatter = new LineAndPointFormatter(Color.BLUE, null, null, null);
        ((LineAndPointFormatter) rrFormatter).setLegendIconEnabled(false);
        series.setRrSeries(new SimpleXYSeries(series.getxRrVals(), series.getyRrVals(), "RR"));
    }
    public void setListener(XYPlot listener) {
        this.listener = listener;
    }
}
