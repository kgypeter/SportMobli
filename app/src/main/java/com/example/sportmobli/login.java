package com.example.sportmobli;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.SharedPreferences;

import com.example.sportmobli.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class login extends AppCompatActivity {

    EditText username, password;
    Button login;
    db DB;
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        login = findViewById(R.id.log_in);
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference("test2");
        ref.setValue("Hello world2!");
        DB = new db(this);
        sharedPref = getSharedPreferences("user_info", MODE_PRIVATE);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = username.getText().toString();
                String pass = password.getText().toString();

                if (TextUtils.isEmpty(user) || TextUtils.isEmpty(pass)) {
                    Toast.makeText(login.this, "Please fill in all fields!", Toast.LENGTH_SHORT).show();
                } else {
                    boolean isSignedIn = sharedPref.getBoolean("is_signed_in", true);

                    if (isSignedIn) {
                        String storedUser = sharedPref.getString("username", "");
                        String storedPass = sharedPref.getString("password", "");

                        if (DB.checkUsernamePassword(user, pass) && user.equals(storedUser) && pass.equals(storedPass)) {
                            Toast.makeText(login.this, "Log in successful!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), lol.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(login.this, "Invalid username or password!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(login.this, "You should sign in first!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}