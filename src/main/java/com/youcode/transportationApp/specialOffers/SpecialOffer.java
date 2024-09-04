package com.youcode.transportationApp.specialOffers;

import java.sql.Timestamp;
import java.time.LocalDate;

import com.youcode.transportationApp.contracts.Contract;
import com.youcode.transportationApp.enums.DiscountType;
import com.youcode.transportationApp.enums.OfferStatus;

public class SpecialOffer {
    private String offerId;
    private String offerName;
    private String offerDescription;
    private LocalDate startingDate;
    private LocalDate endDate;
    private DiscountType discountType;
    private double discountValue;
    private String conditions;
    private OfferStatus offerStatus;
    private Contract belongsToContract;
    

    public SpecialOffer (){}
    
    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public String getOfferName() {
        return offerName;
    }

    public void setOfferName(String offerName) {
        this.offerName = offerName;
    }

    public String getOfferDescription() {
        return offerDescription;
    }

    public void setOfferDescription(String offerDescription) {
        this.offerDescription = offerDescription;
    }

    public LocalDate getStartingDate() {
        return startingDate;
    }

    public void setStartingDate(LocalDate startingDate) {
        this.startingDate = startingDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public DiscountType getDiscountType() {
        return discountType;
    }

    public void setDiscountType(DiscountType discountType) {
        this.discountType = discountType;
    }

    public double getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(double discountValue) {
        this.discountValue = discountValue;
    }

    public String getConditions() {
        return conditions;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
    }

    public OfferStatus getOfferStatus() {
        return offerStatus;
    }

    public void setOfferStatus(OfferStatus offerStatus) {
        this.offerStatus = offerStatus;
    }


    public Contract getContract(){
        return this.belongsToContract;
    }


    public void setContract(Contract c){
        this.belongsToContract = c;
    }
}
