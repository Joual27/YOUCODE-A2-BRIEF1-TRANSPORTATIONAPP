package com.youcode.transportationApp.tickets;

import java.util.List;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.UUID;
import java.sql.SQLException;

import com.youcode.transportationApp.enums.DiscountType;
import com.youcode.transportationApp.enums.TicketStatus;
import com.youcode.transportationApp.enums.TransportationType;
import com.youcode.transportationApp.partners.Partner;
import com.youcode.transportationApp.partners.interfaces.PartnerServiceI;
import com.youcode.transportationApp.specialOffers.SpecialOffer;
import com.youcode.transportationApp.specialOffers.SpecialOfferRepository;
import com.youcode.transportationApp.tickets.interfaces.TicketRepositoryI;
import com.youcode.transportationApp.tickets.interfaces.TicketServiceI;
import com.youcode.transportationApp.contracts.Contract;
import com.youcode.transportationApp.contracts.ContractRepository;
import com.youcode.transportationApp.contracts.ValidContractDTO;



public class TicketService implements TicketServiceI{
    
    private final Scanner sc;
    private final TicketRepositoryI ticketRepository;
    


    public TicketService(TicketRepositoryI ticketRepository ){
       this.ticketRepository = ticketRepository;
       this.sc = new Scanner(System.in);
    }


    @Override
    public List<Ticket> getAllTickets(){
        List<Ticket> tickets = ticketRepository.getAllTickets();
        if(tickets.isEmpty()){
            return null;
        }
        return tickets;
    }
    


    @Override
    public Ticket createTicket(Ticket t) {
        ticketRepository.createTicket(t);
        return t;
    }

    @Override
    public double calculatePriceAfterSpecialRate(double initialPrice, String contractId) {
        ContractRepository cr = new ContractRepository();
        Contract c = cr.getContractById(contractId);

        return initialPrice - (initialPrice * c.getSpecialRate() / 100);
    }

  

    private void fetchAllAvailableContracts(ArrayList<ValidContractDTO> validContracts) {
        String leftAlignFormat = "| %-36s | %-12s | %-12s | %-20s | %-20s | %-15s |%n";
        System.out.format("+--------------------------------------+--------------+--------------+----------------------+----------------------+-----------------+%n");
        System.out.format("| Contract ID                          | Start Date   | End Date     | Company Name          | Transportation Type   | Special Rate    |%n");
        System.out.format("+--------------------------------------+--------------+--------------+----------------------+----------------------+-----------------+%n");
    
        for (ValidContractDTO validContract : validContracts) {
            Contract contract = validContract.getContract();
            Partner partner = validContract.getPartner();
    
            System.out.format(leftAlignFormat,
                    contract.getContractId(),
                    contract.getStartingDate(),
                    contract.getEndDate(),
                    partner.getCompanyName(),
                    partner.getTransportationType(),
                    String.format("%.2f", contract.getSpecialRate()));
        }
    
        System.out.format("+--------------------------------------+--------------+--------------+----------------------+----------------------+-----------------+%n");
    }
    


    public boolean isValidContractId(String contractId , List<ValidContractDTO> validcontracts){
        for(ValidContractDTO v : validcontracts){
            if(v.getContract().getContractId().equals(contractId)){
                return true;
            }
        }
        return false;   
    }

   

    private TransportationType handleTransportationTypeUpdate(TransportationType currentType) {
        while (true) {
            try {
                System.out.println("Current Transportation Type: " + currentType);
                System.out.println("Enter 0 to keep the current value.");
                System.out.println("1. Train\t2. Bus\t3. Airplane");

                int choice = sc.nextInt();
                sc.nextLine();

                switch (choice) {
                    case 0:
                        return currentType;
                    case 1:
                        return TransportationType.TRAIN;
                    case 2:
                        return TransportationType.BUS;
                    case 3:
                        return TransportationType.AIR;
                    default:
                        System.out.println("INVALID CHOICE! Please select 0, 1, 2, or 3.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input, please enter a number.");
                sc.nextLine();
            }
        }
    }

  
    public Ticket getTicketById(String ticketId){
        return ticketRepository.getTicketById(ticketId);
    }



    @Override
    public Ticket updateTicket(Ticket t){
        ticketRepository.editTicket(t);
        return t;
    }

    @Override
    public Ticket deleteTicket(String ticketId){
        ticketRepository.removeTicket(ticketId);
        Ticket t = getTicketById(ticketId);
        return t;
    }


    @Override 
    public Double calculatePriceAfterSpecialOffer(SpecialOffer s , Double price){
        Double finalPrice = 0.0;
        if(s.getDiscountType().equals(DiscountType.PERCENTAGE)){
             finalPrice = price - (price * s.getDiscountValue());
        }
        else if(s.getDiscountType().equals(DiscountType.FIX_AMOUNT)){
            finalPrice = price - s.getDiscountValue();
        }
        return finalPrice;
    }

}
