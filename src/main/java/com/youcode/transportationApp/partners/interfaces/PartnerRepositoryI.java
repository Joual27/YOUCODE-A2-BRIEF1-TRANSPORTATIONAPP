package com.youcode.transportationApp.partners.interfaces;

import java.util.ArrayList;

import com.youcode.transportationApp.partners.Partner;

public interface PartnerRepositoryI {

    public ArrayList<Partner> getAllPartners();

    public Partner getPartnerById(String partnerId);    

    public void createPartner(Partner partner);

    public void editPartner(Partner partner);

    public void removePartner(String partnerId);
}