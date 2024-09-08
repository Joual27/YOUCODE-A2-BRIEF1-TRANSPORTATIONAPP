package com.youcode.transportationApp.specialOffers.interfaces;

import java.util.List;

import com.youcode.transportationApp.specialOffers.SpecialOffer;

public interface SpecialOfferRepositoryI {

    public  List<SpecialOffer> getAllSpecialOffers();

    public SpecialOffer getSpecialOfferById(String offerId);    

    public void createSpecialOffer(SpecialOffer specialOffer) ;

    public void editSpecialOffer(SpecialOffer specialOffer) ;

    public void removeSpecialOffer(String offerId) ;

    public SpecialOffer getSpecialOfferByContractId(String contractId);
}
