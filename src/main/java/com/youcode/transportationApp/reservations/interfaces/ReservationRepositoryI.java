package com.youcode.transportationApp.reservations.interfaces;

import com.youcode.transportationApp.reservations.Reservation;
import com.youcode.transportationApp.tickets.Ticket;

import java.util.List;

public interface ReservationRepositoryI {

    public void saveReservation(Reservation reservation);
    public void saveReservationTickets(Reservation r);
    public List<Reservation> getAllReservationsOfCustomer(String customerEmail);

    public List<Ticket> getAllTicketsOfReservation(String reservationId);



}
