package com.youcode.transportationApp.specialOffers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.youcode.transportationApp.contracts.Contract;
import com.youcode.transportationApp.database.DbConnection;
import com.youcode.transportationApp.enums.DiscountType;
import com.youcode.transportationApp.enums.OfferStatus;

public class SpecialOfferRepository {

    private final Connection cnx;

    public SpecialOfferRepository() throws SQLException {
        this.cnx = DbConnection.getInstance().getConnection();
    }

    public List<SpecialOffer> getAllSpecialOffers() {
        List<SpecialOffer> offers = new ArrayList<>();
        String sqlQuery = "SELECT * FROM special_offers WHERE deleted_at IS NULL;";

        try (PreparedStatement stmt = cnx.prepareStatement(sqlQuery);
             ResultSet resultSet = stmt.executeQuery()) {

            while (resultSet.next()) {
                SpecialOffer offer = new SpecialOffer();
                offer.setOfferId(resultSet.getString("offerid"));
                offer.setOfferName(resultSet.getString("offername"));
                offer.setOfferDescription(resultSet.getString("offerdescription"));
                offer.setStartingDate(resultSet.getTimestamp("startingdate"));
                offer.setEndDate(resultSet.getTimestamp("enddate"));
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
        String query = "INSERT INTO special_offers (offerid, offername, offerdescription, startingdate, enddate, discounttype, discountvalue, conditions, offerstatus, contractid) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setString(1, offer.getOfferId());
            stmt.setString(2, offer.getOfferName());
            stmt.setString(3, offer.getOfferDescription());
            stmt.setTimestamp(4, offer.getStartingDate());
            stmt.setTimestamp(5, offer.getEndDate());
            stmt.setString(6, offer.getDiscountType().name());
            stmt.setDouble(7, offer.getDiscountValue());
            stmt.setString(8, offer.getConditions());
            stmt.setString(9, offer.getOfferStatus().name());
            stmt.setString(10, offer.getContract().getContractId());
            stmt.executeUpdate();
        }
    }

    public void editSpecialOffer(SpecialOffer offer) throws SQLException {
        String query = "UPDATE special_offers SET offername = ?, offerdescription = ?, startingdate = ?, enddate = ?, discounttype = ?, discountvalue = ?, conditions = ?, offerstatus = ?, contractid = ? WHERE offerid = ?";

        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setString(1, offer.getOfferName());
            stmt.setString(2, offer.getOfferDescription());
            stmt.setTimestamp(3, offer.getStartingDate());
            stmt.setTimestamp(4, offer.getEndDate());
            stmt.setString(5, offer.getDiscountType().name());
            stmt.setDouble(6, offer.getDiscountValue());
            stmt.setString(7, offer.getConditions());
            stmt.setString(8, offer.getOfferStatus().name());
            stmt.setString(9, offer.getContract().getContractId());
            stmt.setString(10, offer.getOfferId());
            stmt.executeUpdate();
        }
    }

    public SpecialOffer getSpecialOfferById(String offerId) throws SQLException {
        String query = "SELECT * FROM special_offers WHERE offerid = ?";

        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setString(1, offerId);
            ResultSet res = stmt.executeQuery();
            if (res.next()) {
                SpecialOffer offer = new SpecialOffer();
                offer.setOfferId(res.getString("offerid"));
                offer.setOfferName(res.getString("offername"));
                offer.setOfferDescription(res.getString("offerdescription"));
                offer.setStartingDate(res.getTimestamp("startingdate"));
                offer.setEndDate(res.getTimestamp("enddate"));
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
        String query = "UPDATE special_offers SET deleted_at = NOW() WHERE offerid = ?";

        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setString(1, offerId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
