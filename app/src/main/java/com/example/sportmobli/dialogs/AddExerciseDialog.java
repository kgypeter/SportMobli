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
import com.example.sportmobli.model.Exercise;

public class AddExerciseDialog {

    private static AlertDialog alertDialog;


    public static void show(Context context, String title, final AddExerciseDialogListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        View customLayout = LayoutInflater.from(context).inflate(R.layout.add_exercise_layout, null);
        builder.setView(customLayout);

        EditText editTextName = customLayout.findViewById(R.id.exerciseNameEditText);
        EditText editTextDuration = customLayout.findViewById(R.id.exerciseDurationEditText);
        EditText editTextRestTime = customLayout.findViewById(R.id.exerciseRestTimeTextView);


        Button btnCancel = customLayout.findViewById(R.id.btnCancel);
        Button btnSave = customLayout.findViewById(R.id.btnSave);

        // Set button click listeners
        btnCancel.setOnClickListener(v -> dismissDialog(context));

        btnSave.setOnClickListener(v -> {
            String name = editTextName.getText().toString();
            Float duration = Float.parseFloat(editTextDuration.getText().toString());
            Float restTime = Float.parseFloat(editTextRestTime.getText().toString());

            Exercise newExercise = new Exercise();
            newExercise.setName(name);
            newExercise.setDuration(duration);
            newExercise.setRestTime(restTime);

            listener.saveExercise(newExercise);
            alertDialog.dismiss();

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

    public interface AddExerciseDialogListener {
        void saveExercise(Exercise exercise);

    }
}