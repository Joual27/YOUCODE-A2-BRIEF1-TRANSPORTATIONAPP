package com.youcode.transportationApp.ui.subMenus;

import java.util.Scanner;

import com.youcode.transportationApp.ui.MenuI;

public class CustomerMenu implements MenuI{

    private Scanner scanner;

    public CustomerMenu() {
        this.scanner = new Scanner(System.in);
    }

    public void displayMenu() {
        System.out.println("========= Customer Menu =========");
        System.out.println("1. Make a Reservation");
        System.out.println("2. View My Reservations");
        System.out.println("3. Update My Profile");
        System.out.println("4. Exit");
        System.out.println("=============================");
    }

    public int getMenuChoice() {
        int choice = -1;
        while (choice < 1 || choice > 4) {
            System.out.println("Enter your choice (1-4)");
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                if (choice < 1 || choice > 4) {
                    System.out.println("Invalid choice, please enter a number between 1 and 4.");
                }
            } else {
                System.out.println("Please enter a number.");
                scanner.next();
            }
        }
        return choice;
    }

    public void handleChoice(int choice) {
        switch (choice) {
            case 1:
                System.out.println("Making a reservation...");
                break;
            case 2:
                System.out.println("Viewing my reservations...");
                break;
            case 3:
                System.out.println("Updating your profile...");
                break;
            case 4:
                return;
            default:
                break;
        }
    }

    public void startMenu() {
        int choice;
        do {
            displayMenu();
            choice = getMenuChoice();
            handleChoice(choice);
        } while (choice != 4);
    }
}

