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
        String sqlQuery = "SELECT * FROM partners WHERE deleted_at IS NULL;";

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

    @Override
    public void createPartner(Partner partner) throws SQLException{
        String query = "INSERT INTO partners (partnerid, companyname, commercialcontact, transportationtype, geographiczone, specialconditions, partnershipstatus, creationdate) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try(PreparedStatement stmt = cnx.prepareStatement(query)){
           stmt.setString(1,partner.getPartnerId());
           stmt.setString(2,partner.getCompanyName());
           stmt.setString(3,partner.getCommercialContact());
           stmt.setObject(4, partner.getTransportationType().name(),java.sql.Types.OTHER);
           stmt.setString(5, partner.getGeographicZone());
           stmt.setString(6, partner.getSpecialConditions());
           stmt.setObject(7, partner.getPartnershipStatus().name(),java.sql.Types.OTHER);
           stmt.setTimestamp(8, new java.sql.Timestamp(partner.getCreationDate().getTime()));
           stmt.executeUpdate();
        }
    }

    @Override
    public void editPartner(Partner partner) throws SQLException{
        String query = "UPDATE partners SET companyname = ?, commercialcontact = ?, transportationtype = ?, " +
                   "geographiczone = ?, specialconditions = ?, partnershipstatus = ? " +
                   "WHERE partnerid = ?";

        try(PreparedStatement stmt = cnx.prepareStatement(query)){
           stmt.setString(1, partner.getCompanyName());
           stmt.setString(2, partner.getCommercialContact() );
           stmt.setObject(3, partner.getTransportationType().name() , java.sql.Types.OTHER );
           stmt.setString(4, partner.getGeographicZone() );
           stmt.setString(5, partner.getSpecialConditions() );
           stmt.setObject(6, partner.getPartnershipStatus() , java.sql.Types.OTHER);
           stmt.setString(7, partner.getPartnerId());

           stmt.executeUpdate();
        }
        
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public Partner getPartnerById(String partnerId) throws SQLException{
        String query = "SELECT * FROM partners WHERE partnerid = ? ";

        try(PreparedStatement p = cnx.prepareStatement(query)){
            p.setString(1, partnerId);
            ResultSet res = p.executeQuery();
            if(res.next()){
                return new Partner(res.getString("partnerid"), res.getString("companyname"), res.getString("commercialContact"),TransportationType.valueOf(res.getString("transportationtype")), res.getString("geographiczone"), res.getString("specialconditions"), PartnershipStatus.valueOf(res.getString("partnershipstatus")), res.getTimestamp("creationdate"));
            }
            else{
                System.out.println("no Partner with that id is available");
                return null;
            }
        }
    
    }


    // @Override
    // public Partner getPartnerById(String partnerId){

    // }5b9bb2da-8bbe-4764-8da0-6cc22ebb156a

    @Override
    public void removePartner(String partnerId){
       String query = "UPDATE partners SET deleted_at = NOW() WHERE partnerid = ? ";

       try(PreparedStatement stmt = cnx.prepareStatement(query)){
         stmt.setString(1, partnerId);
         stmt.executeUpdate();
       }
       catch(SQLException e){
         e.printStackTrace();
       }
    }
    
}
