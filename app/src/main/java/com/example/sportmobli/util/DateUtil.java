package com.example.sportmobli.util;

import com.google.firebase.database.DataSnapshot;

public class DateUtil {

    public static String extractDateFromSnapshot(DataSnapshot snapshot, String dateField) {
        Integer dayAdded = snapshot.child(dateField).child("dayOfMonth").getValue(Integer.class);
        Integer monthAdded = snapshot.child(dateField).child("monthValue").getValue(Integer.class);
        Integer yearAdded = snapshot.child(dateField).child("year").getValue(Integer.class);

        Integer hourAdded = snapshot.child(dateField).child("hour").getValue(Integer.class);
        Integer minuteAdded = snapshot.child(dateField).child("minute").getValue(Integer.class);

        String dateAdded = dayAdded.toString() + "/" + monthAdded.toString() + "/" + yearAdded.toString();
        String minuteString;
        if (minuteAdded < 9) {
            minuteString = "0" + minuteAdded;
        } else {
            minuteString = minuteAdded.toString();
        }
        String timeAdded = hourAdded.toString() + ":" + minuteString;
        return dateAdded + " " + timeAdded;
    }
}
