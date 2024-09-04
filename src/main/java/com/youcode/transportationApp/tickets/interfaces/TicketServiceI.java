package com.youcode.transportationApp.tickets.interfaces;

import com.youcode.transportationApp.enums.TransportationType;
import java.sql.SQLException;;

public interface TicketServiceI {

    public void fetchAllTickets() throws SQLException;

    public void createTicket() throws SQLException;

    public String getContractIdFromAvailableContracts(TransportationType ticketTransportationType);

    public void updateTicket() throws SQLException;

    public void deleteTicket() throws SQLException;
}
