package com.example.sportmobli;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sportmobli.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.apache.commons.codec.digest.DigestUtils;

public class signup extends AppCompatActivity {

    EditText username, password, checkPassword;
    Button signup;
    FirebaseDatabase db;
    DatabaseReference userReference;
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        checkPassword = findViewById(R.id.checkPassword);
        signup = findViewById(R.id.sign_in);

        db = FirebaseDatabase.getInstance();
        userReference = db.getReference("User");


        sharedPref = getSharedPreferences("user_info", MODE_PRIVATE);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usernameString = username.getText().toString();
                String passwordString = password.getText().toString();
                String checkPasswordString = checkPassword.getText().toString();

                if (TextUtils.isEmpty(usernameString) || TextUtils.isEmpty(passwordString) || TextUtils.isEmpty(checkPasswordString)) {
                    Toast.makeText(signup.this, "Please fill in all fields!", Toast.LENGTH_SHORT).show();
                } else if (!isUsernameValid(usernameString)) {
                    Toast.makeText(signup.this, "Username must have at least 3 characters and start with a letter!", Toast.LENGTH_SHORT).show();
                } else if (!isPasswordValid(passwordString)) {
                    Toast.makeText(signup.this, "Password must have at least 6 characters, including uppercase, lowercase, and special characters.", Toast.LENGTH_SHORT).show();
                } else if (!passwordString.equals(checkPasswordString)) {
                    Toast.makeText(signup.this, "Password is not matching!", Toast.LENGTH_SHORT).show();
                } else {
                    userReference.child(usernameString).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(signup.this, "Database error.", Toast.LENGTH_SHORT).show();

                            } else {
                                if (task.getResult().getValue() == null) {
                                    String passwordHash = DigestUtils.sha256Hex(passwordString);
                                    User newUser = new User(usernameString, passwordHash);
                                    userReference.child(usernameString).setValue(newUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            sharedPref.edit().putBoolean("is_signed_in", true).apply();
                                            sharedPref.edit().putString("username", usernameString).apply();
                                            sharedPref.edit().putString("password", passwordString).apply();
                                            Toast.makeText(signup.this, "Sign up successful!", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(getApplicationContext(), login.class);
                                            startActivity(intent);
                                        }
                                    });
                                } else {
                                    Toast.makeText(signup.this, "User already exists!", Toast.LENGTH_SHORT).show();
                                }

                            }
                        }
                    });
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
        boolean hasSpecialChar = password.matches(".*[!@#$%^&*()_+{}\\[\\]:;<>,.?~\\-].*");

        return hasUppercase && hasLowercase && hasSpecialChar;
    }
}
