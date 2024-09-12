package com.youcode.transportationApp.reservations;

import com.youcode.transportationApp.auth.Customer;
import com.youcode.transportationApp.tickets.Ticket;

import java.time.LocalDateTime;
import java.util.List;

public class Reservation {

    private String reservationId;
    private LocalDateTime reservedAt;
    private LocalDateTime cancelledAt;
    private Customer customer;
    private List<Ticket> subTickets;

    public Reservation(){}

    public String getReservationId() {
        return reservationId;
    }
    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }
    public LocalDateTime getReservedAt() {
        return reservedAt;
    }
    public void setReservedAt(LocalDateTime reservedAt) {
        this.reservedAt = reservedAt;
    }

    public LocalDateTime getCancelledAt() {
        return cancelledAt;
    }
    public void setCancelledAt(LocalDateTime cancelledAt) {
        this.cancelledAt = cancelledAt;
    }

    public List<Ticket> getSubTickets() {
        return subTickets;
    }

    public void setSubTickets(List<Ticket> subTickets) {
        this.subTickets = subTickets;
    }

    public Customer getCustomer() {
        return customer;
    }
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

}
