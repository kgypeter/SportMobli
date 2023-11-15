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


public class login extends AppCompatActivity {

    EditText username, password;
    Button login;
    FirebaseDatabase db;
    DatabaseReference userReference;
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        db = FirebaseDatabase.getInstance();
        userReference = db.getReference("User");
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        login = findViewById(R.id.log_in);
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference("test2");
        ref.setValue("Hello world2!");

        sharedPref = getSharedPreferences("user_info", MODE_PRIVATE);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usernameString = username.getText().toString();
                String passwordString = password.getText().toString();

                if (TextUtils.isEmpty(usernameString) || TextUtils.isEmpty(passwordString)) {
                    Toast.makeText(login.this, "Please fill in all fields!", Toast.LENGTH_SHORT).show();
                } else {
                    boolean isSignedIn = sharedPref.getBoolean("is_signed_in", true);

                    if (isSignedIn) {
                        String storedUser = sharedPref.getString("username", "");
                        String storedPass = sharedPref.getString("password", "");

                        userReference.child(usernameString).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(login.this, "Invalid username or password!", Toast.LENGTH_SHORT).show();
                                } else {
                                    User user = task.getResult().getValue(User.class);
                                    String hashedPassword = DigestUtils.sha256Hex(passwordString);
                                    if (user.getPassword().equals(hashedPassword)) {
                                        Toast.makeText(login.this, "Log in successful!", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), lol.class);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(login.this, "Invalid username or password!", Toast.LENGTH_SHORT).show();
                                    }
                                }


                            }
                        });
                    } else {
                        Toast.makeText(login.this, "You should sign in first!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}