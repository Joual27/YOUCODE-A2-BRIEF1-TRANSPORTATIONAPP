package com.youcode.transportationApp.utils;

public class Helper {
    public static String formatTripDuration(int totalMinutes) {
        int hours = totalMinutes / 60;
        int minutes = totalMinutes % 60;

        return String.format("%d h %d mins", hours, minutes);
    }
}
