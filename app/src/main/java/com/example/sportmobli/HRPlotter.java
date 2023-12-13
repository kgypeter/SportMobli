package com.example.sportmobli;

import android.graphics.Color;

import com.androidplot.PlotListener;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeriesFormatter;
import com.polar.sdk.api.model.PolarHrData;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HRPlotter {
    private static final String TAG = "TimePlotter";
    private static final int NVALS = 300; // 5 min
    private static final double RR_SCALE = 0.1;

    private XYPlot listener;
    public  XYSeriesFormatter<?> hrFormatter;
    public  XYSeriesFormatter<?> rrFormatter;
    public  SimpleXYSeries hrSeries;
    public  SimpleXYSeries rrSeries;
    private List<Double> xHrVals = new ArrayList<>();
    private List<Double> yHrVals = new ArrayList<>();
    private List<Double> xRrVals = new ArrayList<>();
    private List<Double> yRrVals = new ArrayList<>();

    public HRPlotter() {
        Date now = new Date();
        double endTime = now.getTime();
        double startTime = endTime - NVALS * 1000;
        double delta = (endTime - startTime) / (NVALS - 1);

        // Specify initial values to keep it from auto sizing
        for (int i = 0; i < NVALS; i++) {
            xHrVals.add(i, startTime + i * delta);
            yHrVals.add(i, 60.0);
            xRrVals.add(i, startTime + i * delta);
            yRrVals.add(i, 100.0);
        }
        hrFormatter = new LineAndPointFormatter(Color.RED, null, null, null);
        ((LineAndPointFormatter) hrFormatter).setLegendIconEnabled(false);
        hrSeries = new SimpleXYSeries(xHrVals, yHrVals, "HR");
        rrFormatter = new LineAndPointFormatter(Color.BLUE, null, null, null);
        ((LineAndPointFormatter) rrFormatter).setLegendIconEnabled(false);
        rrSeries = new SimpleXYSeries(xRrVals, yRrVals, "RR");
    }

    public void addValues(PolarHrData.PolarHrSample polarHrData) {
        Date now = new Date();
        long time = now.getTime();
        for (int i = 0; i < NVALS - 1; i++) {
            xHrVals.set(i, xHrVals.get(i + 1));
            yHrVals.set(i, yHrVals.get(i + 1));
            hrSeries.setXY(xHrVals.get(i), yHrVals.get(i), i);
        }
        xHrVals.set(NVALS - 1, (double) time);
        yHrVals.set(NVALS - 1, (double) polarHrData.getHr());
        hrSeries.setXY(xHrVals.get(NVALS - 1), yHrVals.get(NVALS - 1), NVALS - 1);

        // Do RR
        // We don't know at what time the RR intervals start.  All we know is
        // the time the data arrived (the current time, now). This
        // implementation assumes they end at the current time, and spaces them
        // out in the past accordingly.  This seems to get the
        // relative positioning reasonably well.

        // Scale the RR values by this to use the same axis. (Could implement
        // NormedXYSeries and use two axes)
        List<Integer> rrsMs = polarHrData.getRrsMs();
        int nRrVals = rrsMs.size();
        if (nRrVals > 0) {
            for (int i = 0; i < NVALS - nRrVals; i++) {
                xRrVals.set(i, xRrVals.get(i + 1));
                yRrVals.set(i, yRrVals.get(i + 1));
                rrSeries.setXY(xRrVals.get(i), yRrVals.get(i), i);
            }
            double totalRR = 0.0;
            for (int i = 0; i < nRrVals; i++) {
                totalRR += RR_SCALE * rrsMs.get(i);
            }
            int index = 0;
            double rr;
            for (int i = NVALS - nRrVals; i < NVALS; i++) {
                rr = RR_SCALE * rrsMs.get(index++);
                xRrVals.set(i, time - totalRR);
                yRrVals.set(i, rr);
                totalRR -= rr;
                rrSeries.setXY(xRrVals.get(i), yRrVals.get(i), i);
            }
        }
        if (listener != null) {
            listener.redraw();
        }
    }

    public void setListener(XYPlot listener) {
        this.listener = listener;
    }
}
