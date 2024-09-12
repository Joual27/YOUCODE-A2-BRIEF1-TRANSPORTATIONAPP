package com.youcode.transportationApp.tickets;

import java.sql.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


import com.youcode.transportationApp.contracts.Contract;
import com.youcode.transportationApp.contracts.ContractRepository;
import com.youcode.transportationApp.contracts.ValidContractDTO;
import com.youcode.transportationApp.database.DbConnection;
import com.youcode.transportationApp.enums.TicketStatus;
import com.youcode.transportationApp.enums.TransportationType;
import com.youcode.transportationApp.partners.Partner;
import com.youcode.transportationApp.route.Route;
import com.youcode.transportationApp.route.RouteRepository;
import com.youcode.transportationApp.route.interfaces.RouteRepositoryI;
import com.youcode.transportationApp.tickets.interfaces.TicketRepositoryI;

public class TicketRepository implements TicketRepositoryI{

    private Connection cnx;

    public TicketRepository () {
        try{
            cnx = DbConnection.getInstance().getConnection();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }


    public void createTicket(Ticket ticket){
        try {
            String query = "INSERT INTO tickets (ticketId , transportationType , boughtfor , sellingprice , ticketStatus , contractId , routeId , departureDate , tripDuration) VALUES (?,?,?,?,?,?,?,?,?)";
            PreparedStatement stmt = cnx.prepareStatement(query);
            stmt.setString(1, ticket.getTicketId());
            stmt.setObject(2, ticket.getTransportationType().name(),java.sql.Types.OTHER);
            stmt.setDouble(3, ticket.getBoughtFor());
            stmt.setDouble(4, ticket.getSellingPrice());
            stmt.setObject(5, TicketStatus.valueOf("PENDING").name(), java.sql.Types.OTHER);
            stmt.setString(6, ticket.getContract().getContractId());
            stmt.setString(7 , ticket.getRoute().getRouteId());
            stmt.setTimestamp(8, java.sql.Timestamp.valueOf(ticket.getDepartureDate()));
            stmt.setInt(9, ticket.getTripDuration());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




    @Override
    public List<ValidContractDTO> getAvailableContracts(TransportationType transportationType){
        List<ValidContractDTO> validContracts = new ArrayList<ValidContractDTO>();
        try {
            String query = "SELECT * from contracts join partners ON contracts.partnerid = partners.partnerid AND contracts.contractstatus = 'ONGOING' AND partners.partnershipstatus = 'ACTIVE' AND partners.transportationtype = ? AND partners.deleted_at IS NULL and contracts.deleted_at IS NULL ";
            PreparedStatement stmt = cnx.prepareStatement(query);
            stmt.setObject(1, transportationType , java.sql.Types.OTHER);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Contract c = new Contract();
                Partner p = new Partner();
                c.setContractId(rs.getString("contractid"));
                c.setStartingDate(rs.getTimestamp("startingdate").toLocalDateTime().toLocalDate());
                c.setStartingDate(rs.getTimestamp("enddate").toLocalDateTime().toLocalDate());
                c.setSpecialRate(rs.getDouble("specialrate"));

                p.setCompanyName(rs.getString("companyname"));
                p.setTransportationType(transportationType);

                ValidContractDTO validContract = new ValidContractDTO(c,p);
                validContracts.add(validContract);
            }
        
            return validContracts;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public ArrayList<Ticket> getAllTickets(){
        ArrayList<Ticket> tickets = new ArrayList<Ticket>();
        
        try {
            String query = "WITH RankedTrips AS (" +
                    "    SELECT " +
                    "        ROW_NUMBER() OVER (PARTITION BY routes.departure, routes.destination, tickets.departuredate " +
                    "                          ORDER BY tickets.departuredate) AS rn, " +
                    "        tickets.ticketid, " +
                    "        tickets.boughtfor, " +
                    "        tickets.sellingprice, " +
                    "        tickets.soldat, " +
                    "        tickets.ticketstatus, " +
                    "        tickets.contractid, " +
                    "        tickets.transportationtype, " +
                    "        tickets.deleted_at, " +
                    "        tickets.routeid, " +
                    "        tickets.tripduration, " +
                    "        tickets.departuredate, " +
                    "        routes.departure, " +
                    "        routes.destination, " +
                    "        routes.distance, " +
                    "        partners.companyname " +
                    "    FROM " +
                    "        tickets " +
                    "    JOIN routes ON tickets.routeid = routes.routeid " +
                    "    JOIN contracts ON tickets.contractid = contracts.contractid " +
                    "    JOIN partners ON contracts.partnerid = partners.partnerid " +
                    "    WHERE tickets.deleted_at IS NULL AND tickets.soldat IS NULL" +
                    ")" +
                    "SELECT * FROM RankedTrips WHERE rn = 1;";


            PreparedStatement stmt = cnx.prepareStatement(query);
            ResultSet rs= stmt.executeQuery();
            while (rs.next()) {
                Ticket t  = new Ticket();
                t.setTicketId(rs.getString("ticketid"));
                t.setBoughtFor(rs.getDouble("boughtfor"));
                t.setSellingPrice(rs.getDouble("sellingprice"));
                t.setTicketStatus(TicketStatus.valueOf(rs.getString("ticketstatus")));
                t.setTransportationType(TransportationType.valueOf(rs.getString("transportationtype")));
                Contract c  = new Contract();
                c.setContractId(rs.getString("contractid"));

                Partner p = new Partner();
                p.setCompanyName(rs.getString("companyName"));
                c.setPartner(p);

                Route r = new Route();
                r.setDeparture(rs.getString("departure"));
                r.setDestination(rs.getString("destination"));
                r.setDistance(rs.getDouble("distance"));
                t.setRoute(r);
                Timestamp departureTimestamp = rs.getTimestamp("departuredate");
                if (departureTimestamp != null) {
                    t.setDepartureDate(departureTimestamp.toLocalDateTime());
                } else {
                    t.setDepartureDate(null);
                }
                t.setTripDuration(rs.getInt("tripduration"));
                t.setContract(c);

                tickets.add(t);
            } 

        return tickets;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Ticket getTicketById(String ticketId){
        String query = "SELECT * FROM tickets JOIN routes ON tickets.routeid = routes.routeid  WHERE ticketid = ?";

        try {
            PreparedStatement stmt = cnx.prepareStatement(query);
            stmt.setString(1, ticketId);
            ResultSet res = stmt.executeQuery();

            if (res.next()) {
                Ticket ticket = new Ticket();
                ticket.setTicketId(res.getString("ticketid"));
                ticket.setTransportationType(TransportationType.valueOf(res.getString("transportationtype")));
                ticket.setBoughtFor(res.getDouble("boughtfor"));
                ticket.setSellingPrice(res.getDouble("sellingprice"));
                ticket.setSoldAt(res.getTimestamp("soldat"));
                ticket.setTicketStatus(TicketStatus.valueOf(res.getString("ticketstatus")));
                ticket.setDepartureDate(res.getTimestamp("departureDate").toLocalDateTime());
                ticket.setTripDuration(res.getInt("tripduration"));
                RouteRepositoryI rr = new RouteRepository();
                ticket.setRoute(rr.findRouteByDepartureAndDestination(res.getString("departure"), res.getString("destination")));
                
                ContractRepository contractRepository = new ContractRepository();
                ticket.setContract(contractRepository.getContractById(res.getString("contractid")));

                return ticket;
            }
    

            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public void editTicket(Ticket ticket){
        String query = "UPDATE tickets SET transportationtype = ?, sellingprice = ?, ticketstatus = ? WHERE ticketid = ?";

        try {
            PreparedStatement stmt = cnx.prepareStatement(query);
            stmt.setObject(1, ticket.getTransportationType().name(), java.sql.Types.OTHER);
            stmt.setDouble(2, ticket.getSellingPrice());
            stmt.setObject(3, ticket.getTicketStatus().name(), java.sql.Types.OTHER);
            stmt.setString(4, ticket.getTicketId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
    }

    @Override
    public void removeTicket(String ticketId) {
        String query = "UPDATE tickets SET deleted_at = NOW() WHERE ticketid = ?";

        try {
            PreparedStatement stmt = cnx.prepareStatement(query);
            stmt.setString(1, ticketId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
    }

    @Override
    public Ticket getTicketByRouteId(String routeId){
        String query = "SELECT * FROM tickets JOIN routes ON tickets.routeid = routes.routeid WHERE routeid = ? AND deleted_at IS NULL LIMIT 1";
        try{
            PreparedStatement stmt = cnx.prepareStatement(query);
            stmt.setString(1, routeId);
            ResultSet res = stmt.executeQuery();
            if (res.next()) {
                Ticket ticket = new Ticket();
                ticket.setTicketId(res.getString("ticketid"));
                ticket.setTransportationType(TransportationType.valueOf(res.getString("transportationtype")));
                ticket.setBoughtFor(res.getDouble("boughtfor"));
                ticket.setSellingPrice(res.getDouble("sellingprice"));
                ticket.setSoldAt(res.getTimestamp("soldat"));
                ticket.setTicketStatus(TicketStatus.valueOf(res.getString("ticketstatus")));
                ticket.setDepartureDate(res.getTimestamp("departureDate").toLocalDateTime());
                ticket.setTripDuration(res.getInt("tripduration"));
                RouteRepositoryI rr = new RouteRepository();
                ticket.setRoute(rr.findRouteByDepartureAndDestination(res.getString("departure"), res.getString("destination")));

                ContractRepository contractRepository = new ContractRepository();
                ticket.setContract(contractRepository.getContractById(res.getString("contractid")));

                return ticket;
            }
            return null;
        }
        catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public void markTicketAsSold(String ticketId){
        String query = "UPDATE tickets SET soldat = NOW() WHERE ticketid = ?";
        try{
            PreparedStatement stmt = cnx.prepareStatement(query);
            stmt.setString(1, ticketId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
 
}
    

