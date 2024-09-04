package com.youcode.transportationApp.tickets.interfaces;

import com.youcode.transportationApp.contracts.ValidContractDTO;
import com.youcode.transportationApp.enums.TransportationType;
import com.youcode.transportationApp.tickets.Ticket;

import java.sql.SQLException;
import java.util.ArrayList;

public interface TicketRepositoryI {
    
    public ArrayList<Ticket> getAllTickets() throws SQLException;

    public void createTicket(Ticket ticket);

    public ArrayList<ValidContractDTO> getAvailableContracts(TransportationType transportationType);

    public Ticket getTicketById(String ticketId) throws SQLException;

    public void editTicket(Ticket ticket) throws SQLException;

    public void removeTicket(String ticketId) throws SQLException;
}