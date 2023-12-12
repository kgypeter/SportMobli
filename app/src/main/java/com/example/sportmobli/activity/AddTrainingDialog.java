package com.example.sportmobli.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sportmobli.R;
import com.example.sportmobli.model.TrainingSessionDTO;
import com.example.sportmobli.util.AppPreferences;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddTrainingDialog {

    private final Context parentContext;
    private EditText editTextName;
    private Button btnCancel, btnSave;
    private AlertDialog alertDialog;

    private FirebaseDatabase db;
    private DatabaseReference trainingSessionReference;

    public AddTrainingDialog(Context parentContext) {
        this.parentContext = parentContext;
    }

    public void show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(parentContext);

        db = FirebaseDatabase.getInstance();
        trainingSessionReference = db.getReference("TrainingSession");

        View customLayout = LayoutInflater.from(parentContext).inflate(R.layout.add_session_layout, null);
        builder.setView(customLayout);

        editTextName = customLayout.findViewById(R.id.sessionNameEditText);
        btnCancel = customLayout.findViewById(R.id.btnCancel);
        btnSave = customLayout.findViewById(R.id.btnSave);

        // Set button click listeners
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialog();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString();
                String currentUsername = AppPreferences.getUsername(parentContext);
                TrainingSessionDTO newTrainingSession = new TrainingSessionDTO();
                newTrainingSession.setName(name);
                trainingSessionReference.child(currentUsername).child(name).setValue(newTrainingSession).addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Toast.makeText(parentContext, "Error creating new session!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(parentContext, "Session saved.", Toast.LENGTH_SHORT).show();

                    }
                    dismissDialog();
                });

            }
        });

        alertDialog = builder.create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                editTextName.requestFocus();
                InputMethodManager imm = (InputMethodManager) parentContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editTextName, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

        alertDialog.show();
    }

    private void dismissDialog() {
        // Dismiss the dialog and hide the keyboard
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();

            View view = alertDialog.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) parentContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }

    }
}

