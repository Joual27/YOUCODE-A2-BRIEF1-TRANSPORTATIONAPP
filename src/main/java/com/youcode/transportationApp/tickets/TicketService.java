package com.youcode.transportationApp.tickets;

import java.util.List;

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


    public boolean isValidContractId(String contractId , List<ValidContractDTO> validcontracts){
        for(ValidContractDTO v : validcontracts){
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

}
