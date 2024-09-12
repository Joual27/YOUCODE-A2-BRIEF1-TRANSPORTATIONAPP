package com.youcode.transportationApp.ui.process;

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


    public void handleFetchingSearchResults(List<List<Ticket>> allComposedTickets){
        List<String> searchResults = new ArrayList<>();

        for (List<Ticket> composedTrip : allComposedTickets) {
            StringBuilder result = new StringBuilder();

            int totalDuration = ticketService.calculateTotalDuration(composedTrip);
            double totalPrice = composedTrip.stream().mapToDouble(Ticket::getSellingPrice).sum();

            result.append(String.format("Stops: %d, Total Duration: %d hours %d minutes, Total Price: %.2f EUR",
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

        if (searchResults.isEmpty()){
            System.out.println("NO available trips with this criteria");
        }
        else{
            System.out.println(searchResults.toString());
        }
    }

}
