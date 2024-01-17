package com.example.sportmobli.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.androidplot.xy.XYPlot;
import com.example.sportmobli.R;
import com.example.sportmobli.util.HRPlotter;

import java.util.HashMap;
import java.util.Map;

public class PlotActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plot);

        // Get data from the intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            HRPlotter plotter = new HRPlotter();
            XYPlot plot = findViewById(R.id.plot);
            plotter.setListener(plot);

            Bundle bundle = getIntent().getBundleExtra("hrSeries");
            if (bundle != null) {
                // Retrieve the Map from the Bundle
                Map<String, Double> hrHistory = new HashMap<>();
                for (String key : bundle.keySet()) {
                    hrHistory.put(key, bundle.getDouble(key));
                }

                // hrHistory Map containing the data from the previous activity
                plotter.setListener(plot);
                plot.addSeries(plotter.series.getHrSeries(), plotter.hrFormatter);
                if (hrHistory != null) {
                    for (int i = 5; i < hrHistory.size(); i++) {
                        double value = hrHistory.get(String.valueOf(i));
                        plotter.addValuesManual((long) i, value);
                    }
                }
            }
        }
    }

}
