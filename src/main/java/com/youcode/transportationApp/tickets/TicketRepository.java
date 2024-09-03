package com.youcode.transportationApp.tickets;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.youcode.transportationApp.database.DbConnection;
import com.youcode.transportationApp.tickets.interfaces.TicketRepositoryI;

public class TicketRepository implements TicketRepositoryI{

    private Connection cnx;

    public TicketRepository () throws SQLException{
        cnx = DbConnection.getInstance().getConnection();
    }


    public void createTicket(Ticket ticket){
        String query = "INSERT INTO tickets (ticketId , transportationType , boughtfor , sellingprice , ticketStatus , contractId ) VALUES (?,?,?,?,?,?)";

        try(PreparedStatement stmt = cnx.prepareStatement(query)){
            
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }
    
}
