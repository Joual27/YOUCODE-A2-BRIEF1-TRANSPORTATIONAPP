package com.youcode.transportationApp.specialOffers.interfaces;

import java.sql.SQLException;

public interface SpecialOfferServiceI {

    public void fetchAllSpecialOffers() throws SQLException;
    
    public void addSpecialOffer() throws SQLException;

    public void updateSpecialOffer() throws SQLException;

    public void deleteSpecialOffer() throws SQLException;
}
