package com.youcode.transportationApp.ui;

import java.sql.SQLException;
import java.util.Scanner;

import com.youcode.transportationApp.contracts.interfaces.ContractRepositoryI;
import com.youcode.transportationApp.contracts.interfaces.ContractServiceI;
import com.youcode.transportationApp.contracts.ContractRepository;
import com.youcode.transportationApp.contracts.ContractService;
import com.youcode.transportationApp.partners.PartnerRepository;
import com.youcode.transportationApp.partners.PartnerService;
import com.youcode.transportationApp.partners.interfaces.PartnerRepositoryI;
import com.youcode.transportationApp.partners.interfaces.PartnerServiceI;
import com.youcode.transportationApp.specialOffers.SpecialOfferRepository;
import com.youcode.transportationApp.specialOffers.SpecialOfferService;
import com.youcode.transportationApp.specialOffers.interfaces.SpecialOfferRepositoryI;
import com.youcode.transportationApp.specialOffers.interfaces.SpecialOfferServiceI;
import com.youcode.transportationApp.tickets.TicketRepository;
import com.youcode.transportationApp.tickets.TicketService;
import com.youcode.transportationApp.tickets.interfaces.TicketRepositoryI;
import com.youcode.transportationApp.tickets.interfaces.TicketServiceI;
import com.youcode.transportationApp.ui.subMenus.ContractMenu;
import com.youcode.transportationApp.ui.subMenus.PartnerMenu;
import com.youcode.transportationApp.ui.subMenus.SpecialOfferMenu;
import com.youcode.transportationApp.ui.subMenus.TicketSubMenu;

public class Menu implements MenuI{
    
    private Scanner scanner;
    private PartnerMenu partnerMenu;
    private ContractMenu contractMenu;
    private SpecialOfferMenu specialOfferMenu;
    private TicketSubMenu ticketSubMenu;

    public Menu (){
        this.scanner = new Scanner(System.in);
        try{
            PartnerRepositoryI partnerRepository = new PartnerRepository();
            PartnerServiceI partnerService = new PartnerService(partnerRepository);
            this.partnerMenu = new PartnerMenu(partnerService);

            ContractRepositoryI contractRepository = new ContractRepository();
            ContractServiceI contractService = new ContractService(contractRepository , partnerRepository);
            this.contractMenu = new ContractMenu(contractService);


            SpecialOfferRepositoryI specialOfferRepository = new SpecialOfferRepository();
            SpecialOfferServiceI specialOfferService  = new SpecialOfferService(specialOfferRepository);
            this.specialOfferMenu = new SpecialOfferMenu(specialOfferService);



            TicketRepositoryI ticketRepository = new TicketRepository();
            TicketServiceI ticketService = new TicketService(ticketRepository, partnerService);
            this.ticketSubMenu = new TicketSubMenu(ticketService);
        }
        catch(SQLException e){
            e.printStackTrace();
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
        int choice;
        do{
            displayMenu();
            choice = getMenuChoice();
            handleChoice(choice);     
        }while(choice != 5);
    }


    
}
