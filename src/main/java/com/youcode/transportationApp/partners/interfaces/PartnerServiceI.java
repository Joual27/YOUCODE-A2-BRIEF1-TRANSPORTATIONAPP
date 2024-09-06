package com.youcode.transportationApp.partners.interfaces;


import com.youcode.transportationApp.partners.Partner;


import java.util.List;

public interface PartnerServiceI {

    public List<Partner> getAllPartners();

    public Partner addPartner(Partner p);
    
    public Partner updatePartner(Partner p);
    
    public Partner deletePartner(String partnerId);


} 
