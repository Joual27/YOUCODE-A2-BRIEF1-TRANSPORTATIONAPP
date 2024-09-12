package com.youcode.transportationApp.tickets;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.youcode.transportationApp.enums.DiscountType;


import com.youcode.transportationApp.specialOffers.SpecialOffer;
import com.youcode.transportationApp.tickets.interfaces.TicketRepositoryI;
import com.youcode.transportationApp.tickets.interfaces.TicketServiceI;
import com.youcode.transportationApp.contracts.Contract;
import com.youcode.transportationApp.contracts.ContractRepository;
import com.youcode.transportationApp.contracts.ValidContractDTO;



public class TicketService implements TicketServiceI{
    
    private final TicketRepositoryI ticketRepository;
    

    public TicketService(TicketRepositoryI ticketRepository ){
       this.ticketRepository = ticketRepository;
    }


    @Override
    public List<Ticket> getAllTickets(){
        return ticketRepository.getAllTickets();
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


    public boolean isValidContractId(String contractId , List<ValidContractDTO> validContracts){
        for(ValidContractDTO v : validContracts){
            if(v.getContract().getContractId().equals(contractId)){
                return true;
            }
        }
        return false;   
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

    @Override
    public List<List<Ticket>> searchForAvailableTickets(String departure , String destination , LocalDate departureDate){
        List<Ticket> allTickets = getAllTickets();
        List<List<Ticket>> allComposedTickets = new ArrayList<>();
        getComposedTickets(departure , destination , departureDate.atStartOfDay() , new ArrayList<Ticket>() , allComposedTickets , allTickets);


        return allComposedTickets;
    }

    @Override
    public void getComposedTickets(String departure, String destination, LocalDateTime departureDate,
                                   List<Ticket> currentComposedTicket, List<List<Ticket>> allComposedTickets,
                                   List<Ticket> allTickets) {
        if (departure.equals(destination)) {
            allComposedTickets.add(new ArrayList<>(currentComposedTicket));
            return;
        }

        List<Ticket> nextLegs = allTickets.stream()
                .filter(ticket -> ticket.getRoute().getDeparture().equals(departure)
                        && (isSameDay(ticket.getDepartureDate(), LocalDate.now()) ?
                        ticket.getDepartureDate().isAfter(LocalDateTime.now()) :
                        ticket.getDepartureDate().isAfter(departureDate))
                        )
                .collect(Collectors.toList());

        for (Ticket nextLeg : nextLegs) {
            if (!currentComposedTicket.isEmpty()) {
                Ticket lastLeg = currentComposedTicket.get(currentComposedTicket.size() - 1);
                long waitingTime = Duration.between(
                        lastLeg.getDepartureDate().plusMinutes(lastLeg.getTripDuration()),
                        nextLeg.getDepartureDate()
                ).toMinutes();

                if (waitingTime >= 300) {
                    continue;
                }
            }

            currentComposedTicket.add(nextLeg);
            getComposedTickets(nextLeg.getRoute().getDestination(),
                    destination,
                    nextLeg.getDepartureDate().plusMinutes(nextLeg.getTripDuration()),
                    currentComposedTicket,
                    allComposedTickets,
                    allTickets);
            currentComposedTicket.remove(nextLeg);
        }
    }


    public int calculateTotalDuration(List<Ticket> trip) {
        return trip.stream().mapToInt(Ticket::getTripDuration).sum();
    }

    public double calculateTotalDistance(List<Ticket> trip) {
        return trip.stream().mapToDouble(ticket -> ticket.getRoute().getDistance()).sum();
    }

    private boolean isSameDay(LocalDateTime departureDate, LocalDate localDate) {
        return departureDate.toLocalDate().equals(localDate);
    }

    @Override
    public void markTicketAsSold(String ticketId){
        ticketRepository.markTicketAsSold(ticketId);
    }

}
