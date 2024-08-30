package com.youcode.transportationApp.ui.subMenus;

import java.util.Scanner;

import com.youcode.transportationApp.ui.MenuI;

public class PartnerMenu implements MenuI{
    
    private Scanner scanner;

    public PartnerMenu(){
        scanner = new Scanner(System.in);
    }

    
    public void displayMenu(){
        System.out.println("========= Handle Partners =========");
        System.out.println("1. Fetch All Partners");
        System.out.println("2. Add Partner");
        System.out.println("3. Update Partner Data");
        System.out.println("4. delete Partner");
        System.out.println("5. Back to main menu");
    }



    public int getMenuChoice(){

        int choice = -1;
        
        while (choice < 1 || choice > 5 ){
            if(scanner.hasNextInt()){
                choice = scanner.nextInt();

                if(choice < 1 || choice > 5){
                    System.out.println("Invalid choice ,PLease enter a number between 1 and 5 ");
                }

            }
            else{
                System.out.println("PLease enter a number");
                scanner.next();
            }
        }

        return choice;
    }


    public void handleChoice(int choice){
        switch (choice) {
            case 1:
                System.out.println("========= LIst Of All Partners =========");
                break;
            default:
                break;
        }
    }


    public void startMenu(){
        int choice ;

        do{
           displayMenu();
           choice = getMenuChoice();
           handleChoice(choice);
        }while(choice != 5);
    }

}
