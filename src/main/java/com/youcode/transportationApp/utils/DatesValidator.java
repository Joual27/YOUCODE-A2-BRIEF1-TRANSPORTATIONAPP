package com.youcode.transportationApp.utils;

import java.time.LocalDate;
import java.util.Scanner;

public class DatesValidator {

    private final Scanner sc;

    public DatesValidator(){
        sc = new Scanner(System.in);
    }

    public boolean validateStartingDate(LocalDate startDate) {
        LocalDate today = LocalDate.now();
        return startDate.isAfter(today);
    }

    
    public boolean validateEndingDate(LocalDate startDate, LocalDate endDate) {
        return endDate.isAfter(startDate);
    }

    
    public int handleDays() {
        int day;
        while (true) { 
            System.out.println("PLease provide the day ");    
            day = sc.nextInt();

            if( day <= 0 || day > 31){
              System.out.println("Day cannot be outside range 0 and 31");
            }
            else{
                break;
            }
        }

        return day;
    }

    
    public int handleMonths() {
        int month;
        while (true) {
            System.out.println("Please provide the month:");
            month = sc.nextInt();

            if (month < 1 || month > 12) {
                System.out.println("Month must be between 1 and 12.");
            } else {
                break;
            }
        }
        return month;
    }


    public int handleYear() {
        int year;
        while (true) {
            System.out.println("Please provide the year:");
            year = sc.nextInt();

            if (year < 2024) {
                System.out.println("Year must be 2024 or later.");
            } else {
                break;
            }
        }
        return year;
    }

    public int handleHours(){
        int hour;
        while (true) {
            System.out.println("Please provide the hour:");
            hour = sc.nextInt();
            if (hour < 0 || hour > 23) {
                System.out.println("Hour must be between 0 and 23");
            }else{
                break;
            }
        }
        return hour;
    }

    public int handleMinutes(){
        int minutes;
        while (true) {
            System.out.println("Please provide the minutes:");
            minutes = sc.nextInt();
            if (minutes < 0 || minutes > 59) {
                System.out.println("Minutes must be between 0 and 60");
            }else{
                break;
            }
        }
        return minutes;
    }


}

