package com.youcode.transportationApp.partners;


import com.youcode.transportationApp.partners.interfaces.PartnerRepositoryI;
import com.youcode.transportationApp.partners.interfaces.PartnerServiceI;

import java.util.List;


public class PartnerService implements PartnerServiceI{
    
    private final PartnerRepositoryI partnerRepository;
 

    public PartnerService(PartnerRepositoryI partnerRepository){
        this.partnerRepository = partnerRepository;
    }

    @Override
    public List<Partner> getAllPartners(){
        
        return partnerRepository.getAllPartners();
    
    }

    @Override
    public Partner addPartner(Partner p){
        partnerRepository.createPartner(p);
        return p;
    }

    @Override

    public Partner getPartnerById(String partnerId){
        return partnerRepository.getPartnerById(partnerId);
    }


    
    @Override
    public Partner updatePartner(Partner partnerToUpdate){
        partnerRepository.editPartner(partnerToUpdate);
        return partnerToUpdate;
    }

    @Override
    public Partner deletePartner(String partnerId){
        Partner existingPartner = partnerRepository.getPartnerById(partnerId);
        partnerRepository.removePartner(partnerId);
        return existingPartner;
    }
 

}


