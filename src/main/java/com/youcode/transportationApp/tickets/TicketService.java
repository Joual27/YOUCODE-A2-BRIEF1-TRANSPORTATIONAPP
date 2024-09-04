package com.youcode.transportationApp.tickets;

import com.youcode.transportationApp.enums.TransportationType;
import com.youcode.transportationApp.partners.interfaces.PartnerServiceI;
import com.youcode.transportationApp.tickets.interfaces.TicketRepositoryI;
import com.youcode.transportationApp.tickets.interfaces.TicketServiceI;


public class TicketService implements TicketServiceI{

    private final TicketRepositoryI ticketRepository;
    private final PartnerServiceI partnerService;


    public TicketService(TicketRepositoryI ticketRepository , PartnerServiceI partnerService){
       this.ticketRepository = ticketRepository;
       this.partnerService = partnerService;
    }

    public void createTicket(){
       System.out.println("Give the tickets transportation Type");
       TransportationType ticketTransportationType =  partnerService.handleTransportationType();
       

    }
}
