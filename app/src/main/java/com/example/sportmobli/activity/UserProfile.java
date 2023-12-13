package com.example.sportmobli.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.sportmobli.R;
import com.example.sportmobli.model.Diet;
import com.example.sportmobli.model.User;

public class UserProfile extends AppCompatActivity {

    private User loggedInUser;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // Retrieve the passed information from the Intent
        Intent intent = getIntent();
        if (intent != null) {

            String username = intent.getStringExtra("USERNAME");
            String gender = intent.getStringExtra("GENDER");
            int age = intent.getIntExtra("AGE", 0); // Default value if not found
            float height = intent.getFloatExtra("HEIGHT", 0.0f);
            float weight = intent.getFloatExtra("WEIGHT", 0.0f);

            TextView usernameTextView = findViewById(R.id.usernameTextView);
            TextView genderTextView = findViewById(R.id.genderTextView);
            TextView ageTextView = findViewById(R.id.ageTextView);
            TextView heightTextView = findViewById(R.id.heightTextView);
            TextView weightTextView = findViewById(R.id.weightTextView);

            // Set the retrieved information to the TextViews
            usernameTextView.setText(username);

            // Set user attributes under the corresponding images
            genderTextView.setText(gender);
            ageTextView.setText(String.valueOf(age));
            heightTextView.setText(String.valueOf(height));
            weightTextView.setText(String.valueOf(weight));
        }


        Button lolButton = findViewById(R.id.button3);
        Button trainingButton = findViewById(R.id.button4);
        Button trackingButton = findViewById(R.id.button7);
        Button dietButton = findViewById(R.id.button5);

        lolButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Home.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });

        dietButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Diet.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });

        trainingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Training.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });

        trackingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Tracking.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });
    }
}