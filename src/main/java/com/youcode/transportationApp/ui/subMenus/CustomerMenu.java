package com.youcode.transportationApp.ui.subMenus;

import java.util.Scanner;

import com.youcode.transportationApp.ui.MenuI;
import com.youcode.transportationApp.ui.process.CustomerMenuProcess;
import com.youcode.transportationApp.ui.process.TicketReservationProcess;
import com.youcode.transportationApp.utils.Session;

public class CustomerMenu implements MenuI{

    private Scanner scanner;
    private final static CustomerMenuProcess customerMenuProcess = new CustomerMenuProcess();
    private final TicketReservationProcess ticketReservationProcess = new TicketReservationProcess();

    public CustomerMenu() {
        this.scanner = new Scanner(System.in);
    }

    public void displayMenu() {
        System.out.println("========= Customer Menu =========");
        System.out.println("1. Make a Reservation");
        System.out.println("2. View My Reservations");
        System.out.println("3. Cancel a Reservation");
        System.out.println("4. Update My Profile");
        System.out.println("5. Exit");
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
                ticketReservationProcess.handleTicketSearchingProcess();
                break;
            case 2:
                ticketReservationProcess.handleFetchingCustomerReservations();
                break;
            case 3:
                ticketReservationProcess.handleReservationCancellation();
                break;
            case 4:
                customerMenuProcess.handleCustomerProfileUpdate();
                break;

            case 5:
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

