package com.example.sportmobli.util;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPreferences {
    private static final String PREFS_NAME = "MyPrefs";


    public static void saveUsername(Context context, String username) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("username", username);
        editor.apply();
    }

    public static String getUsername(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return preferences.getString("username", "");
    }
}
