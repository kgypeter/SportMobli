package com.example.sportmobli;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

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
        startButton.setEnabled(false);
        pauseButton.setEnabled(true); // Enable the pause button for resume
    }

    private void resumeExerciseSession() {
        resumeTimer();
        startButton.setEnabled(false);
        pauseButton.setEnabled(true);
    }
}
