package com.example.sportmobli.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.sportmobli.R;
import com.example.sportmobli.model.TrainingSessionDTO;
import com.example.sportmobli.util.AppPreferences;

public class AddTrainingDialog {


    private static AlertDialog alertDialog;


    public static void show(Context context, String title, final AddTrainingDialogListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);


        View customLayout = LayoutInflater.from(context).inflate(R.layout.add_session_layout, null);
        builder.setView(customLayout);

        EditText editTextName = customLayout.findViewById(R.id.sessionNameEditText);
        Button btnCancel = customLayout.findViewById(R.id.btnCancel);
        Button btnSave = customLayout.findViewById(R.id.btnSave);

        // Set button click listeners
        btnCancel.setOnClickListener(v -> dismissDialog(context));

        btnSave.setOnClickListener(v -> {
            String name = editTextName.getText().toString();
            String currentUsername = AppPreferences.getUsername(context);
            TrainingSessionDTO newTrainingSession = new TrainingSessionDTO();
            newTrainingSession.setName(name);
            newTrainingSession.setOwner(currentUsername);
            listener.saveTraining(newTrainingSession);
            dismissDialog(context);
        });


        alertDialog = builder.create();

        alertDialog.setOnShowListener(dialogInterface -> {
            editTextName.requestFocus();
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(editTextName, InputMethodManager.SHOW_IMPLICIT);
        });

        alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

        alertDialog.show();
    }

    private static void dismissDialog(Context context) {
        // Dismiss the dialog and hide the keyboard
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();

            View view = alertDialog.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }

    }

    public interface AddTrainingDialogListener {
        void saveTraining(TrainingSessionDTO trainingSessionDTO);
    }
}

