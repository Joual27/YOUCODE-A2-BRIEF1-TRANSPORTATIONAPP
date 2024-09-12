package com.youcode.transportationApp.ui.process;

import com.youcode.transportationApp.reservations.Reservation;
import com.youcode.transportationApp.reservations.ReservationService;
import com.youcode.transportationApp.tickets.Ticket;
import com.youcode.transportationApp.tickets.TicketRepository;
import com.youcode.transportationApp.tickets.TicketService;
import com.youcode.transportationApp.tickets.interfaces.TicketRepositoryI;
import com.youcode.transportationApp.tickets.interfaces.TicketServiceI;
import com.youcode.transportationApp.utils.DatesValidator;

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

    public TicketReservationProcess() {
        TicketRepositoryI ticketRepository = new TicketRepository();
        this.ticketService = new TicketService(ticketRepository);
    }



    public void handleTicketSearchingProcess(){
        System.out.println("Please enter the departure :");
        String departure = scanner.nextLine();
        System.out.println("Please enter the destination :");
        String destination = scanner.nextLine();

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
                System.out.println("Reservation on trip " + createdReservation.getSubTickets().getFirst().getRoute().getDeparture() + " - " + createdReservation.getSubTickets().getFirst().getRoute().getDestination() + "Made Successfully !");
            }
        }
    }
}
