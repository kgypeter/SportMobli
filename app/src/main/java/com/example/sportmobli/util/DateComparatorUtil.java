package com.example.sportmobli.util;

import com.example.sportmobli.model.DietHistoryDisplay;
import com.example.sportmobli.model.TrainingHistoryDisplay;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateComparatorUtil {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public static int compareTrainingHistory(TrainingHistoryDisplay obj1, TrainingHistoryDisplay obj2) {
        try {
            Date date1 = dateFormat.parse(obj1.getAddedDate());
            Date date2 = dateFormat.parse(obj2.getAddedDate());

            // Compare the two dates
            return -date1.compareTo(date2);
        } catch (ParseException e) {
            // Handle parsing exception if necessary
            e.printStackTrace();
            return 0; // Or throw an exception, depending on your requirements
        }
    }

    public static int compareDietHistory(DietHistoryDisplay obj1, DietHistoryDisplay obj2) {
        try {
            Date date1 = dateFormat.parse(obj1.getDateAdded());
            Date date2 = dateFormat.parse(obj2.getDateAdded());

            // Compare the two dates
            return -date1.compareTo(date2);
        } catch (ParseException e) {
            // Handle parsing exception if necessary
            e.printStackTrace();
            return 0; // Or throw an exception, depending on your requirements
        }
    }


}
