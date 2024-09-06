package com.youcode.transportationApp.specialOffers.interfaces;

import java.sql.SQLException;
import java.util.List;

import com.youcode.transportationApp.specialOffers.SpecialOffer;

public interface SpecialOfferRepositoryI {

    public  List<SpecialOffer> getAllSpecialOffers() throws SQLException;

    public SpecialOffer getSpecialOfferById(String offerId);    

    public void createSpecialOffer(SpecialOffer specialOffer) throws SQLException;

    public void editSpecialOffer(SpecialOffer specialOffer) throws SQLException;

    public void removeSpecialOffer(String offerId) throws SQLException;

    public SpecialOffer getSpecialOfferByContractId(String contractId);
}
