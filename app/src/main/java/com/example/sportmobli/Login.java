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


public class Login extends AppCompatActivity {

    EditText username, password;
    Button login;
    com.example.sportmobli.DB DB;
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        login = findViewById(R.id.log_in);
        DB = new DB(this);
        sharedPref = getSharedPreferences("user_info", MODE_PRIVATE);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = username.getText().toString();
                String pass = password.getText().toString();

                if (TextUtils.isEmpty(user) || TextUtils.isEmpty(pass)) {
                    Toast.makeText(Login.this, "Please fill in all fields!", Toast.LENGTH_SHORT).show();
                } else {
                    boolean isSignedIn = sharedPref.getBoolean("is_signed_in", true);

                    if (isSignedIn) {
                        String storedUser = sharedPref.getString("username", "");
                        String storedPass = sharedPref.getString("password", "");

                        if (DB.checkUsernamePassword(user, pass) && user.equals(storedUser) && pass.equals(storedPass)) {
                            Toast.makeText(Login.this, "Log in successful!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), Home.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(Login.this, "Invalid username or password!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(Login.this, "You should sign in first!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}