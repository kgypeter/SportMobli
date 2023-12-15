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
    private static final int NVALS = 300; // 5 min
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

        // Specify initial values to keep it from auto sizing
        for (int i = 0; i < NVALS; i++) {
            series.getxHrVals().add(i, startTime + i * delta);
            series.getyHrVals().add(i, 60.0);
            series.getxRrVals().add(i, startTime + i * delta);
            series.getyRrVals().add(i, 100.0);
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
        for (int i = 0; i < NVALS - 1; i++) {
            series.getxHrVals().set(i, series.getxHrVals().get(i + 1));
            series.getyHrVals().set(i, series.getyHrVals().get(i + 1));
            series.getHrSeries().setXY(series.getxHrVals().get(i), series.getyHrVals().get(i), i);
        }
        series.getxHrVals().set(NVALS - 1, (double) time);
        series.getyHrVals().set(NVALS - 1, (double) polarHrData.getHr());
        series.getHrSeries().setXY(series.getxHrVals().get(NVALS - 1), series.getyRrVals().get(NVALS - 1), NVALS - 1);

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
                series.getxRrVals().set(i, series.getxRrVals().get(i + 1));
                series.getyRrVals().set(i, series.getyRrVals().get(i + 1));
                series.getRrSeries().setXY(series.getxRrVals().get(i), series.getyRrVals().get(i), i);
            }
            double totalRR = 0.0;
            for (int i = 0; i < nRrVals; i++) {
                totalRR += RR_SCALE * rrsMs.get(i);
            }
            int index = 0;
            double rr;
            for (int i = NVALS - nRrVals; i < NVALS; i++) {
                rr = RR_SCALE * rrsMs.get(index++);
                series.getxRrVals().set(i, time - totalRR);
                series.getyRrVals().set(i, rr);
                totalRR -= rr;
                series.getRrSeries().setXY(series.getxRrVals().get(i), series.getyRrVals().get(i), i);
            }
        }
        if (listener != null) {
            listener.redraw();
        }
    }


    public void reset(){
        series = new HRSeries();
        series.setxHrVals(new ArrayList<>());
        series.setyHrVals(new ArrayList<>());
        series.setxRrVals(new ArrayList<>());
        series.setyRrVals(new ArrayList<>());
        Date now = new Date();
        double endTime = now.getTime();
        double startTime = endTime - NVALS * 1000;
        double delta = (endTime - startTime) / (NVALS - 1);

        // Specify initial values to keep it from auto sizing
        for (int i = 0; i < NVALS; i++) {
            series.getxHrVals().add(i, startTime + i * delta);
            series.getyHrVals().add(i, 60.0);
            series.getxRrVals().add(i, startTime + i * delta);
            series.getyRrVals().add(i, 100.0);
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
