package com.youcode.transportationApp.reservations;

import com.youcode.transportationApp.auth.Customer;
import com.youcode.transportationApp.contracts.Contract;
import com.youcode.transportationApp.database.DbConnection;
import com.youcode.transportationApp.partners.Partner;
import com.youcode.transportationApp.reservations.interfaces.ReservationRepositoryI;
import com.youcode.transportationApp.route.Route;
import com.youcode.transportationApp.tickets.Ticket;
import com.youcode.transportationApp.tickets.TicketRepository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReservationRepository implements ReservationRepositoryI {

    private final Connection cnx;
    private final TicketRepository ticketRepository = new TicketRepository();
    public ReservationRepository() throws SQLException{
        cnx = DbConnection.getInstance().getConnection();
    }

    @Override
    public void saveReservation(Reservation reservation){
        String query = "INSERT INTO reservations (reservationid , customeremail , reserved_at) VALUES (? , ? , ? )";
        try{
            PreparedStatement stmt = cnx.prepareStatement(query);
            stmt.setString(1, reservation.getReservationId());
            stmt.setString(2 , reservation.getCustomer().getEmail());
            stmt.setTimestamp(3 , Timestamp.valueOf(LocalDateTime.now()));
            stmt.executeUpdate();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void saveReservationTickets(Reservation r){
        String query = "INSERT INTO ticketsofreservation VALUES ( ? , ? )";
        try{
            PreparedStatement stmt = cnx.prepareStatement(query);
            for(Ticket ticket : r.getSubTickets()){
               stmt.setString(1, ticket.getTicketId());
               stmt.setString(2 , r.getReservationId());
               stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Reservation> getAllReservationsOfCustomer(String customerEmail){
        List<Reservation> reservations = new ArrayList<>();
        String query = "SELECT * FROM reservations WHERE customeremail = ?";
        try{
            PreparedStatement stmt = cnx.prepareStatement(query);
            stmt.setString(1, customerEmail);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                Reservation reservation = new Reservation();
                reservation.setReservationId(rs.getString("reservationid"));
                reservation.setCancelledAt(rs.getTimestamp("cancelled_at").toLocalDateTime());
                reservation.setReservedAt(rs.getTimestamp("reserved_at").toLocalDateTime());
                reservation.setSubTickets(getAllTicketsOfReservation(reservation.getReservationId()));
                reservations.add(reservation);
            }
            return reservations;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Ticket> getAllTicketsOfReservation(String reservationId){
        List<Ticket> tickets = new ArrayList<>();
        String query = "SELECT * FROM ticketsofreservation JOIN tickets ON ticketsofreservation.ticketid = tickets.ticketid " +
                "JOIN contrcats ON tickets.contractid = contracts.ticketsid" +
                "JOIN partners ON contracts.partnerid = partners.partnerid" +
                "JOIN routes ON tickets.routeid = routes.routeid" +
                "WHERE reservationid = ?";
        try{
            PreparedStatement stmt = cnx.prepareStatement(query);
            stmt.setString(1, reservationId);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                Ticket ticket = new Ticket();
                ticket.setTicketId(rs.getString("ticketid"));
                ticket = ticketRepository.getTicketById(rs.getString("ticketid"));
                Contract c = new Contract();
                Partner p = new Partner();
                Route r = new Route();
                r.setDeparture(rs.getString("departure"));
                r.setDestination(rs.getString("destination"));
                r.setDistance(rs.getDouble("distance"));
                p.setCompanyName(rs.getString("companyname"));
                c.setPartner(p);
                ticket.setContract(c);
                ticket.setRoute(r);
                tickets.add(ticket);
            }
            return tickets;
        }
        catch(SQLException e){
            e.printStackTrace();
            return null;
        }
    }

}
