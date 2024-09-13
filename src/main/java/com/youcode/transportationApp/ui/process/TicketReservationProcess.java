package com.youcode.transportationApp.ui.process;

import com.youcode.transportationApp.auth.Customer;
import com.youcode.transportationApp.reservations.Reservation;
import com.youcode.transportationApp.reservations.ReservationService;
import com.youcode.transportationApp.searchHistory.SearchHistory;
import com.youcode.transportationApp.searchHistory.SearchHistoryService;
import com.youcode.transportationApp.tickets.Ticket;
import com.youcode.transportationApp.tickets.TicketRepository;
import com.youcode.transportationApp.tickets.TicketService;
import com.youcode.transportationApp.tickets.interfaces.TicketRepositoryI;
import com.youcode.transportationApp.tickets.interfaces.TicketServiceI;
import com.youcode.transportationApp.utils.DatesValidator;
import com.youcode.transportationApp.utils.Session;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TicketReservationProcess {
    public final Scanner scanner = new Scanner(System.in);
    public final TicketServiceI ticketService;
    public final DatesValidator validator = new DatesValidator();
    private final ReservationService reservationService = new ReservationService();
    private final SearchHistoryService searchHistoryService = new SearchHistoryService();

    public TicketReservationProcess() {
        TicketRepositoryI ticketRepository = new TicketRepository();
        this.ticketService = new TicketService(ticketRepository);
    }



    public void handleTicketSearchingProcess(){
        String departure = null;
        String destination = null;

        List<SearchHistory> previousSearches = searchHistoryService.getTop3Searches(Session.getInstance().getLoggedEmail());
        if (previousSearches != null && !previousSearches.isEmpty()) {
             System.out.println("Your Most searched Trips :");
             for (int i = 0; i < previousSearches.size(); i++) {
                 System.out.format("%d - %s ------> %s \n" , i+1 , previousSearches.get(i).getDeparture() , previousSearches.get(i).getDestination());
             }
            int choice;
             while(true){
                 System.out.println("Please enter the number of the trip to search or enter 0 to enter custom values : ");
                 choice = scanner.nextInt();
                 scanner.nextLine();
                 if(choice >= 0 && choice <= previousSearches.size()){
                     if (choice == 0){
                         System.out.println("Please enter the departure :");
                         departure = scanner.nextLine();
                         System.out.println("Please enter the destination :");
                         destination = scanner.nextLine();
                     }
                     else{
                         departure = previousSearches.get(choice-1).getDeparture();
                         destination = previousSearches.get(choice-1).getDestination();
                     }
                     break;
                 }
                 else {
                     System.out.println("Invalid choice ! try again !");
                 }
             }


        }


        if (departure == null || destination == null) {
            System.out.println("Please enter the departure :");
            departure = scanner.nextLine();
            System.out.println("Please enter the destination :");
            destination = scanner.nextLine();
        }

        SearchHistory currentSearchCombination = searchHistoryService.getSearchHistoryElementByDepartureAndDestination(departure,destination,Session.getInstance().getLoggedEmail());
        if(currentSearchCombination != null){
            currentSearchCombination.incrementNumberOfOccurrences();
            searchHistoryService.updateCustomerSearchHistory(currentSearchCombination , Session.getInstance().getLoggedEmail());
        }
        else{
            SearchHistory newSearchHistoryElement = new SearchHistory();
            newSearchHistoryElement.setDeparture(departure);
            newSearchHistoryElement.setDestination(destination);
            Customer c = new Customer();
            c.setEmail(Session.getInstance().getLoggedEmail());
            newSearchHistoryElement.setCustomer(c);

            searchHistoryService.saveSearchHistoryElement(newSearchHistoryElement);
        }



        System.out.println("Please enter your travel date ! ");
        int day = validator.handleDays();
        int month = validator.handleMonths();
        int year = validator.handleYear();

        LocalDate departureDate = LocalDate.of(year, month, day);
        List<List<Ticket>> allComposedTickets = ticketService.searchForAvailableTickets(departure, destination, departureDate);
        handleFetchingSearchResults(allComposedTickets);
    }

    public void handleFetchingSearchResults(List<List<Ticket>> allComposedTickets) {
        List<String> searchResults = new ArrayList<>();

        for (int tripIndex = 0; tripIndex < allComposedTickets.size(); tripIndex++) {
            List<Ticket> composedTrip = allComposedTickets.get(tripIndex);
            StringBuilder result = new StringBuilder();

            int totalDuration = ticketService.calculateTotalDuration(composedTrip);
            double totalPrice = ticketService.calculateTotalDistance(composedTrip);

            result.append(String.format("%d - Trip: %s, Stops: %d, Total Duration: %d hours %d minutes, Total Price: %.2f DH",
                            tripIndex + 1,
                            composedTrip.get(0).getRoute().getDeparture() + " ------> " + composedTrip.get(composedTrip.size() - 1).getRoute().getDestination(),
                            composedTrip.size() - 1,
                            totalDuration / 60, totalDuration % 60,
                            totalPrice))
                    .append("\n--------------------------------------------------\n");

            for (int i = 0; i < composedTrip.size(); i++) {
                Ticket ticket = composedTrip.get(i);
                String legDetail = String.format("%s ---> %s\n- Transportation Type: %s\n- Company: %s\n- Departure: %s\n- Duration: %d hours %d minutes\n",
                        ticket.getRoute().getDeparture(),
                        ticket.getRoute().getDestination(),
                        ticket.getTransportationType(),
                        ticket.getContract().getPartner().getCompanyName(),
                        ticket.getDepartureDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                        ticket.getTripDuration() / 60, ticket.getTripDuration() % 60);
                result.append(legDetail);

                if (i < composedTrip.size() - 1) {
                    Ticket nextTicket = composedTrip.get(i + 1);
                    long waitingTime = Duration.between(
                            ticket.getDepartureDate().plusMinutes(ticket.getTripDuration()),
                            nextTicket.getDepartureDate()
                    ).toMinutes();

                    result.append(String.format("- Stop: %s, Waiting Time: %d hours %d minutes\n",
                                    ticket.getRoute().getDestination(),
                                    waitingTime / 60, waitingTime % 60))
                            .append("-------------------------\n");
                }
            }

            result.append("--------------------------------------------------\n");
            searchResults.add(result.toString());
        }

        if (searchResults.isEmpty()) {
            System.out.println("NO available trips with this criteria");
        } else {
            searchResults.forEach(System.out::println);
            int tripNumber;
            while(true){
                System.out.println("Please Provide The Number of the trip you want to book Or enter 0 to go back to Menu");
                tripNumber = scanner.nextInt();
                scanner.nextLine();
                if(tripNumber >= 0 && tripNumber <= searchResults.size()){
                    break;
                }
                else{
                    System.out.println("Please enter a valid trip number !");
                }
            }
            if(tripNumber == 0){
                return;
            }
            else{
                List<Ticket> chosenTrip = allComposedTickets.get(tripNumber-1);
                Reservation createdReservation = reservationService.makeReservation(chosenTrip);
                System.out.println("Reservation on trip " + createdReservation.getSubTickets().getFirst().getRoute().getDeparture() + " - " + createdReservation.getSubTickets().getLast().getRoute().getDestination() + " Made Successfully !");
            }
        }
    }

    public void handleFetchingCustomerReservations() {
        String customerEmail = Session.getInstance().getLoggedEmail();
        System.out.println("All Reservations of: " + customerEmail);

        List<Reservation> reservations = reservationService.getAllReservationsOfCustomer(customerEmail);

        if (reservations == null || reservations.isEmpty()) {
            System.out.println("No reservations found for this customer.");
        } else {
            for (Reservation reservation : reservations) {
                System.out.println("Reservation ID: " + reservation.getReservationId());
                System.out.println("Reserved At: " + reservation.getReservedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));

                if (reservation.getCancelledAt() != null) {
                    System.out.println("Cancelled At: " + reservation.getCancelledAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
                }
                else{
                    System.out.println("Active Reservation");
                }

                System.out.println("Tickets:");
                for (Ticket ticket : reservation.getSubTickets()) {
                    System.out.println("  Route: " + ticket.getRoute().getDeparture() + " ---> " + ticket.getRoute().getDestination());
                    System.out.println("  Transportation Type: " + ticket.getTransportationType());
                    System.out.println("  Company: " + ticket.getContract().getPartner().getCompanyName());
                    System.out.println("  Departure: " + ticket.getDepartureDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
                    System.out.println("  Duration: " + (ticket.getTripDuration() / 60) + " hours " + (ticket.getTripDuration() % 60) + " minutes");
                    System.out.println("  -----------------------------------");
                }
                System.out.println("=========================================");
            }
        }
    }


    public void handleReservationCancellation(){
        System.out.println("Please provide the id of the reservation You want to cancel ");
        String reservationId = scanner.nextLine();

        reservationService.cancelReservation(reservationId);
        System.out.println("Reservation " + reservationId +"cancelled successfully !" );
    }





}
