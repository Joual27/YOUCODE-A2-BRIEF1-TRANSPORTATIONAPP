package com.youcode.transportationApp.specialOffers;


import com.youcode.transportationApp.specialOffers.interfaces.SpecialOfferRepositoryI;
import com.youcode.transportationApp.specialOffers.interfaces.SpecialOfferServiceI;

import java.util.List;


public class SpecialOfferService implements SpecialOfferServiceI{

    private final SpecialOfferRepositoryI specialOfferRepository;

    public SpecialOfferService(SpecialOfferRepositoryI specialOfferRepository) {
        this.specialOfferRepository = specialOfferRepository;
    }

    @Override
    public List<SpecialOffer> getAllSpecialOffers(){
        return specialOfferRepository.getAllSpecialOffers();
    }

    public SpecialOffer addSpecialOffer(SpecialOffer s){
       specialOfferRepository.createSpecialOffer(s);
       return s;
    }

    public SpecialOffer updateSpecialOffer(SpecialOffer s) {
        specialOfferRepository.editSpecialOffer(s);
        return s;
    }

    public SpecialOffer deleteSpecialOffer(String offerId){
        SpecialOffer specialOfferToDelete = getSpecialOfferById(offerId); 
        specialOfferRepository.removeSpecialOffer(offerId);
        return specialOfferToDelete;
    }

   
    public SpecialOffer getSpecialOfferById(String offerId){
        return specialOfferRepository.getSpecialOfferById(offerId);
    }

}
