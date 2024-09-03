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
}

