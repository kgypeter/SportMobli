package com.example.sportmobli.activity;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.StepMode;
import com.androidplot.xy.XYGraphWidget;
import com.androidplot.xy.XYPlot;
import com.example.sportmobli.util.HRPlotter;
import com.example.sportmobli.R;
import com.example.sportmobli.model.Exercise;
import com.example.sportmobli.util.PolarBleApiUtil;
import com.polar.sdk.api.PolarBleApi;
import com.polar.sdk.api.PolarBleApiDefaultImpl;
import com.polar.sdk.api.errors.PolarInvalidArgument;
import com.polar.sdk.api.model.PolarHrData;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.EnumSet;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;

public class ChronometerActivity extends AppCompatActivity {
    private Chronometer chronometer;
    private TextView exerciseTitleTextView;
    private Button startButton;
    private Button pauseButton;
    private Button restartButton;
    private ArrayList<Exercise> exercisesList;
    private int currentExerciseIndex = 0;
    private CountDownTimer exerciseTimer;
    private boolean isExerciseTime = true;
    private long timeRemaining;
    private boolean isPaused = false;

    //Verity Sense Stuff
    private PolarBleApi api;
    private final String deviceId = "A6FC0B2E";
    private Disposable hrDisposable;
    private HRPlotter plotter;
    private XYPlot plot;
    TextView currentHR;
    boolean verityReady = false;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chronometer);

        chronometer = findViewById(R.id.chronometer);
        exerciseTitleTextView = findViewById(R.id.exerciseTitleTextView);
        startButton = findViewById(R.id.startButton);
        pauseButton = findViewById(R.id.pauseButton);
        restartButton = findViewById(R.id.restartButton);
        restartButton.setOnClickListener(v -> restartExerciseSession());
        startButton.setVisibility(View.GONE);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("exercises")) {
            exercisesList = intent.getParcelableArrayListExtra("exercises");
        }

        startButton.setOnClickListener(v -> startExerciseSession());
        pauseButton.setOnClickListener(v -> {
            if (!isPaused) {
                pauseExerciseSession();
            } else {
                resumeExerciseSession();
            }
        });

        pauseButton.setOnClickListener(v -> {
            if (!isPaused) {
                pauseExerciseSession();
                // Change the text to "Resume" when paused
                pauseButton.setText("Resume");
            } else {
                resumeExerciseSession();
                // Change the text back to "Pause" when resumed
                pauseButton.setText("Pause");
            }
        });


        //verity sense stuff
        api = PolarBleApiDefaultImpl.defaultImplementation(
                getApplicationContext(),
                EnumSet.of(
                        PolarBleApi.PolarBleSdkFeature.FEATURE_POLAR_ONLINE_STREAMING,
                        PolarBleApi.PolarBleSdkFeature.FEATURE_BATTERY_INFO,
                        PolarBleApi.PolarBleSdkFeature.FEATURE_DEVICE_INFO
                )
        );
        api.setApiLogger(str -> Log.d("SDK", str));
        api.setApiCallback(PolarBleApiUtil.getApiSetup(deviceId, getApplicationContext(), () -> executeWhenApiReady()));

        try {
            api.connectToDevice(deviceId);
            Toast.makeText(getApplicationContext(), "Connecting to " + deviceId, Toast.LENGTH_SHORT).show();
            currentHR = findViewById(R.id.textViewCurrentHR);

            plotter = new HRPlotter();
            plot = findViewById(R.id.hr_view_plot);
            plotter.setListener(plot);
            plot.addSeries(plotter.series.getHrSeries(), plotter.hrFormatter);
            plot.addSeries(plotter.series.getRrSeries(), plotter.rrFormatter);
            plot.setRangeBoundaries(50, 100, BoundaryMode.AUTO);
            plot.setDomainBoundaries(0, 360000, BoundaryMode.AUTO);
            plot.setRangeStep(StepMode.INCREMENT_BY_VAL, 10.0);
            plot.setDomainStep(StepMode.INCREMENT_BY_VAL, 60000.0);
            plot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.LEFT).setFormat(new DecimalFormat("#"));
            plot.setLinesPerRangeLabel(2);

        } catch (PolarInvalidArgument e) {
            Toast.makeText(getApplicationContext(), "Failed to connect to " + deviceId + e, Toast.LENGTH_SHORT).show();
            throw new RuntimeException(e);
        }
    }
    private void executeWhenApiReady() {

        new Thread(() -> {

                try {
                    runOnUiThread(() -> {startButton.setVisibility(View.VISIBLE);verityReady=true;});
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

        }).start();
    }
    private void startExerciseSession() {
        if (currentExerciseIndex < exercisesList.size()) {
            Exercise currentExercise = exercisesList.get(currentExerciseIndex);
            long duration;
            String title;

            if (isExerciseTime) {
                duration = (long) ((currentExercise.getDuration() * 1000) + 1000); // Convert to milliseconds
                title = currentExercise.getName();
            } else {
                duration = (long) ((currentExercise.getRestTime() * 1000) + 1000); // Convert to milliseconds
                title = "Rest";
            }

            setExerciseTitle(title);
            startTimer(duration);
            streamHR();
            isExerciseTime = !isExerciseTime;
        } else {
            exerciseTitleTextView.setText("Exercise session finished");
        }
    }

    private void restartExerciseSession() {
        // Cancel the existing timer if running
        if (exerciseTimer != null) {
            exerciseTimer.cancel();
        }
        plot.clear();
        plotter.reset();
        plot = findViewById(R.id.hr_view_plot);
        plotter.setListener(plot);
        plot.addSeries(plotter.series.getHrSeries(), plotter.hrFormatter);
        plot.addSeries(plotter.series.getRrSeries(), plotter.rrFormatter);
        plot.setRangeBoundaries(50, 100, BoundaryMode.AUTO);
        plot.setDomainBoundaries(0, 360000, BoundaryMode.AUTO);
        plot.setRangeStep(StepMode.INCREMENT_BY_VAL, 10.0);
        plot.setDomainStep(StepMode.INCREMENT_BY_VAL, 60000.0);
        plot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.LEFT).setFormat(new DecimalFormat("#"));
        plot.setLinesPerRangeLabel(2);
        streamHR();
        currentExerciseIndex = 0;
        isExerciseTime = true;
        isPaused = false;
        startButton.setEnabled(true);
        pauseButton.setEnabled(true);
        startExerciseSession();
    }

    private void setExerciseTitle(String title) {
        exerciseTitleTextView.setText(title);
    }

    private void startTimer(long duration) {
        exerciseTimer = new CountDownTimer(duration, 1000) {
            public void onTick(long millisUntilFinished) {
                timeRemaining = millisUntilFinished;
                chronometer.setBase(SystemClock.elapsedRealtime() + millisUntilFinished);
            }

            public void onFinish() {
                if (!isPaused) {
                    // Increment currentExerciseIndex after both exercise and rest periods
                    if (!isExerciseTime) {
                        currentExerciseIndex++;
                    }
                    startExerciseSession();
                }
            }
        }.start();
        startButton.setEnabled(false);
        pauseButton.setEnabled(true);
    }

    private void pauseTimer() {
        if (exerciseTimer != null) {
            exerciseTimer.cancel();
        }
        isPaused = true;
    }

    private void resumeTimer() {
        startTimer(timeRemaining);
        isPaused = false;
    }

    private void pauseExerciseSession() {
        pauseTimer();
        if (hrDisposable != null && !hrDisposable.isDisposed()) {
            // Stop the HR stream
            hrDisposable.dispose();
            hrDisposable = null;
        }
        startButton.setEnabled(false);
        pauseButton.setEnabled(true); // Enable the pause button for resume
    }

    private void resumeExerciseSession() {
        resumeTimer();
        streamHR();
        startButton.setEnabled(false);
        pauseButton.setEnabled(true);
    }

    public void streamHR() {
        //Toast.makeText(getApplicationContext(), "Device connected " + deviceId, Toast.LENGTH_SHORT).show();
        boolean isDisposed = hrDisposable == null || hrDisposable.isDisposed();
        if (isDisposed) {
            hrDisposable = api.startHrStreaming(deviceId)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            hrData -> {
                                for (PolarHrData.PolarHrSample sample : hrData.getSamples()) {
                                    Log.d(TAG, "HR " + sample.getHr());
                                    //currentHR.setText(String.valueOf(sample.getHr()));
                                    plotter.addValues(sample);
                                }
                            },
                            error -> {
                                Log.e(TAG, "HR stream failed. Reason " + error + ". Tried on device: " + deviceId);
                                hrDisposable = null;
                                //Toast.makeText(getApplicationContext(), "Failed to connect to " + deviceId + ".\nError: " + error, Toast.LENGTH_LONG).show();
                            },
                            () -> Log.d(TAG, "HR stream complete")
                    );
        } else {
            // NOTE stops streaming if it is "running"
            hrDisposable.dispose();
            hrDisposable = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        api.shutDown();

    }
}
