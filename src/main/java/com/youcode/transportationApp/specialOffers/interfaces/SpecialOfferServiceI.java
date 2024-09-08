package com.youcode.transportationApp.specialOffers.interfaces;

import java.util.List;

import com.youcode.transportationApp.specialOffers.SpecialOffer;

public interface SpecialOfferServiceI {

    public List<SpecialOffer> getAllSpecialOffers();
    
    public SpecialOffer addSpecialOffer(SpecialOffer s) ;

    public SpecialOffer updateSpecialOffer(SpecialOffer s);

    public SpecialOffer deleteSpecialOffer(String offerId) ;

    public SpecialOffer getSpecialOfferById(String offerId);
}
