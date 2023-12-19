package com.example.sportmobli.activity;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.StepMode;
import com.androidplot.xy.XYGraphWidget;
import com.androidplot.xy.XYPlot;
import com.example.sportmobli.R;
import com.example.sportmobli.dialogs.AddVeritySenseDialog;
import com.example.sportmobli.model.Exercise;
import com.example.sportmobli.model.TrainingHistory;
import com.example.sportmobli.util.AppPreferences;
import com.example.sportmobli.util.HRPlotter;
import com.example.sportmobli.util.PolarBleApiUtil;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.polar.sdk.api.PolarBleApi;
import com.polar.sdk.api.PolarBleApiDefaultImpl;
import com.polar.sdk.api.errors.PolarInvalidArgument;
import com.polar.sdk.api.model.PolarHrData;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Locale;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;

public class ChronometerActivity extends AppCompatActivity {
    FirebaseDatabase db;
    DatabaseReference trainingHistoryReference;
    TextView currentHR;
    boolean verityReady = false;
    private String deviceId;
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
    private String sessionName;

    private TextView chronometerTextView;

    private TextToSpeech textToSpeech;

    //Verity Sense Stuff
    private PolarBleApi api;
    private Disposable hrDisposable;
    private HRPlotter plotter;
    private XYPlot plot;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chronometer);
        db = FirebaseDatabase.getInstance();
        trainingHistoryReference = db.getReference("TrainingHistory");

        chronometerTextView = findViewById(R.id.chronometerTextView);
        exerciseTitleTextView = findViewById(R.id.exerciseTitleTextView);

        // Initialize TextToSpeech for reading exercise titles aloud
        textToSpeech = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                int result = textToSpeech.setLanguage(Locale.US); // Set language

                if (result == TextToSpeech.LANG_MISSING_DATA ||
                        result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("TTS", "Language not supported");
                }
            } else {
                Log.e("TTS", "Initialization failed");
            }
        });

        chronometerTextView = findViewById(R.id.chronometerTextView);
        exerciseTitleTextView = findViewById(R.id.exerciseTitleTextView);
        startButton = findViewById(R.id.startButton);
        pauseButton = findViewById(R.id.pauseButton);
        restartButton = findViewById(R.id.restartButton);
        restartButton.setOnClickListener(v -> restartExerciseSession());
        startButton.setVisibility(View.GONE);
        AddVeritySenseDialog.show(this, "If you want to use a Verity Sense device, enter its id. If not, click cancel.", new AddVeritySenseDialog.AddVerityDialogListener() {
            @Override
            public void fetchDeviceId() {
                showTimerLayout();
            }
        });

        // Retrieve exercises from intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("exercises")) {
            exercisesList = intent.getParcelableArrayListExtra("exercises");
            sessionName = intent.getExtras().get("sessionName").toString();
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


    }

    private void showTimerLayout() {
        deviceId = AppPreferences.getVerityDeviceId(this);
        if (deviceId == null || deviceId.equals("")) {
            executeWhenApiReady();
        } else {
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
                plot.setRangeBoundaries(50, 100, BoundaryMode.AUTO);
                plot.setDomainBoundaries(0, 360000, BoundaryMode.AUTO);
                plot.setRangeStep(StepMode.INCREMENT_BY_VAL, 10.0);
                plot.setDomainStep(StepMode.INCREMENT_BY_VAL, 60000.0);
                plot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.LEFT).setFormat(new DecimalFormat("#"));
                plot.setLinesPerRangeLabel(2);

            } catch (PolarInvalidArgument e) {
                Toast.makeText(getApplicationContext(), "Failed to connect to " + deviceId + e, Toast.LENGTH_SHORT).show();
                executeWhenApiReady();
                throw new RuntimeException(e);
            }
        }
    }

    private void executeWhenApiReady() {

        new Thread(() -> {

            try {
                runOnUiThread(() -> {
                    startButton.setVisibility(View.VISIBLE);
                    verityReady = true;
                });
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }).start();
    }

    private void startExerciseSession() {
        if (deviceId != null && !deviceId.equals("")) {
            streamHR();
        }
        exerciseTimerRecursion();
    }

    private void exerciseTimerRecursion() {

        if (currentExerciseIndex < exercisesList.size()) {
            Exercise currentExercise = exercisesList.get(currentExerciseIndex);
            long duration;
            String title;

            // Determine duration and title based on exercise/rest time
            if (isExerciseTime) {
                duration = (long) ((currentExercise.getDuration() * 1000) + 1000);
                title = currentExercise.getName();
            } else {
                duration = (long) ((currentExercise.getRestTime() * 1000) + 1000);
                title = "Rest";

            }

            setExerciseTitle(title);

            // Speak the text from exerciseTitleTextView
            speakText(title);

            startTimer(duration);
            isExerciseTime = !isExerciseTime;
        } else {
            endSession();
        }
    }

    private void endSession() {
        exerciseTitleTextView.setText("Exercise session finished");
        if (hrDisposable != null && !hrDisposable.isDisposed()) {
            // Stop the HR stream and save the session to firebase
            saveSession();
            hrDisposable.dispose();
            hrDisposable = null;
        }
    }

    private void saveSession() {
        String currentUsername = AppPreferences.getUsername(this);
        TrainingHistory trainingHistoryEntry = new TrainingHistory();
        LocalDateTime dateAdded = LocalDateTime.now();
        trainingHistoryEntry.setAddedDate(dateAdded);
        trainingHistoryEntry.setSessionName(sessionName);
        trainingHistoryEntry.setOwner(currentUsername);

        Map<String, Double> hrHistory = new HashMap<>();
        List<Double> yVals = plotter.series.getyHrVals();
        for (Integer i = 0; i < yVals.size(); i++) {
            hrHistory.put(i.toString(), yVals.get(i));

        }
        trainingHistoryEntry.setHrHistory(hrHistory);
        String uuid = UUID.randomUUID().toString();
        trainingHistoryReference.child(currentUsername).child(uuid).setValue(trainingHistoryEntry);


    }

}
    }

    private void setExerciseTitle(String title) {
        exerciseTitleTextView.setText(title);
    }

    // Method to speak the text
    private void speakText(String text) {
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
    }

    private void restartExerciseSession() {
        // Cancel the existing timer if running
        if (exerciseTimer != null) {
            exerciseTimer.cancel();
        }

        currentExerciseIndex = 0;
        isExerciseTime = true;
        isPaused = false;
        startButton.setEnabled(true);
        pauseButton.setEnabled(true);
        startExerciseSession();
    }

    private void startTimer(long duration) {
        exerciseTimer = new CountDownTimer(duration, 1000) {
            public void onTick(long millisUntilFinished) {
                timeRemaining = millisUntilFinished;
                updateChronometerText(millisUntilFinished);
            }

            public void onFinish() {
                if (!isPaused) {
                    if (!isExerciseTime) {
                        currentExerciseIndex++;
                    }
                    exerciseTimerRecursion();
                }
            }
        }.start();
        startButton.setEnabled(false);
        pauseButton.setEnabled(true);
    }

    private void updateChronometerText(long millisUntilFinished) {
        int seconds = (int) (millisUntilFinished / 1000);
        int minutes = seconds / 60;
        seconds = seconds % 60;

        String time = String.format("%02d:%02d", minutes, seconds);
        chronometerTextView.setText(time);
    }


    private void pauseExerciseSession() {
        pauseTimer();
        if (hrDisposable != null && !hrDisposable.isDisposed()) {
            // Stop the HR stream
//            hrDisposable.dispose();
//            hrDisposable = null;
        }
        startButton.setEnabled(false);
        pauseButton.setEnabled(true);
    }

    private void resumeExerciseSession() {
        resumeTimer();
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

    // Override onDestroy method to release MediaPlayer resources when activity is destroyed
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Release TextToSpeech resources
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }

}
