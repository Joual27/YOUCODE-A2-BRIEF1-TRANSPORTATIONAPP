package com.youcode.transportationApp.ui.subMenus;


import com.youcode.transportationApp.tickets.interfaces.TicketServiceI;
import com.youcode.transportationApp.ui.MenuI;

import java.sql.SQLException;
import java.util.Scanner;

public class TicketSubMenu implements MenuI{
    private Scanner sc;
    private final TicketServiceI ticketService;

    public TicketSubMenu(TicketServiceI ticketService){
        sc = new Scanner(System.in);
        this.ticketService = ticketService;
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
                try {
                    ticketService.fetchAllTickets();
                    break;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            case 2 :
                try {
                    ticketService.createTicket();
                    break; 
                } catch (SQLException e) {
                   e.printStackTrace();
                }  
            case 3:
                try {
                    ticketService.updateTicket();
                    break;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            case 4:
                try {
                    ticketService.deleteTicket();   
                    break;
                } catch (SQLException e) {
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
