package com.youcode.transportationApp.ui.subMenus;


import com.youcode.transportationApp.ui.MenuI;
import com.youcode.transportationApp.ui.process.TicketMenuProcess;
import java.util.Scanner;

public class TicketSubMenu implements MenuI{
    private Scanner sc;
    private final static TicketMenuProcess ticketMenuProcess = new TicketMenuProcess();

    public TicketSubMenu(){
        sc = new Scanner(System.in);
    }


    @Override
    public void displayMenu(){
        System.out.println("========= Handle Tickets =========");
        System.out.println("1. Fetch All Tickets");
        System.out.println("2. Add Ticket");
        System.out.println("3. Update Ticket Data");
        System.out.println("4. delete Ticket");
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
                ticketMenuProcess.handleFetchingAllTickets();
                break;
            case 2 :
                ticketMenuProcess.handleTicketCreation();
                break;
            case 3:
                ticketMenuProcess.handleTicketUpdate();
                break;
            case 4:
                ticketMenuProcess.handleTicketDeletion();
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
