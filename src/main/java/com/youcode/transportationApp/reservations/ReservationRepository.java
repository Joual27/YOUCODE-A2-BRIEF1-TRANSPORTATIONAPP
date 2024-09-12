package com.youcode.transportationApp.reservations;

import com.youcode.transportationApp.auth.Customer;
import com.youcode.transportationApp.database.DbConnection;
import com.youcode.transportationApp.reservations.interfaces.ReservationRepositoryI;
import com.youcode.transportationApp.tickets.Ticket;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReservationRepository implements ReservationRepositoryI {

    private final Connection cnx;
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
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Ticket> getAllTicketsOfReservation(String reservationId){

    }

}
