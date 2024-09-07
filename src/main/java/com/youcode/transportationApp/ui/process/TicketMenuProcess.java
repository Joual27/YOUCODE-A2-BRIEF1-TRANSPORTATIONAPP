package com.youcode.transportationApp.ui.process;
import java.util.Scanner;
import java.util.UUID;
import java.util.InputMismatchException;
import java.util.List;



import com.youcode.transportationApp.contracts.Contract;
import com.youcode.transportationApp.contracts.ValidContractDTO;
import com.youcode.transportationApp.enums.TicketStatus;
import com.youcode.transportationApp.enums.TransportationType;
import com.youcode.transportationApp.partners.Partner;
import com.youcode.transportationApp.specialOffers.SpecialOffer;
import com.youcode.transportationApp.specialOffers.SpecialOfferRepository;
import com.youcode.transportationApp.tickets.Ticket;
import com.youcode.transportationApp.tickets.TicketRepository;
import com.youcode.transportationApp.tickets.TicketService;
import com.youcode.transportationApp.tickets.interfaces.TicketRepositoryI;
import com.youcode.transportationApp.tickets.interfaces.TicketServiceI;

public class TicketMenuProcess {


    private static final Scanner sc = new Scanner(System.in);
   
    private final TicketRepositoryI ticketRepository;

    private final TicketServiceI ticketService;

    public TicketMenuProcess(){
       ticketRepository = new TicketRepository();
       ticketService = new TicketService(ticketRepository);
    }


    public void handleFetchingAllTickets(){
        
        List<Ticket> tickets = ticketService.getAllTickets();

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



    public void handleFetchingAllAVailableContracts(TransportationType transportationType){
        List<ValidContractDTO> activeContracts = ticketRepository.getAvailableContracts(transportationType);
        String leftAlignFormat = "| %-36s | %-12s | %-12s | %-20s | %-20s | %-15s |%n";
        System.out.format("+--------------------------------------+--------------+--------------+----------------------+----------------------+-----------------+%n");
        System.out.format("| Contract ID                          | Start Date   | End Date     | Company Name          | Transportation Type   | Special Rate    |%n");
        System.out.format("+--------------------------------------+--------------+--------------+----------------------+----------------------+-----------------+%n");
    
        for (ValidContractDTO validContract : activeContracts) {
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

    public void handleTicketCreation() {
        System.out.println("Give the ticket's transportation Type");
        PartnerMenuProcess p= new PartnerMenuProcess(); 
        TransportationType ticketTransportationType = p.handleTransportationType();

        List<ValidContractDTO> activeContracts = ticketRepository.getAvailableContracts(ticketTransportationType);
        String targetContractId = "";
        
        if(activeContracts.isEmpty()){
            System.out.println("NO available active contracts for this transportation Type , try another one !");
        }
        else{
            handleFetchingAllAVailableContracts(ticketTransportationType);
            while (true) {
                System.out.println("Please Enter the ID of the contract that this ticket belongs to :");
                String initialContractId = sc.nextLine();
                if(ticketService.isValidContractId(initialContractId, activeContracts)){
                    targetContractId = initialContractId;
                    break;
                }
                System.out.println("NO available contract with that id try again !");

            }
           
        }

        System.out.println("Please enter the company's price of this ticket (without special rate)");
        double initialPrice = sc.nextDouble();
        sc.nextLine();

        double priceAfterSpecialRate = ticketService.calculatePriceAfterSpecialRate(initialPrice, targetContractId);
        System.out.println("There is a special rate for your company, which makes your price: " + priceAfterSpecialRate);

        SpecialOfferRepository sr = new SpecialOfferRepository();
        SpecialOffer s = sr.getSpecialOfferByContractId(targetContractId);

        double finalPrice = priceAfterSpecialRate;
        if (s != null) {
            ticketService.calculatePriceAfterSpecialOffer(s, priceAfterSpecialRate);

            System.out.println("There is a special offer on that contract with type: " + s.getDiscountType() + " and value of: " + s.getDiscountValue() + " which makes your final price: " + finalPrice);
        } else {
            System.out.println("There is no available special offer right now linked with that contract.");
        }

        double sellingPrice;
        while (true) {
            System.out.println("Please enter the selling price:");
            sellingPrice = sc.nextDouble();
            sc.nextLine();

            if (sellingPrice > finalPrice) {
                break;
            } else {
                System.out.println("Selling price must be greater than the final price.");
            }
        }

        TicketStatus ticketStatus = handleTicketStatus();
        System.out.println("How many tickets of this type do you want to add?");
        int numberOfTickets = sc.nextInt();
        sc.nextLine();

        createTickets(numberOfTickets, finalPrice, sellingPrice, ticketStatus, ticketTransportationType, targetContractId);
    }

    private void createTickets(int numberOfTickets, double finalPrice, double sellingPrice, TicketStatus ticketStatus, TransportationType ticketTransportationType, String targetContractId) {
        try {
            for (int i = 0; i < numberOfTickets; i++) {
                String ticketId = UUID.randomUUID().toString();

                Ticket t = new Ticket();
                t.setTicketId(ticketId);
                t.setBoughtFor(finalPrice);
                t.setSellingPrice(sellingPrice);
                t.setTicketStatus(ticketStatus);
                t.setTransportationType(ticketTransportationType);

                Contract contract = new Contract();
                contract.setContractId(targetContractId);
                t.setContract(contract);

                Ticket createdTicket = ticketService.createTicket(t);
                System.out.println("Ticket " + createdTicket.getTicketId() + " Created Successfully !");
            }

            if (numberOfTickets == 1) {
                System.out.println("Ticket created successfully!");
            } else {
                System.out.println("Tickets created successfully!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void handleTicketUpdate(){
        handleFetchingAllTickets();
        System.out.println("Please enter the id of the ticket you want to update");

        String id = sc.nextLine();

        Ticket existingTicket = ticketService.getTicketById(id);
        if (existingTicket == null) {
            System.out.println("No ticket available with this ID");
            return;
        } else {
            System.out.println("Updating Ticket " + existingTicket.getTicketId());

            System.out.println("Current Selling Price: " + existingTicket.getSellingPrice());
            System.out.println("Enter new value or leave blank if you don't want to update!");

            String sellingPriceStr = sc.nextLine();
            double sellingPrice = sellingPriceStr.isEmpty() ? existingTicket.getSellingPrice() : Double.parseDouble(sellingPriceStr);

            PartnerMenuProcess partnerMenuProcess = new PartnerMenuProcess();

            TransportationType transportationType = partnerMenuProcess.handleTransportationTypeUpdate(existingTicket.getTransportationType());

            TicketStatus ticketStatus = handleTicketStatusUpdate(existingTicket.getTicketStatus());

            Ticket ticketToUpdate = new Ticket();
            ticketToUpdate.setTicketId(existingTicket.getTicketId());
            ticketToUpdate.setSellingPrice(sellingPrice);
            ticketToUpdate.setTransportationType(transportationType);
            ticketToUpdate.setTicketStatus(ticketStatus);
          
            Ticket updatedTicket = ticketService.updateTicket(ticketToUpdate);
            
            System.out.println("Ticket " + updatedTicket.getTicketId() + " updated successfully!");
        }
    }


    public void handleTicketDeletion(){
        handleFetchingAllTickets();
        System.out.println("Please enter the id of the ticket you want to delete");

        String id = sc.nextLine();

        Ticket existingTicket = ticketService.getTicketById(id);
        if (existingTicket == null) {
            System.out.println("No ticket is available with this ID");
            return;
        } else {
            Ticket t = ticketService.deleteTicket(id);
            System.out.println("Ticket " + t.getTicketId() + " deleted successfully!");
        }
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

}
