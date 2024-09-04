// package com.youcode.transportationApp.tickets;

// import java.sql.Connection;
// import java.sql.PreparedStatement;
// import java.sql.SQLException;
// import java.util.ArrayList;

// import com.youcode.transportationApp.contracts.ValidContractDTO;
// import com.youcode.transportationApp.database.DbConnection;
// import com.youcode.transportationApp.enums.TransportationType;
// import com.youcode.transportationApp.partners.Partner;
// import com.youcode.transportationApp.tickets.interfaces.TicketRepositoryI;

// public class TicketRepository implements TicketRepositoryI{

//     private Connection cnx;

//     public TicketRepository () throws SQLException{
//         cnx = DbConnection.getInstance().getConnection();
//     }


//     public void createTicket(Ticket ticket){
//         String query = "INSERT INTO tickets (ticketId , transportationType , boughtfor , sellingprice , ticketStatus , contractId ) VALUES (?,?,?,?,?,?)";

//         try(PreparedStatement stmt = cnx.prepareStatement(query)){
//             stmt.setString(1, ticket.getTicketId());
//             stmt.setObject(2, ticket.getTransportationType().name(),java.sql.Types.OTHER);
//             stmt.setDouble(3, ticket.getBoughtFor());
//             stmt.setDouble(4, ticket.getSellingPrice());
//             stmt.setObject(5, ticket.getTicketStatus().name(),java.sql.Types.OTHER);
//             stmt.setString(6, ticket.getContract().getContractId());
//             stmt.executeUpdate();
//         }
//         catch(SQLException e){
//             e.printStackTrace();
//         }
//     }

//     public ArrayList<ValidContractDTO> getAvailableContracts(TransportationType transportationType){
//         String query = "SELECT * contracts join partners ON contracts.partnerid = partners.partnerid AND contracts.contractstatus = 'ONGOING' AND partners.partnershipstatus = 'ACTIVE' AND partners.transportationtype = ?";


//         try(PreparedStatement stmt = cnx.prepareStatement(query)){
//            while () {
            
//            }
//         }
//         catch(SQLException e){
//             e.printStackTrace();
//         }
//     }

    
// }
