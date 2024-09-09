package com.youcode.transportationApp.ui;

import java.util.Scanner;


import com.youcode.transportationApp.auth.AuthService;
import com.youcode.transportationApp.auth.interfaces.AuthServiceI;
import com.youcode.transportationApp.ui.subMenus.ContractMenu;
import com.youcode.transportationApp.ui.subMenus.CustomerMenu;
import com.youcode.transportationApp.ui.subMenus.PartnerMenu;
import com.youcode.transportationApp.ui.subMenus.SpecialOfferMenu;
import com.youcode.transportationApp.ui.subMenus.TicketSubMenu;

public class Menu implements MenuI{
    
    private final Scanner scanner;
    private final PartnerMenu partnerMenu;
    private final ContractMenu contractMenu;
    private final SpecialOfferMenu specialOfferMenu;
    private final TicketSubMenu ticketSubMenu;
    private final CustomerMenu customerMenu;
    private final AuthServiceI authService;

    public Menu (){
        this.scanner = new Scanner(System.in);
        this.partnerMenu = new PartnerMenu();
        this.ticketSubMenu = new TicketSubMenu();
        this.contractMenu = new ContractMenu();
        this.specialOfferMenu = new SpecialOfferMenu();
        this.customerMenu = new CustomerMenu();
        this.authService = new AuthService();
    }

    public void displayStarterMenu() {
        System.out.println("========= Starter Menu =========");
        System.out.println("1. Login as Admin");
        System.out.println("2. Access Customer Place");
        System.out.println("0. Exit From App");
        System.out.println("=============================");
    }



    public int getStarterMenuChoice() {
        int choice = -1;
        while (choice < 0 || choice > 2) {
            System.out.println("Enter your choice (1-2)");
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                if (choice < 1 || choice > 2) {
                    System.out.println("Invalid choice, please enter a number between 1 and 2.");
                }
            } else {
                System.out.println("Please enter a number.");
                scanner.next();
            }
        }
        return choice;
    }

    public boolean authenticateAdmin() {
        scanner.nextLine(); 
        System.out.println("Enter admin email:");
        String email = scanner.nextLine();
        System.out.println("Enter admin password:");
        String password = scanner.nextLine();

        if (email.equals("admin@ecomove.com") && password.equals("admin123")) {
            return true;
        } else {
            System.out.println("Invalid Credentials !");
            return false;
        }
    }

    public boolean authenticateCustomer(){
        System.out.println("Please Provide your email:");
        String email = scanner.nextLine();
        if(authService.authenticateCustomer(email) != null ){
            return true;
        }
        return false;
    }

    public void handleCustomerAuthentication(){
        if (authenticateCustomer()) {
            customerMenu.startMenu();
        }
        else{
            System.out.println("Invalid Credentials ! PLease");
        }
    }


    @Override
    public void displayMenu(){
        System.out.println("========= Main Menu =========");
        System.out.println("1. Handle Partners");
        System.out.println("2. Handle Contracts");
        System.out.println("3. Handle Tickets");
        System.out.println("4. Handle Special Offes");
        System.out.println("5. Exit");
        System.out.println("=============================");
    }


    @Override
    public int getMenuChoice(){
          int choice = -1 ;

          while(choice < 1 || choice > 5){
            System.out.println("Enter your choice ( 1-5 ) ");
            if (scanner.hasNextInt() ){
                choice = scanner.nextInt();

                if (choice < 1 || choice > 5){
                    System.out.println("Invalid choice ,PLease enter a number between 1 and 5 ");
                }
            }

            else{
                System.out.println("Please enter a number");
                scanner.next();
            }
          }
          return choice ;
    }

    @Override
    public void handleChoice(int choice ){
        switch (choice) {
            case 1:
                partnerMenu.startMenu();
                break;
            case 2:
                contractMenu.startMenu();
                break;    
            case 3:
                ticketSubMenu.startMenu();
                break;    
            case 4:
                specialOfferMenu.startMenu();
                break;
            
            case 5 :
                 return ;
            default:
                break;
        }
    }


    @Override
    public void startMenu(){
        while (true) {
            displayStarterMenu();
            int starterMenuChoice = getStarterMenuChoice();
            if (starterMenuChoice == 1) {
                if (authenticateAdmin()) {
                    int choice;
                    do {
                        displayMenu();
                        choice = getMenuChoice();
                        handleChoice(choice);
                    } while (choice != 5);
                }
                else{
                    return;
                }
                break;
            } else if(starterMenuChoice == 2){

                break;
            }
            else if(starterMenuChoice == 0){
                return;
            }
            else{
                System.out.println("Invalid choice try again");
            }
        }
    }
}
