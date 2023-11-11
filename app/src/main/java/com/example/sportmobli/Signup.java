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

public class Signup extends AppCompatActivity {

    EditText username, password, cPassword;
    Button signup;
    com.example.sportmobli.DB DB;
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        cPassword = findViewById(R.id.cPassword);
        signup = findViewById(R.id.sign_in);

        DB = new DB(this);
        sharedPref = getSharedPreferences("user_info", MODE_PRIVATE);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = username.getText().toString();
                String pass = password.getText().toString();
                String cPass = cPassword.getText().toString();

                if (TextUtils.isEmpty(user) || TextUtils.isEmpty(pass) || TextUtils.isEmpty(cPass)) {
                    Toast.makeText(Signup.this, "Please fill in all fields!", Toast.LENGTH_SHORT).show();
                } else if (!isUsernameValid(user)) {
                    Toast.makeText(Signup.this, "Username must have at least 3 characters and start with a letter!", Toast.LENGTH_SHORT).show();
                } else if (!isPasswordValid(pass)) {
                    Toast.makeText(Signup.this, "Password must have at least 6 characters, including uppercase, lowercase, digit and special character.", Toast.LENGTH_SHORT).show();
                } else if (!pass.equals(cPass)) {
                    Toast.makeText(Signup.this, "Password is not matching!", Toast.LENGTH_SHORT).show();
                } else {
                    Boolean checkUser = DB.checkUsername(user);
                    if (!checkUser) {
                        Boolean insert = DB.insertData(user, pass);
                        if (insert) {
                            sharedPref.edit().putBoolean("is_signed_in", true).apply();
                            sharedPref.edit().putString("username", user).apply();
                            sharedPref.edit().putString("password", pass).apply();
                            Toast.makeText(Signup.this, "Sign up successful!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), Login.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(Signup.this, "Please try again!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(Signup.this, "This username already exists!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    private boolean isUsernameValid(String username) {
        if (username.length() < 3) {
            return false;
        }
        char firstChar = username.charAt(0);
        return Character.isLetter(firstChar);
    }

    private boolean isPasswordValid(String password) {
        if (password.length() < 6) {
            return false;
        }

        boolean hasUppercase = !password.equals(password.toLowerCase());
        boolean hasLowercase = !password.equals(password.toUpperCase());
        boolean hasSpecialChar = password.matches(".*[!@#$%^&*()_+{}\\[\\]:;<>,.?~\\-].*");;
        boolean hasDigit = password.matches(".*\\d.*");

        return hasUppercase && hasLowercase && hasSpecialChar && hasDigit;
    }
}
