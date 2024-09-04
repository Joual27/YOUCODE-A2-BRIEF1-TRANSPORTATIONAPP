package com.youcode.transportationApp.specialOffers.interfaces;

import java.sql.SQLException;
import java.util.ArrayList;
import com.youcode.transportationApp.specialOffers.SpecialOffer;

public interface SpecialOfferRepositoryI {

    public ArrayList<SpecialOffer> getAllSpecialOffers() throws SQLException;

    public SpecialOffer getSpecialOfferById(String offerId) throws SQLException;    

    public void createSpecialOffer(SpecialOffer specialOffer) throws SQLException;

    public void editSpecialOffer(SpecialOffer specialOffer) throws SQLException;

    public void removeSpecialOffer(String offerId) throws SQLException;
}
