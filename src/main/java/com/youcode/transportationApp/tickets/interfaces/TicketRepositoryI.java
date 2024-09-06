package com.youcode.transportationApp.tickets.interfaces;

import com.youcode.transportationApp.contracts.ValidContractDTO;
import com.youcode.transportationApp.enums.TransportationType;
import com.youcode.transportationApp.tickets.Ticket;

import java.util.List;

public interface TicketRepositoryI {
    
    public List<Ticket> getAllTickets();

    public void createTicket(Ticket ticket);

    public List<ValidContractDTO> getAvailableContracts(TransportationType transportationType);

    public Ticket getTicketById(String ticketId);

    public void editTicket(Ticket ticket);

    public void removeTicket(String ticketId);
}