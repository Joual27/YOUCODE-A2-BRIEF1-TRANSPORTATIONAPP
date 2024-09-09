package com.youcode.transportationApp.ui.subMenus;

import java.util.Scanner;

import com.youcode.transportationApp.ui.MenuI;
import com.youcode.transportationApp.ui.process.SpecialOfferMenuProcess;

public class SpecialOfferMenu implements MenuI{

    private Scanner scanner;
    private SpecialOfferMenuProcess specialOfferMenuProcess;

    public SpecialOfferMenu(){
        scanner = new Scanner(System.in);
        this.specialOfferMenuProcess = new SpecialOfferMenuProcess();
    }


    
    @Override
    public void displayMenu(){
        System.out.println("========= Handle Special Offers =========");
        System.out.println("1. Fetch All SpecialOffers");
        System.out.println("2. Add Special Offer");
        System.out.println("3. Update Special Offer Data");
        System.out.println("4. delete Special Offer");
        System.out.println("5. Back to main menu");
    }



    @Override
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


    @Override
    public void handleChoice(int choice){
        switch (choice) {
            case 1:
                specialOfferMenuProcess.handleFetchingAllSpecialOffers();;
                break;
               
            case 2:
                specialOfferMenuProcess.handleSpecialOfferCreation();
                break;
            case 3 :
                specialOfferMenuProcess.handleSpecialOfferUpdate();
                break;
            case 4 :
                specialOfferMenuProcess.handleSpecialOfferDeletion();
                break;
            case 5 :
                return;    
            default:
                break;
        }
    }


    @Override
    public void startMenu(){
        int choice ;

        do{
           displayMenu();
           choice = getMenuChoice();
           handleChoice(choice);
        }while(choice != 5);
    }
}
