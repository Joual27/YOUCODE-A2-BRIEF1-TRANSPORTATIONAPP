package com.youcode.transportationApp.tickets.interfaces;

import com.youcode.transportationApp.contracts.ValidContractDTO;

import com.youcode.transportationApp.specialOffers.SpecialOffer;
import com.youcode.transportationApp.tickets.Ticket;

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
    
}
