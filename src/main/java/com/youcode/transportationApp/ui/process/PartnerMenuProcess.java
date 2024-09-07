package com.youcode.transportationApp.ui.process;

import com.youcode.transportationApp.enums.PartnershipStatus;
import com.youcode.transportationApp.enums.TransportationType;
import com.youcode.transportationApp.partners.Partner;
import com.youcode.transportationApp.partners.PartnerRepository;
import com.youcode.transportationApp.partners.PartnerService;
import com.youcode.transportationApp.partners.interfaces.PartnerRepositoryI;
import com.youcode.transportationApp.partners.interfaces.PartnerServiceI;


import java.util.List;
import java.sql.Timestamp;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.UUID;


public class PartnerMenuProcess {


    private final PartnerServiceI partnerService ;
    private final PartnerRepositoryI partnerRepository;
    private static final Scanner sc = new Scanner(System.in);

    public PartnerMenuProcess(){
        partnerRepository = new PartnerRepository();
        partnerService = new PartnerService(partnerRepository);
    }

    public void handlePartnersFetching(){
        List<Partner> partners = partnerService.getAllPartners();
        
        if(partners.isEmpty()){
            System.out.println("No Available Partners");
            return;
        }
        else{
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


    public void handlePartnerCreation(){
        System.out.println("Please Provide the company name :");
        String companyName = sc.nextLine();
        System.out.println("Please Provide the Commerical contact :");
        String commercialContact = sc.nextLine();
        TransportationType transportationType = handleTransportationType();
        sc.nextLine();
        System.out.println("Please Provide the Geographic Zone :");
        String geographicZone = sc.nextLine();
        System.out.println("Please Provide the Special Conditions :");
        String specialConditions = sc.nextLine();
        PartnershipStatus partnershipStatus = handlePartnershipStatus();
        Timestamp creationDate = new Timestamp(new Date().getTime());
        Partner p = new Partner(UUID.randomUUID().toString() , companyName , commercialContact , transportationType , geographicZone , specialConditions  , partnershipStatus , creationDate);
        partnerService.addPartner(p);
        System.out.println("Partner +" + p.getCompanyName() + " Created Successfully !");
    }


    public void handlePartnerUpdate(){
        handlePartnersFetching();
        System.out.println("Pls enter the id of the partner you want to update");

        String id = sc.nextLine();
        
        Partner existingPartner = partnerRepository.getPartnerById(id);
        if (existingPartner == null) {
            System.out.println("NO AVAILABLE PARTNER WITH THIS ID");
            return;
        }
        else{
            System.out.println("Updating Partner "+ existingPartner.getCompanyName());
            System.out.println("Current Company Name : " + existingPartner.getCompanyName());
            System.out.println("Enter new value Or leave blank field if you don't want to update !");

            String companyName = sc.nextLine();
            if(companyName.isEmpty()) companyName = existingPartner.getCompanyName();

            System.out.println("Current Commercial Contact : " + existingPartner.getCommercialContact());
            System.out.println("Enter new value Or leave blank field if you don't want to update !");

            String commercialContact = sc.nextLine();
            if(commercialContact.isEmpty()) commercialContact = existingPartner.getCommercialContact();

            TransportationType transportationType = handleTransportationTypeUpdate(existingPartner.getTransportationType());

            System.out.println("Current Geographic Zone : " + existingPartner.getGeographicZone());
            System.out.println("Enter new value Or leave blank field if you don't want to update !");

            String geographiqueZone = sc.nextLine();
            if(geographiqueZone.isEmpty()) geographiqueZone = existingPartner.getGeographicZone();

            System.out.println("Current special Conditions : " + existingPartner.getSpecialConditions());
            System.out.println("Enter new value Or leave blank field if you don't want to update !");

            String specialConditions = sc.nextLine();
            if(specialConditions.isEmpty()) specialConditions = existingPartner.getSpecialConditions();

            PartnershipStatus partnershipStatus = handlePartnershipStatusUpdate(existingPartner.getPartnershipStatus());

            Partner partnerToUpdate = new Partner(existingPartner.getPartnerId(), companyName, commercialContact, transportationType, geographiqueZone, specialConditions, partnershipStatus, existingPartner.getCreationDate());

            Partner updatedPartner = partnerService.updatePartner(partnerToUpdate);

            System.out.println("Partner "+ updatedPartner.getCompanyName()+" Updated Successfully !");
            
        }
    }


    public void handlePartnerDeletion(){
        System.out.println("Pls enter the id of the partner you want to Delete");

        String id = sc.nextLine();

        Partner existingPartner = partnerRepository.getPartnerById(id);
        if (existingPartner == null) {
            System.out.println("NO AVAILABLE PARTNER WITH THIS ID");
            return;
        }
        else{
            Partner deletedPartner = partnerService.deletePartner(id);
            System.out.println("Partner "+ deletedPartner.getCompanyName() + " Deleted Successfully !");
        }
    
    }
     
    

    public TransportationType handleTransportationType(){
        while (true) {
            System.out.println("Please select the Transportation Type : \n");
            System.out.println("1. Train\t2. Bus\t3. Airplane");
            
            try{
               int choice = sc.nextInt();

               switch (choice) {
                case 1:
                    return TransportationType.TRAIN;
                case 2 :
                    return TransportationType.BUS;
                case 3 :
                    return TransportationType.AIR;
                default:
                    System.out.println("INVALID CHOICE !");
                    break;
               }
            }
            catch(InputMismatchException e){
                System.out.println("invalid input , please give a number");
                sc.nextLine();
            }
        }
    }

    public PartnershipStatus handlePartnershipStatus(){
        while (true) {
            System.out.println("Please select the Partnership Status : \n");
            System.out.println("1. Active\t2. Inactive\t 3. Suspended");
            
            try{
               int choice = sc.nextInt();

               switch (choice) {
                case 1:
                    return PartnershipStatus.ACTIVE;
                case 2 :
                    return PartnershipStatus.INACTIVE;
                case 3 :
                    return PartnershipStatus.SUSPENDED;
                default:
                    System.out.println("INVALID CHOICE !");
                    break;
               }
            }
            catch(InputMismatchException e){
                System.out.println("invalid input , please give a number");
                sc.nextLine();
            }
        }
    }

    
    public TransportationType handleTransportationTypeUpdate(TransportationType currentType) {
        while (true) {
            try {
                System.out.println("Current Transportation Type: " + currentType);
                System.out.println("Enter 0 to keep the current value.");
                System.out.println("1. Train\t2. Bus\t3. Airplane");

                int choice = sc.nextInt();
                sc.nextLine(); 

                switch (choice) {
                    case 0:
                        return currentType;
                    case 1:
                        return TransportationType.TRAIN;
                    case 2:
                        return TransportationType.BUS;
                    case 3:
                        return TransportationType.AIR;
                    default:
                        System.out.println("INVALID CHOICE! Please select 0, 1, 2, or 3.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input, please enter a number.");
                sc.nextLine(); 
            }
        }
    }

    
    public PartnershipStatus handlePartnershipStatusUpdate(PartnershipStatus currentPartnershipStatus){
        while (true) {
            try {
                System.out.println("Current Partnership STatus: " + currentPartnershipStatus);
                System.out.println("Enter 0 to keep the current value.");
                System.out.println("1. Active\t2. Inactive\t3. Suspended");

                int choice = sc.nextInt();
                sc.nextLine(); 

                switch (choice) {
                    case 0:
                        return currentPartnershipStatus;
                    case 1:
                        return PartnershipStatus.ACTIVE;
                    case 2:
                        return PartnershipStatus.INACTIVE;
                    case 3:
                        return PartnershipStatus.SUSPENDED;
                    default:
                        System.out.println("INVALID CHOICE! Please select 0, 1, 2, or 3.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input, please enter a number.");
                sc.nextLine(); 
            }
        }
    }

}
