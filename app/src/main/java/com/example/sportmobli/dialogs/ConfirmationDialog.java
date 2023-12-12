package com.example.sportmobli.dialogs;

import android.app.AlertDialog;
import android.content.Context;

public class ConfirmationDialog {

    public static void show(Context context, String title, String message, final ConfirmationDialogListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("Yes", (dialog, which) -> {
                    if (listener != null) {
                        listener.onYesClicked();
                    }
                })
                .setNegativeButton("No", (dialog, which) -> {

                })
                .show();
    }


    public interface ConfirmationDialogListener {
        void onYesClicked();
    }
}
