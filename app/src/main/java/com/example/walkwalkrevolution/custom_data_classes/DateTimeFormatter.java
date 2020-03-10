package com.example.walkwalkrevolution.custom_data_classes;

import android.content.Intent;
import android.icu.util.Calendar;
import android.util.Log;

import java.text.SimpleDateFormat;

/**
 * Date class for formatting Date strings.
 */
public class DateTimeFormatter {

    // Public Constants for extractMonthDayYear()
    public static final int MONTH_INDEX = 0, DAY_INDEX = 1, YEAR_INDEX = 2;


    /**
     * @return The current date as a String MM/dd/yyyy
     */
    public static String getCurrentDate(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        java.util.Date currentTime = Calendar.getInstance().getTime();
        String date = dateFormat.format(currentTime);

        return date;
    }

    /**
     * @return From a date string formatted by DateFormatter, extract an int[] where:
     * int[MONTH_INDEX] = month
     * int[DAY_INDEX] = day
     * int[YEAR_INDEX] = year
     */
    public static int[] extractMonthDayYear(String date) {
        int month = Integer.parseInt(date.substring(0,2)) - 1;
        int day = Integer.parseInt(date.substring(3,5));
        int year = Integer.parseInt(date.substring(6, date.length()));

        int[] mdy = {month, day, year};

        return mdy;
    }

    /**
     * Format inputted ints into a String indicating the date.
     */
    public static String formatMonthDayYear(int month, int day, int year) {
        // Handle the case for single digit months/days
        String monthStr = month + "";
        if (monthStr.length() < 2) {
            monthStr = "0" + monthStr;
        }
        String dayStr = day + "";
        if (dayStr.length() < 2) {
            dayStr = "0" + dayStr;
        }

        String date = monthStr + "/" + dayStr + "/" + year;

        return date;
    }


    // TIME FORMATTING -----------------------------------------------------------------

    /**
     * Format inputted ints (from a time spinner) into a String indicating the time.
     * Inputted hours are from 0-23.
     * Inputted minutes are from 0-59.
     */
    public static String formatTime(int hours, int minutes) {
        String amOrPm;
        if ((int) hours / 2 == 0) {
            amOrPm = "AM";
        } else {
            amOrPm = "PM";
        }

        String hoursStr;
        if (hours % 12 == 0) {
            hoursStr = "12";
        } else {
            hoursStr = hours % 12 + "";
        }

        String minuteStr = minutes + "";
        if (minuteStr.length() < 2) {
            minuteStr += "0";
        }

        String time = hoursStr + ":" + minuteStr + " " + amOrPm;

        return time;
    }

}
