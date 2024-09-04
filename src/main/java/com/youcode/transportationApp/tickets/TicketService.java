package com.youcode.transportationApp.tickets;

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
    private final PartnerServiceI partnerService;


    public TicketService(TicketRepositoryI ticketRepository , PartnerServiceI partnerService){
       this.ticketRepository = ticketRepository;
       this.partnerService = partnerService;
       this.sc = new Scanner(System.in);
    }


    @Override
    public void fetchAllTickets() throws SQLException{
        ArrayList<Ticket> tickets = ticketRepository.getAllTickets();

        if (tickets.isEmpty()) {
            System.out.println("No available tickets");
            return;
        }
    
        String leftAlignFormat = "| %-36s | %-25s | %-15s | %-15s | %-20s |%n";
    
        System.out.format("+--------------------------------------+---------------------------+-----------------+-----------------+----------------------+%n");
        System.out.format("| Ticket ID                            | Bought for                | Selling Price   | Ticket Status   | Transportation Type  |");
        System.out.format("+--------------------------------------+---------------------------+-----------------+-----------------+----------------------+%n");
    
        for (Ticket ticket : tickets) {
            System.out.format(leftAlignFormat,
                    ticket.getTicketId(), 
                    ticket.getBoughtFor(), 
                    String.format("$%.2f", ticket.getSellingPrice()), 
                    ticket.getTicketStatus(), // 
                    ticket.getTransportationType()
            );   
                    
        }
    
        System.out.format("+--------------------------------------+---------------------------+-----------------+-----------------+----------------------+-----------------+%n");
    }
    


    @Override
    public void createTicket() throws SQLException{
      
        System.out.println("Give the tickets transportation Type");
        TransportationType ticketTransportationType =  partnerService.handleTransportationType();
        String targetContractId = getContractIdFromAvailableContracts(ticketTransportationType);
        System.out.println("PLease enter the company's price of this ticket (without special rate)");
        double initialPrice = sc.nextDouble();
        sc.nextLine();

        ContractRepository cr = new ContractRepository();
        Contract c = cr.getContractById(targetContractId);

        double priceAfterSpecialRateAndSpecialOffer = initialPrice - (initialPrice * c.getSpecialRate() / 100);

        System.out.println("There is a special rate for your company of : " + c.getSpecialRate() +  "Which makes your price :" + priceAfterSpecialRateAndSpecialOffer);

        SpecialOfferRepository sr = new SpecialOfferRepository();

        SpecialOffer s = sr.getSpecialOfferByContractId(targetContractId);
        if(s == null){
            System.out.println("THere is no available special offer right now linked with that contract");
        }
        else{
            if(s.getDiscountType().equals(DiscountType.PERCENTAGE)){
                priceAfterSpecialRateAndSpecialOffer = priceAfterSpecialRateAndSpecialOffer - (priceAfterSpecialRateAndSpecialOffer * s.getDiscountValue());
            }
            else{
                priceAfterSpecialRateAndSpecialOffer = priceAfterSpecialRateAndSpecialOffer - s.getDiscountValue();
            }

            System.out.println("There is a special offer on that contract with type : " + s.getDiscountType() + " and value of : " + s.getDiscountValue() + " which makes your final price : " + priceAfterSpecialRateAndSpecialOffer);
        }

        System.out.println("Please enter the selling price ");
        double sellingPrice = sc.nextDouble();
        sc.nextLine();
        
        TicketStatus ticketStatus = handleTicketStatus();
        
        System.out.println("How Many tickets of this type you want to add ? ");

        int numberOfTickets = sc.nextInt();

        sc.nextLine();
        
        try {
            for(int i=0 ; i < numberOfTickets ; i++){
                String ticketId = UUID.randomUUID().toString();
                
                Ticket t = new Ticket();
                t.setTicketId(ticketId);
                t.setBoughtFor(priceAfterSpecialRateAndSpecialOffer);
                t.setSellingPrice(sellingPrice);
                t.setTicketStatus(ticketStatus);
                t.setTransportationType(ticketTransportationType);
                Contract contract = new Contract();
                contract.setContractId(targetContractId);
                t.setContract(contract);
                
                ticketRepository.createTicket(t);
            }
             
            if(numberOfTickets == 1){
                System.out.println("Ticket created successfully !");
            }
            else{
                System.out.println("Tickets created Successfully !");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        

    }

    @Override
    public String getContractIdFromAvailableContracts(TransportationType ticketTransportationType){
        ArrayList<ValidContractDTO> activeContracts = ticketRepository.getAvailableContracts(ticketTransportationType);
        String contractId = "";
        if(activeContracts.isEmpty()){
            System.out.println("NO available active contracts for this transportation Type , try another one !");
        }

        else{
            fetchAllAvailableContracts(activeContracts);
            while (true) {
                System.out.println("Please Enter the ID of the contract that this ticket belongs to :");
                String initialContractId = sc.nextLine();
                if(isValidContractId(initialContractId, activeContracts)){
                    contractId = initialContractId;
                    break;
                }
                System.out.println("NO available contract with that id try again !");

            }
           
        }

        return contractId;

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
    


    private boolean isValidContractId(String contractId , ArrayList<ValidContractDTO> validcontracts){
        for(ValidContractDTO v : validcontracts){
            if(v.getContract().getContractId().equals(contractId)){
                return true;
            }
        }
        return false;
    }

    private TicketStatus handleTicketStatus() {
    while (true) {
        System.out.println("Select Ticket Status:");
        System.out.println("1. Sold");
        System.out.println("2. Cancelled");
        System.out.println("3. Pending");

        try {
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    return TicketStatus.SOLD;
                case 2:
                    return TicketStatus.CANCELLED;
                case 3:
                    return TicketStatus.PENDING;
                default:
                    System.out.println("INVALID CHOICE!");
                    break;
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input, please enter a number.");
            sc.nextLine();  
        }
     }
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

    private TicketStatus handleTicketStatusUpdate(TicketStatus currentTicketStatus) {
        while (true) {
            try {
                System.out.println("Current Ticket Status: " + currentTicketStatus);
                System.out.println("Enter 0 to keep the current value.");
                System.out.println("1. Sold\t2. Cancelled\t3. Pending");

                int choice = sc.nextInt();
                sc.nextLine();

                switch (choice) {
                    case 0:
                        return currentTicketStatus;
                    case 1:
                        return TicketStatus.SOLD;
                    case 2:
                        return TicketStatus.CANCELLED;
                    case 3:
                        return TicketStatus.PENDING;
                    default:
                        System.out.println("INVALID CHOICE!");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input, please enter a number.");
                sc.nextLine();
            }
        }
    }


    @Override
    public void updateTicket() throws SQLException {
        fetchAllTickets();
        System.out.println("Please enter the id of the ticket you want to update");

        String id = sc.nextLine();

        Ticket existingTicket = ticketRepository.getTicketById(id);
        if (existingTicket == null) {
            System.out.println("No ticket available with this ID");
            return;
        } else {
            System.out.println("Updating Ticket " + existingTicket.getTicketId());

            System.out.println("Current Selling Price: " + existingTicket.getSellingPrice());
            System.out.println("Enter new value or leave blank if you don't want to update!");

            String sellingPriceStr = sc.nextLine();
            double sellingPrice = sellingPriceStr.isEmpty() ? existingTicket.getSellingPrice() : Double.parseDouble(sellingPriceStr);

            TransportationType transportationType = handleTransportationTypeUpdate(existingTicket.getTransportationType());

            TicketStatus ticketStatus = handleTicketStatusUpdate(existingTicket.getTicketStatus());

            Ticket ticketToUpdate = new Ticket();
            ticketToUpdate.setTicketId(existingTicket.getTicketId());
            ticketToUpdate.setSellingPrice(sellingPrice);
            ticketToUpdate.setTransportationType(transportationType);
            ticketToUpdate.setTicketStatus(ticketStatus);

            try {
                ticketRepository.editTicket(ticketToUpdate);
                System.out.println("Ticket " + ticketToUpdate.getTicketId() + " updated successfully!");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void deleteTicket() throws SQLException{
        fetchAllTickets();

        System.out.println("Please enter the id of the ticket you want to delete");

        String id = sc.nextLine();

        try {
            Ticket existingTicket = ticketRepository.getTicketById(id);
            if (existingTicket == null) {
                System.out.println("No ticket is available with this ID");
                return;
            } else {
                ticketRepository.removeTicket(id);
                System.out.println("Ticket " + existingTicket.getTicketId() + " deleted successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
