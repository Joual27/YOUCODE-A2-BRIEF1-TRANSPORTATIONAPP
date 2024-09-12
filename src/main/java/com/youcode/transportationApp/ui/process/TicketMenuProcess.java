package com.youcode.transportationApp.ui.process;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.UUID;
import java.util.InputMismatchException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import com.youcode.transportationApp.contracts.Contract;
import com.youcode.transportationApp.contracts.ValidContractDTO;
import com.youcode.transportationApp.enums.TicketStatus;
import com.youcode.transportationApp.enums.TransportationType;
import com.youcode.transportationApp.partners.Partner;
import com.youcode.transportationApp.route.Route;
import com.youcode.transportationApp.route.RouteService;
import com.youcode.transportationApp.route.interfaces.RouteServiceI;
import com.youcode.transportationApp.specialOffers.SpecialOffer;
import com.youcode.transportationApp.specialOffers.SpecialOfferRepository;
import com.youcode.transportationApp.tickets.Ticket;
import com.youcode.transportationApp.tickets.TicketRepository;
import com.youcode.transportationApp.tickets.TicketService;
import com.youcode.transportationApp.tickets.interfaces.TicketRepositoryI;
import com.youcode.transportationApp.tickets.interfaces.TicketServiceI;
import com.youcode.transportationApp.utils.DatesValidator;
import com.youcode.transportationApp.utils.Helper;

public class TicketMenuProcess {


    private static final Scanner sc = new Scanner(System.in);
   
    private final TicketRepositoryI ticketRepository;
    private final TicketServiceI ticketService;
    private final DatesValidator validator = new DatesValidator();

    public TicketMenuProcess(){
       ticketRepository = new TicketRepository();
       ticketService = new TicketService(ticketRepository);
    }


    public void handleFetchingAllTickets() {
        List<Ticket> tickets = ticketService.getAllTickets();

        if (tickets.isEmpty()) {
            System.out.println("No available tickets");
            return;
        }

        String leftAlignFormat = "| %-36s | %-25s | %-15s | %-15s | %-20s | %-20s | %-20s | %-10s | %-20s | %-15s |%n";

        System.out.format("+--------------------------------------+---------------------------+-----------------+-----------------+----------------------+----------------------+----------------------+------------+----------------------+-----------------+%n");
        System.out.format("| Ticket ID                            | Bought for                | Selling Price   | Ticket Status   | Transportation Type  | Departure            | Destination          | Distance   | Departure Date       | Trip Duration   |%n");
        System.out.format("+--------------------------------------+---------------------------+-----------------+-----------------+----------------------+----------------------+----------------------+------------+----------------------+-----------------+%n");

        for (Ticket ticket : tickets) {
            System.out.format(leftAlignFormat,
                    ticket.getTicketId(),
                    ticket.getBoughtFor(),
                    String.format("$%.2f", ticket.getSellingPrice()),
                    ticket.getTicketStatus(),
                    ticket.getTransportationType(),
                    ticket.getRoute().getDeparture(),
                    ticket.getRoute().getDestination(),
                    String.format("%.2f km", ticket.getRoute().getDistance()),
                    ticket.getDepartureDate().toString(),
                    Helper.formatTripDuration(ticket.getTripDuration())
            );
        }

        System.out.format("+--------------------------------------+---------------------------+-----------------+-----------------+----------------------+----------------------+----------------------+------------+----------------------+-----------------+%n");
    }




    public void handleFetchingAllAvailableContracts(TransportationType transportationType){
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
            handleFetchingAllAvailableContracts(ticketTransportationType);
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

        System.out.println("Please provide the ticket's route infos !");
        System.out.println("Please enter the departure :");
        String departure = sc.nextLine();
        System.out.println("Please enter the destination :");
        String destination = sc.nextLine();
        Double distance;
        while (true){
            System.out.println("Please enter the distance of the route (in KM) :");
            distance = sc.nextDouble();
            sc.nextLine();
            if(distance > 0){
                break;
            }
            else {
                System.out.println("Distance can't be a negative value !");
            }
        }

        RouteServiceI routeService = new RouteService();
        Route existingRoute = routeService.getRouteByDepartureAndDestination(departure,destination);

        Route ticketRoute = new Route();

        if(existingRoute == null){
            Route route = new Route();
            route.setRouteId(UUID.randomUUID().toString());
            route.setDeparture(departure);
            route.setDestination(destination);
            route.setDistance(distance);
            Route newRoute = routeService.createRoute(route);
            ticketRoute.setRouteId(newRoute.getRouteId());
        }
        else{
            ticketRoute.setRouteId(existingRoute.getRouteId());
        }

        LocalDateTime departureDate = handleDepartureDate();
        int tripDuration = handleTripDuration();

        System.out.println("How many tickets of this type do you want to add?");
        int numberOfTickets = sc.nextInt();
        sc.nextLine();

        createTickets(numberOfTickets, finalPrice, sellingPrice,ticketTransportationType, targetContractId , ticketRoute , departureDate, tripDuration);
    }

    private void createTickets(int numberOfTickets, double finalPrice, double sellingPrice, TransportationType ticketTransportationType, String targetContractId , Route r , LocalDateTime departureDate , int tripDuration) {
        try {
            for (int i = 0; i < numberOfTickets; i++) {
                String ticketId = UUID.randomUUID().toString();

                Ticket t = new Ticket();
                t.setTicketId(ticketId);
                t.setBoughtFor(finalPrice);
                t.setSellingPrice(sellingPrice);
                t.setTransportationType(ticketTransportationType);
                t.setDepartureDate(departureDate);
                t.setTripDuration(tripDuration);
                t.setRoute(r);

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


    private LocalDateTime handleDepartureDate(){
        LocalDateTime departureDate;
        while(true){
            int day = validator.handleDays();
            int month = validator.handleMonths();
            int year = validator.handleYear();
            int hour = validator.handleHours();
            int minutes = validator.handleMinutes();
            departureDate = LocalDateTime.of(year, month, day, hour, minutes);

            if(departureDate.isAfter(LocalDateTime.now())){
                break;
            }
            else{
                System.out.println("Departure date can't be after now !");
            }
        }
        return departureDate;
    }

    public static int handleTripDuration() {
        Scanner scanner = new Scanner(System.in);
        String durationPattern = "(\\d{1,2})\\s*h\\s*(\\d{1,2})\\s*mins";

        while (true) {
            System.out.print("Enter the trip duration in the format 'xx h xx mins': ");
            String input = scanner.nextLine();

            Pattern pattern = Pattern.compile(durationPattern);
            Matcher matcher = pattern.matcher(input);

            if (matcher.matches()) {
                int hours = Integer.parseInt(matcher.group(1));
                int minutes = Integer.parseInt(matcher.group(2));

                return hours * 60 + minutes;
            } else {
                System.out.println("Invalid format. Please enter the duration in the correct format.");
            }
        }
    }



}
