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


public class Login extends AppCompatActivity {

    EditText username, password;
    Button Login;

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
        Login = findViewById(R.id.log_in);
        FirebaseDatabase db = FirebaseDatabase.getInstance();

        sharedPref = getSharedPreferences("user_info", MODE_PRIVATE);

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usernameString = username.getText().toString();
                String passwordString = password.getText().toString();

                if (TextUtils.isEmpty(usernameString) || TextUtils.isEmpty(passwordString)) {
                    Toast.makeText(Login.this, "Please fill in all fields!", Toast.LENGTH_SHORT).show();

                } else {

//                        String storedUser = sharedPref.getString("username", "");
//                        String storedPass = sharedPref.getString("password", "");

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