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
import com.example.sportmobli.util.AppPreferences;

public class AddVeritySenseDialog {


    private static AlertDialog alertDialog;


    public static void show(Context context, String title, AddVerityDialogListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);


        View customLayout = LayoutInflater.from(context).inflate(R.layout.add_verity_layout, null);
        builder.setView(customLayout);
        builder.setTitle(title);
        String prefDeviceId = AppPreferences.getVerityDeviceId(context);
        EditText editTextName = customLayout.findViewById(R.id.deviceIdEditText);
        if (prefDeviceId != null) {
            editTextName.setText(prefDeviceId);
        }
        Button btnCancel = customLayout.findViewById(R.id.btnCancel);
        Button btnSave = customLayout.findViewById(R.id.btnSave);

        // Set button click listeners
        btnCancel.setOnClickListener(v -> {
            AppPreferences.setVerityDeviceId(context, null);
            dismissDialog(context, listener);
        });

        btnSave.setOnClickListener(view -> {
            String deviceId = editTextName.getText().toString();
            AppPreferences.setVerityDeviceId(context, deviceId);
            listener.fetchDeviceId();
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

    private static void dismissDialog(Context context, AddVerityDialogListener listener) {
        // Dismiss the dialog and hide the keyboard
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
            listener.fetchDeviceId();
            View view = alertDialog.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }

    }

    public interface AddVerityDialogListener {
        void fetchDeviceId();
    }
}
