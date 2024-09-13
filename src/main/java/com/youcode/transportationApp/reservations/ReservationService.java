package com.youcode.transportationApp.reservations;

import com.youcode.transportationApp.auth.AuthRepository;
import com.youcode.transportationApp.auth.interfaces.AuthRepositoryI;
import com.youcode.transportationApp.reservations.interfaces.ReservationRepositoryI;
import com.youcode.transportationApp.reservations.interfaces.ReservationServiceI;
import com.youcode.transportationApp.tickets.Ticket;
import com.youcode.transportationApp.tickets.TicketRepository;
import com.youcode.transportationApp.tickets.interfaces.TicketRepositoryI;
import com.youcode.transportationApp.utils.Session;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ReservationService implements ReservationServiceI {

    private AuthRepositoryI authRepository;
    private ReservationRepositoryI reservationRepository ;
    private TicketRepositoryI ticketRepository;

    public ReservationService() {
        try {
            authRepository = new AuthRepository();
            reservationRepository = new ReservationRepository();
            ticketRepository = new TicketRepository();
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Reservation makeReservation(List<Ticket> tickets){
        Reservation reservation = new Reservation();
        reservation.setReservationId(UUID.randomUUID().toString());
        reservation.setCustomer(authRepository.getCustomerByEmail(Session.getInstance().getLoggedEmail()));
        reservation.setSubTickets(tickets);
        reservationRepository.saveReservation(reservation);
        reservationRepository.saveReservationTickets(reservation);
        for(Ticket ticket : tickets){
            ticketRepository.markTicketAsSold(ticket.getTicketId());
        }
        return reservation;
    }

    public List<Reservation> getAllReservationsOfCustomer(String customerEmail){
        return reservationRepository.getAllReservationsOfCustomer(customerEmail);
    }

    public void cancelReservation(String reservationId){
        reservationRepository.cancelReservation(reservationId);
        List<Ticket> reservationTickets = reservationRepository.getAllTicketsOfReservation(reservationId);

        for(Ticket ticket : reservationTickets){
            ticketRepository.markTicketAsUnsold(ticket.getTicketId());
        }
    }

}
