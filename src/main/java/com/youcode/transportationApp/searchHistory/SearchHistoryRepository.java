package com.youcode.transportationApp.searchHistory;

import com.youcode.transportationApp.database.DbConnection;
import com.youcode.transportationApp.searchHistory.interfaces.SearchHistoryRepositoryI;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SearchHistoryRepository implements SearchHistoryRepositoryI {

    private Connection cnx;
    public SearchHistoryRepository(){
        try{
            cnx = DbConnection.getInstance().getConnection();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public List<SearchHistory> getTopSearchedTrips(String customerEmail){
        List<SearchHistory> searchedTrips = new ArrayList<>();
        String query = "SELECT * FROM searchHistory WHERE customerEmail= ? ORDER BY numberofoccurrences DESC LIMIT 3";
        try{
            PreparedStatement stmt = cnx.prepareStatement(query);
            stmt.setString(1, customerEmail);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                SearchHistory searchHistory = new SearchHistory();
                searchHistory.setDeparture(rs.getString("departure"));
                searchHistory.setDestination(rs.getString("destination"));
                searchedTrips.add(searchHistory);
            }
            return searchedTrips;
        }
        catch(SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void updateCustomerSearchHistory(SearchHistory searchHistory , String customerEmail){
        String query = "UPDATE searchHistory SET numberofoccurrences = ? WHERE customerEmail = ? AND departure = ? AND destination = ?";

        try{
            PreparedStatement stmt = cnx.prepareStatement(query);
            stmt.setInt(1, searchHistory.getNumberOfOccurrences());
            stmt.setString(2, customerEmail);
            stmt.setString(3, searchHistory.getDeparture());
            stmt.setString(4, searchHistory.getDestination());
            stmt.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public SearchHistory getSearchHistoryElementByDepartureAndDestination(String departure, String destination , String customerEmail){
        String query = "SELECT * FROM searchHistory WHERE departure = ? AND destination = ? AND customerEmail = ?";
        try{
            PreparedStatement stmt = cnx.prepareStatement(query);
            stmt.setString(1, departure);
            stmt.setString(2, destination);
            stmt.setString(3, customerEmail);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                SearchHistory searchHistory = new SearchHistory();
                searchHistory.setDeparture(rs.getString("departure"));
                searchHistory.setDestination(rs.getString("destination"));
                searchHistory.setNumberOfOccurrences(rs.getInt("numberofoccurrences"));
                return searchHistory;
            }
            return null;
        }
        catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void createSearchHistoryElement(SearchHistory searchHistory){
        String query = "INSERT INTO searchHistory (departure, destination,customeremail) VALUES (?, ? , ?)";
        try{
            PreparedStatement stmt = cnx.prepareStatement(query);
            stmt.setString(1, searchHistory.getDeparture());
            stmt.setString(2, searchHistory.getDestination());
            stmt.setString(3 ,searchHistory.getCustomer().getEmail());
            stmt.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }


}
