package com.youcode.transportationApp.partners;


import com.youcode.transportationApp.partners.interfaces.PartnerRepositoryI;
import com.youcode.transportationApp.partners.interfaces.PartnerServiceI;

import java.util.List;
import java.util.Scanner;


public class PartnerService implements PartnerServiceI{
    
    private final PartnerRepositoryI partnerRepository;
    private Scanner sc;

    public PartnerService(PartnerRepositoryI partnerRepository){
        this.partnerRepository = partnerRepository;
        sc = new Scanner(System.in);
    }

    @Override
    public List<Partner> getAllPartners(){
        List<Partner> partners = partnerRepository.getAllPartners();
        if(partners.isEmpty()){
            return null;
        }
        return partners;
    }

    @Override
    public Partner addPartner(Partner p){
        partnerRepository.createPartner(p);
        return p;
    }


    
    @Override
    public Partner updatePartner(Partner partnerToUpdate){
        partnerRepository.editPartner(partnerToUpdate);
        return partnerToUpdate;
    }

    @Override
    public Partner deletePartner(String partnerId){
        partnerRepository.removePartner(partnerId);
        return partnerRepository.getPartnerById(partnerId);
    }

        

}


