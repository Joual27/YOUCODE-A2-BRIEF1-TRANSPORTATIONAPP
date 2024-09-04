package com.youcode.transportationApp.tickets.interfaces;

import com.youcode.transportationApp.contracts.ValidContractDTO;
import com.youcode.transportationApp.enums.TransportationType;
import com.youcode.transportationApp.tickets.Ticket;


import java.util.ArrayList;

public interface TicketRepositoryI {
    public void createTicket(Ticket ticket);

    public ArrayList<ValidContractDTO> getAvailableContracts(TransportationType transportationType);
}