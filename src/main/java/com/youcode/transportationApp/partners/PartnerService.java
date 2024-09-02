package com.youcode.transportationApp.partners;

import com.youcode.transportationApp.partners.interfaces.PartnerRepositoryI;
import com.youcode.transportationApp.partners.interfaces.PartnerServiceI;
import java.util.ArrayList;

public class PartnerService implements PartnerServiceI{
    
    private final PartnerRepositoryI partnerRepository;

    public PartnerService(PartnerRepositoryI partnerRepository){
        this.partnerRepository = partnerRepository;
    }

    public void fetchAllPartners(){
        ArrayList<Partner> partners = partnerRepository.getAllPartners();

        if(partners.isEmpty()){
            System.out.println("No available partners");
            return;
        }

        String leftAlignFormat = "| %-12s | %-20s | %-20s | %-18s | %-15s | %-25s | %-18s | %-20s |%n";

        System.out.format("+--------------+----------------------+----------------------+--------------------+-----------------+---------------------------+--------------------+----------------------+%n");
        System.out.format("| Partner ID   | Company Name          | Commercial Contact    | Transportation Type| Geographic Zone | Special Conditions         | Partnership Status | Creation Date         |%n");
        System.out.format("+--------------+----------------------+----------------------+--------------------+-----------------+---------------------------+--------------------+----------------------+%n");


        for(Partner partner : partners){
            System.out.format(leftAlignFormat,partner.getPartnerId(),partner.getCompanyName() , partner.getCommercialContact() , partner.getTransportationType() , partner.getGeographicZone() , partner.getSpecialConditions() , partner.getPartnershipStatus() , partner.getCreationDate());
        }

        System.out.format("+--------------+----------------------+----------------------+--------------------+-----------------+---------------------------+--------------------+----------------------+%n");
    
    }
}
