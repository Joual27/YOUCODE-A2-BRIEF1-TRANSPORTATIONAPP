package com.youcode.transportationApp.contracts;

import java.util.ArrayList;
import java.time.LocalDate;

import com.youcode.transportationApp.enums.ContractStatus;
import com.youcode.transportationApp.partners.Partner;
import com.youcode.transportationApp.specialOffers.SpecialOffer;
import com.youcode.transportationApp.tickets.Ticket;

public class Contract {
    private String contractId;
    private LocalDate startingDate;
    private LocalDate endDate;
    private double specialRate;
    private String agreementConditions;
    private boolean renewable;
    private ContractStatus contractStatus;
    private ArrayList<Ticket> tickets;
    private ArrayList<SpecialOffer> offers;
    private Partner partner;

    public Contract (){};

    public Contract(String contractId, LocalDate startingDate, LocalDate endDate, double specialRate, 
                    String agreementConditions, boolean renewable, ContractStatus contractStatus, Partner partner) {
            this.contractId = contractId;
            this.startingDate = startingDate;
            this.endDate = endDate;
            this.specialRate = specialRate;
            this.agreementConditions = agreementConditions;
            this.renewable = renewable;
            this.contractStatus = contractStatus;
            this.partner = partner;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
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

    public double getSpecialRate() {
        return specialRate;
    }

    public void setSpecialRate(double specialRate) {
        this.specialRate = specialRate;
    }

    public String getAgreementConditions() {
        return agreementConditions;
    }

    public void setAgreementConditions(String agreementConditions) {
        this.agreementConditions = agreementConditions;
    }

    public boolean isRenewable() {
        return renewable;
    }

    public void setRenewable(boolean renewable) {
        this.renewable = renewable;
    }

    public ContractStatus getContractStatus() {
        return contractStatus;
    }

    public void setContractStatus(ContractStatus contractStatus) {
        this.contractStatus = contractStatus;
    }

    public ArrayList<Ticket> getTickets(){
        return this.tickets;
    }

    public void setTickets(ArrayList<Ticket> tickets){
        this.tickets = tickets;
    }

    public ArrayList<SpecialOffer> getOffers(){
        return this.offers;
    }

    public void setOffers(ArrayList<SpecialOffer> offers){
        this.offers = offers;
    }


    public Partner getPartner(){
        return this.partner;
    }

    public void setPartner(Partner partner){
        this.partner = partner;
    }
    
}
