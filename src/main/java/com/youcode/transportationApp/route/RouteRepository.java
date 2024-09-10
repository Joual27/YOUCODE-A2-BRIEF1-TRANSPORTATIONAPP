package com.youcode.transportationApp.route;

import com.youcode.transportationApp.database.DbConnection;
import com.youcode.transportationApp.route.interfaces.RouteRepositoryI;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class RouteRepository implements RouteRepositoryI {

    private Connection cnx ;

    public RouteRepository(){
        try{
            cnx = DbConnection.getInstance().getConnection();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }
    public Route findRouteByDepartureAndDestination(String departure, String destination){
        String query = "SELECT * FROM routes WHERE departure = ? AND destination = ?";
        try{
            PreparedStatement stmt = cnx.prepareStatement(query);
            stmt.setString(1, departure);
            stmt.setString(2, destination);
            ResultSet rs = stmt.executeQuery();
            Route r  = new Route();
            if(rs.next()){
                r.setRouteId(rs.getString("routeId"));
                r.setDeparture(departure);
                r.setDestination(destination);
                return r;
            }
            return null;
        }
        catch(SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public void createRoute(Route route){
        String query = "INSERT INTO routes ( routeId ,departure, destination , distance) VALUES (?, ? , ? , ?)";
        try{
            PreparedStatement stmt = cnx.prepareStatement(query);
            stmt.setString(1, route.getRouteId());
            stmt.setString(2, route.getDeparture());
            stmt.setString(3, route.getDestination());
            stmt.setDouble(4, route.getDistance());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
