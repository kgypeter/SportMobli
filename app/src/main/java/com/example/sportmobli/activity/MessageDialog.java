package com.example.sportmobli.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class MessageDialog {
    public static void showAlertDialog(Context context, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Handle the OK button click
                        dialog.dismiss(); // Close the dialog
                    }
                });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
