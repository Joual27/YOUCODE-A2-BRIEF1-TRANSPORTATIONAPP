package com.youcode.transportationApp.specialOffers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Timestamp;

import com.youcode.transportationApp.contracts.Contract;
import com.youcode.transportationApp.database.DbConnection;
import com.youcode.transportationApp.enums.DiscountType;
import com.youcode.transportationApp.enums.OfferStatus;
import com.youcode.transportationApp.specialOffers.interfaces.SpecialOfferRepositoryI;

public class SpecialOfferRepository implements SpecialOfferRepositoryI{

    private final Connection cnx;

    public SpecialOfferRepository() throws SQLException {
        this.cnx = DbConnection.getInstance().getConnection();
    }

    public List<SpecialOffer> getAllSpecialOffers() {
        List<SpecialOffer> offers = new ArrayList<>();
        String sqlQuery = "SELECT * FROM specialoffers WHERE deleted_at IS NULL;";

        try (PreparedStatement stmt = cnx.prepareStatement(sqlQuery);
             ResultSet resultSet = stmt.executeQuery()) {

            while (resultSet.next()) {
                SpecialOffer offer = new SpecialOffer();
                offer.setOfferId(resultSet.getString("offerid"));
                offer.setOfferName(resultSet.getString("offername"));
                offer.setOfferDescription(resultSet.getString("offerdescription"));
                Timestamp startingTimestamp = resultSet.getTimestamp("startingdate");
                if (startingTimestamp != null) {
                    offer.setStartingDate(startingTimestamp.toLocalDateTime().toLocalDate());
                }
        
                Timestamp endingTimestamp = resultSet.getTimestamp("enddate");
                if (endingTimestamp != null) {
                    offer.setEndDate(endingTimestamp.toLocalDateTime().toLocalDate());
                }
        
                offer.setDiscountType(DiscountType.valueOf(resultSet.getString("discounttype")));
                offer.setDiscountValue(resultSet.getDouble("discountvalue"));
                offer.setConditions(resultSet.getString("conditions"));
                offer.setOfferStatus(OfferStatus.valueOf(resultSet.getString("offerstatus")));
 
                
                Contract contract = new Contract();
                contract.setContractId(resultSet.getString("contractid"));
                offer.setContract(contract);

                offers.add(offer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return offers;
    }

    public void createSpecialOffer(SpecialOffer offer) throws SQLException {
        String query = "INSERT INTO specialoffers (offerid, offername, offerdescription, startingdate, enddate, discounttype, discountvalue, conditions, offerstatus, contractid) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setString(1, offer.getOfferId());
            stmt.setString(2, offer.getOfferName());
            stmt.setString(3, offer.getOfferDescription());
            stmt.setDate(4, java.sql.Date.valueOf(offer.getStartingDate()));
            stmt.setDate(5, java.sql.Date.valueOf(offer.getEndDate()));
            stmt.setString(6, offer.getDiscountType().name());
            stmt.setDouble(7, offer.getDiscountValue());
            stmt.setString(8, offer.getConditions());
            stmt.setObject(9, offer.getOfferStatus().name(),java.sql.Types.OTHER);
            stmt.setString(10, offer.getContract().getContractId());
            stmt.executeUpdate();
        }
    }

    public void editSpecialOffer(SpecialOffer offer) throws SQLException {
        String query = "UPDATE specialoffers SET offername = ?, offerdescription = ?, startingdate = ?, enddate = ?, discounttype = ?, discountvalue = ?, conditions = ?, offerstatus = ? WHERE offerid = ?";

        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setString(1, offer.getOfferName());
            stmt.setString(2, offer.getOfferDescription());
            stmt.setDate(3, java.sql.Date.valueOf(offer.getStartingDate()));
            stmt.setDate(4, java.sql.Date.valueOf(offer.getEndDate()));
            stmt.setString(5, offer.getDiscountType().name());
            stmt.setDouble(6, offer.getDiscountValue());
            stmt.setString(7, offer.getConditions());
            stmt.setObject(8, offer.getOfferStatus().name(),java.sql.Types.OTHER);
            stmt.setString(9, offer.getOfferId());
            stmt.executeUpdate();
        }
    }

    public SpecialOffer getSpecialOfferById(String offerId) throws SQLException {
        String query = "SELECT * FROM specialoffers WHERE offerid = ?";

        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setString(1, offerId);
            ResultSet res = stmt.executeQuery();
            if (res.next()) {
                SpecialOffer offer = new SpecialOffer();
                offer.setOfferId(res.getString("offerid"));
                offer.setOfferName(res.getString("offername"));
                offer.setOfferDescription(res.getString("offerdescription"));
                Timestamp startingTimestamp = res.getTimestamp("startingdate");
                if (startingTimestamp != null) {
                    offer.setStartingDate(startingTimestamp.toLocalDateTime().toLocalDate());
                }
        
                Timestamp endingTimestamp = res.getTimestamp("enddate");
                if (endingTimestamp != null) {
                    offer.setEndDate(endingTimestamp.toLocalDateTime().toLocalDate());
                }
                offer.setDiscountType(DiscountType.valueOf(res.getString("discounttype")));
                offer.setDiscountValue(res.getDouble("discountvalue"));
                offer.setConditions(res.getString("conditions"));
                offer.setOfferStatus(OfferStatus.valueOf(res.getString("offerstatus")));

                Contract contract = new Contract();
                contract.setContractId(res.getString("contractid"));
                offer.setContract(contract);

                return offer;
            } else {
                System.out.println("No SpecialOffer with that ID is available");
                return null;
            }
        }
    }

    public void removeSpecialOffer(String offerId) {
        String query = "UPDATE specialoffers SET deleted_at = NOW() WHERE offerid = ?";

        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setString(1, offerId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public SpecialOffer getSpecialOfferByContractId(String contractId) throws SQLException{
        String query = "SELECT * FROM specialoffers where startingdate < NOW() and enddate > NOW() and offerstatus = 'ACTIVE' AND deleted_at IS NULL" ;

        try(PreparedStatement stmt = cnx.prepareStatement(query)){
           ResultSet rs = stmt.executeQuery();

           if(rs.next()){
              SpecialOffer specialOffer = new SpecialOffer();
              specialOffer.setOfferName(rs.getString("offername"));
              specialOffer.setDiscountType(DiscountType.valueOf(rs.getString("discounttype")));
              specialOffer.setDiscountValue(rs.getDouble("discountvalue"));
              return specialOffer;
           }
           
           return null;
        }
        
    }


}
