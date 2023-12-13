package com.example.sportmobli.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.sportmobli.R;
import com.example.sportmobli.model.Diet;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button dietButton = findViewById(R.id.button5);
        Button trainingButton = findViewById(R.id.button4);
        Button userProfileButton = findViewById(R.id.button6);
        Button trackingButton = findViewById(R.id.button7);

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

        userProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UserProfile.class);
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
