package com.youcode.transportationApp.ui.subMenus;

import com.youcode.transportationApp.ui.MenuI;
import com.youcode.transportationApp.ui.process.ContractMenuProcess;

import java.util.Scanner;

public class ContractMenu implements MenuI{
    
    private Scanner sc;
    private final ContractMenuProcess contractMenuProcess;

    public ContractMenu(){
        sc = new Scanner(System.in);
        this.contractMenuProcess = new ContractMenuProcess();
    }


    @Override
    public void displayMenu(){
        System.out.println("========= Handle Contracts =========");
        System.out.println("1. Fetch All Contracts");
        System.out.println("2. Add Contract");
        System.out.println("3. Update Contract Data");
        System.out.println("4. delete Contract");
        System.out.println("5. Back to main menu");
    }

    
    @Override
    public int getMenuChoice(){

        int choice = -1;
        
        while (choice < 1 || choice > 5 ){
            if(sc.hasNextInt()){
                choice = sc.nextInt();

                if(choice < 1 || choice > 5){
                    System.out.println("Invalid choice ,PLease enter a number between 1 and 5 ");
                }

            }
            else{
                System.out.println("PLease enter a number");
                sc.next();
            }
        }

        return choice;
    }

    public void handleChoice(int choice){
        switch (choice) {
            case 1:
                contractMenuProcess.handleFetchingAllContracts();
                break;
            case 2 :
                contractMenuProcess.handleContractCreation();
                break;
            case 3:
                contractMenuProcess.handleContractUpdate();
                break;
            case 4:
                contractMenuProcess.handleContractDeletion();
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
