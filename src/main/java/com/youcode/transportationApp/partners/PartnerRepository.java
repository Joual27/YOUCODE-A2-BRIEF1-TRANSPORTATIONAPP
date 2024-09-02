package com.youcode.transportationApp.partners;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.youcode.transportationApp.database.DbConnection;
import com.youcode.transportationApp.enums.PartnershipStatus;
import com.youcode.transportationApp.enums.TransportationType;
import com.youcode.transportationApp.partners.interfaces.PartnerRepositoryI;

public class PartnerRepository implements PartnerRepositoryI{

    private Connection cnx ;

    public PartnerRepository () throws SQLException{
        this.cnx = DbConnection.getInstance().getConnection();
    }

    @Override
    public ArrayList<Partner> getAllPartners(){
        ArrayList<Partner> partners= new ArrayList<Partner>();
        String sqlQuery = "SELECT * FROM partners ;";

        try (PreparedStatement stmt = cnx.prepareStatement(sqlQuery);
            ResultSet resultSet = stmt.executeQuery(); ) {
            
            while (resultSet.next()) {
                Partner partner = new Partner(
                    resultSet.getString("partnerId"),
                    resultSet.getString("companyName"),
                    resultSet.getString("commercialContact"),
                    TransportationType.valueOf(resultSet.getString("transportationType")),
                    resultSet.getString("geographicZone"),
                    resultSet.getString("specialConditions"),
                    PartnershipStatus.valueOf(resultSet.getString("partnershipStatus")),
                    resultSet.getTimestamp("creationDate")
                );
                partners.add(partner);
            } 
        }
        catch(SQLException e){
            e.printStackTrace();
        }


        return partners;
    }

    // @Override
    // public Partner createPartner(Partner partner){

    // }

    // @Override
    // public Partner editPartner(Partner partner){

    // }

    // @Override
    // public Partner getPartnerById(String partnerId){

    // }

    // @Override
    // public Partner removePartner(String partnerId){

    // }
    
}
