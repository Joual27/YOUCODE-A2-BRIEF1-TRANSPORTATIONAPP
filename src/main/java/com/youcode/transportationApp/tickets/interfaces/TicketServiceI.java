package com.youcode.transportationApp.tickets.interfaces;

import com.youcode.transportationApp.contracts.ValidContractDTO;

import com.youcode.transportationApp.specialOffers.SpecialOffer;
import com.youcode.transportationApp.tickets.Ticket;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface TicketServiceI {

    public List<Ticket> getAllTickets() ;

    public Ticket createTicket(Ticket t);

    public Ticket updateTicket(Ticket t) ;

    public Ticket deleteTicket(String ticketId);

    public Ticket getTicketById(String ticketId);

    public double calculatePriceAfterSpecialRate(double initialPrice, String contractId);

    public boolean isValidContractId(String contractId , List<ValidContractDTO> validcontracts);

    public Double calculatePriceAfterSpecialOffer(SpecialOffer s , Double price);

    public List<List<Ticket>> searchForAvailableTickets(String departure , String destination , LocalDate departureDate);

    public void getComposedTickets(String departure , String destination , LocalDateTime departureDate , List<Ticket> currentComposedTicket , List<List<Ticket>> allComposedTickets , List<Ticket> allTickets );

    public int calculateTotalDuration(List<Ticket> trip);

    public double calculateTotalDistance(List<Ticket> trip);

    public void markTicketAsSold(String ticketId);

}
