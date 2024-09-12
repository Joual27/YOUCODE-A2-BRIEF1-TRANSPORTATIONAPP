package com.youcode.transportationApp.reservations.interfaces;

import com.youcode.transportationApp.reservations.Reservation;
import com.youcode.transportationApp.tickets.Ticket;

import java.util.List;

public interface ReservationServiceI {

    public Reservation makeReservation(List<Ticket> tickets);

}
