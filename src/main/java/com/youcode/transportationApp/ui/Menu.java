package com.youcode.transportationApp.ui;

import java.util.Scanner;

import com.youcode.transportationApp.ui.subMenus.PartnerMenu;

public class Menu implements MenuI{
    
    private Scanner scanner;
    private PartnerMenu partnerMenu;

    public Menu (){
        this.scanner = new Scanner(System.in);
        this.partnerMenu = new PartnerMenu();
    }

    
    public void displayMenu(){
        System.out.println("========= Main Menu =========");
        System.out.println("1. Handle Partners");
        System.out.println("2. Handle Contracts");
        System.out.println("3. Handle Tickets");
        System.out.println("4. Handle Special Offes");
        System.out.println("5. Exit");
        System.out.println("=============================");
    }


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

    public void handleChoice(int choice ){
        switch (choice) {
            case 1:
                partnerMenu.startMenu();
                break;
            case 2:
                System.out.println("here show contracts menu");
                break;    
            case 3:
                System.out.println("here show tickets menu");
                break;    
            case 4:
                System.out.println("here show SPecial Offers menu");
                break;
            
            case 5 :
                 return ;
            default:
                break;
        }
    }


    public void startMenu(){
        int choice;
        do{
            displayMenu();
            choice = getMenuChoice();
            handleChoice(choice);     
        }while(choice != 5);
    }


    
}
