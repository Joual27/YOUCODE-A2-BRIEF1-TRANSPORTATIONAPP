package com.youcode.transportationApp.ui.subMenus;

import java.util.Scanner;
import java.sql.SQLException;

import com.youcode.transportationApp.specialOffers.interfaces.SpecialOfferServiceI;
import com.youcode.transportationApp.ui.MenuI;

public class SpecialOfferMenu implements MenuI{

     private Scanner scanner;
    private SpecialOfferServiceI specialOfferService;

    public SpecialOfferMenu(SpecialOfferServiceI specialOfferService){
        scanner = new Scanner(System.in);
        this.specialOfferService = specialOfferService;
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
               try {
                    specialOfferService.fetchAllSpecialOffers();
                    break;
               } catch (Exception e) {
                    e.printStackTrace();
               }
            case 2:
                try {
                    specialOfferService.addSpecialOffer();
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            case 3 :
                try {
                    specialOfferService.updateSpecialOffer();
                    break;
                } catch (SQLException e) {
                    e.printStackTrace();
                    System.out.println("An error occurred while updating the partner.");
                }
            case 4 :
                try {
                    specialOfferService.deleteSpecialOffer();
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
