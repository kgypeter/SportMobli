package com.example.sportmobli.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sportmobli.R;
import com.example.sportmobli.model.User;
import com.example.sportmobli.util.AppPreferences;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.apache.commons.codec.digest.DigestUtils;


public class Login extends AppCompatActivity {

    EditText username, password;

    Button Login;

    FirebaseDatabase db;

    DatabaseReference userReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        db = FirebaseDatabase.getInstance();
        userReference = db.getReference("User");
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        Login = findViewById(R.id.log_in);
        FirebaseDatabase db = FirebaseDatabase.getInstance();

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppPreferences.saveUsername(Login.this, "user");

                String usernameString = username.getText().toString();
                String passwordString = password.getText().toString();

                if (TextUtils.isEmpty(usernameString) || TextUtils.isEmpty(passwordString)) {
                    Toast.makeText(Login.this, "Please fill in all fields!", Toast.LENGTH_SHORT).show();

                } else {


                    userReference.child(usernameString).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(Login.this, "Invalid username or password!", Toast.LENGTH_SHORT).show();
                            } else {
                                User user = task.getResult().getValue(User.class);
                                if (user == null) {
                                    Toast.makeText(Login.this, "Invalid username or password!", Toast.LENGTH_SHORT).show();
                                }
                                String hashedPassword = DigestUtils.sha256Hex(passwordString);
                                if (user.getPassword().equals(hashedPassword)) {
                                    Toast.makeText(Login.this, "Log in successful!", Toast.LENGTH_SHORT).show();
                                    AppPreferences.saveUsername(Login.this, user.getUsername());

                                    Intent intent = new Intent(getApplicationContext(), Home.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(Login.this, "Invalid username or password!", Toast.LENGTH_SHORT).show();
                                }
                            }


                        }
                    });
                }
            }
        });
    }
}