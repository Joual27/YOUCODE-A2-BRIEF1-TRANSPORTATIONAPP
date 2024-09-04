package com.youcode.transportationApp.specialOffers;

import com.youcode.transportationApp.enums.DiscountType;
import com.youcode.transportationApp.enums.OfferStatus;
import com.youcode.transportationApp.contracts.Contract;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;
import java.util.InputMismatchException;


public class SpecialOfferService {

    private final SpecialOfferRepository specialOfferRepository;
    private final Scanner sc;

    public SpecialOfferService(SpecialOfferRepository specialOfferRepository) {
        this.specialOfferRepository = specialOfferRepository;
        this.sc = new Scanner(System.in);
    }

    public void fetchAllSpecialOffers() {
        List<SpecialOffer> offers = specialOfferRepository.getAllSpecialOffers();

        if (offers.isEmpty()) {
            System.out.println("No available special offers");
            return;
        }

        String leftAlignFormat = "| %-12s | %-20s | %-25s | %-20s | %-20s | %-15s | %-15s | %-15s | %-20s |%n";

        System.out.format("+--------------+----------------------+---------------------------+----------------------+----------------------+-----------------+-----------------+-----------------+----------------------+%n");
        System.out.format("| Offer ID     | Offer Name           | Offer Description         | Starting Date        | End Date             | Discount Type   | Discount Value  | Offer Status    | Contract ID          |%n");
        System.out.format("+--------------+----------------------+---------------------------+----------------------+----------------------+-----------------+-----------------+-----------------+----------------------+%n");

        for (SpecialOffer offer : offers) {
            System.out.format(leftAlignFormat,
                    offer.getOfferId(),
                    offer.getOfferName(),
                    offer.getOfferDescription(),
                    offer.getStartingDate(),
                    offer.getEndDate(),
                    offer.getDiscountType(),
                    offer.getDiscountValue(),
                    offer.getOfferStatus(),
                    offer.getContract().getContractId());
        }

        System.out.format("+--------------+----------------------+---------------------------+----------------------+----------------------+-----------------+-----------------+-----------------+----------------------+%n");
    }

    public void addSpecialOffer() {
        System.out.println("Please Provide the Offer Name:");
        String offerName = sc.nextLine();

        System.out.println("Please Provide the Offer Description:");
        String offerDescription = sc.nextLine();

        Timestamp startingDate = new Timestamp(new Date().getTime());
        System.out.println("Please Provide the End Date (format: YYYY-MM-DD HH:MM:SS):");
        Timestamp endDate = Timestamp.valueOf(sc.nextLine());

        DiscountType discountType = handleDiscountType();

        System.out.println("Please Provide the Discount Value:");
        double discountValue = sc.nextDouble();
        sc.nextLine(); 

        System.out.println("Please Provide the Conditions:");
        String conditions = sc.nextLine();

        OfferStatus offerStatus = handleOfferStatus();

       
        Contract contract = getContract();

        SpecialOffer specialOffer = new SpecialOffer();
        specialOffer.setOfferId(UUID.randomUUID().toString());
        specialOffer.setOfferName(offerName);
        specialOffer.setOfferDescription(offerDescription);
        specialOffer.setStartingDate(startingDate);
        specialOffer.setEndDate(endDate);
        specialOffer.setDiscountType(discountType);
        specialOffer.setDiscountValue(discountValue);
        specialOffer.setConditions(conditions);
        specialOffer.setOfferStatus(offerStatus);
        specialOffer.setContract(contract);

        try {
            specialOfferRepository.createSpecialOffer(specialOffer);
            System.out.println("Special Offer " + offerName + " created successfully.");
        } catch (SQLException e) {
            System.err.println("Error while creating special offer: " + e.getMessage());
        }
    }

    public void updateSpecialOffer() {
        System.out.println("Please Provide the Offer ID to update:");
        String offerId = sc.nextLine();

        try {
            SpecialOffer offer = specialOfferRepository.getSpecialOfferById(offerId);
            if (offer == null) {
                System.out.println("Special offer with ID " + offerId + " not found.");
                return;
            }

            System.out.println("Current Offer Name: " + offer.getOfferName());
            System.out.println("Enter new Offer Name (or press Enter to keep the current):");
            String offerName = sc.nextLine();
            if (!offerName.trim().isEmpty()) {
                offer.setOfferName(offerName);
            }

            System.out.println("Current Offer Description: " + offer.getOfferDescription());
            System.out.println("Enter new Offer Description (or press Enter to keep the current):");
            String offerDescription = sc.nextLine();
            if (!offerDescription.trim().isEmpty()) {
                offer.setOfferDescription(offerDescription);
            }

            System.out.println("Current Starting Date: " + offer.getStartingDate());
            System.out.println("Enter new Starting Date (format: YYYY-MM-DD HH:MM:SS) (or press Enter to keep the current):");
            String startDateInput = sc.nextLine();
            if (!startDateInput.trim().isEmpty()) {
                offer.setStartingDate(Timestamp.valueOf(startDateInput));
            }

            System.out.println("Current End Date: " + offer.getEndDate());
            System.out.println("Enter new End Date (format: YYYY-MM-DD HH:MM:SS) (or press Enter to keep the current):");
            String endDateInput = sc.nextLine();
            if (!endDateInput.trim().isEmpty()) {
                offer.setEndDate(Timestamp.valueOf(endDateInput));
            }

            System.out.println("Current Discount Type: " + offer.getDiscountType());
            System.out.println("Enter new Discount Type (or press Enter to keep the current):");
            DiscountType discountType = handleDiscountType();
            if (discountType != null) {
                offer.setDiscountType(discountType);
            }

            System.out.println("Current Discount Value: " + offer.getDiscountValue());
            System.out.println("Enter new Discount Value (or press Enter to keep the current):");
            String discountValueInput = sc.nextLine();
            if (!discountValueInput.trim().isEmpty()) {
                offer.setDiscountValue(Double.parseDouble(discountValueInput));
            }

            System.out.println("Current Conditions: " + offer.getConditions());
            System.out.println("Enter new Conditions (or press Enter to keep the current):");
            String conditions = sc.nextLine();
            if (!conditions.trim().isEmpty()) {
                offer.setConditions(conditions);
            }

            System.out.println("Current Offer Status: " + offer.getOfferStatus());
            System.out.println("Enter new Offer Status (or press Enter to keep the current):");
            OfferStatus offerStatus = handleOfferStatus();
            if (offerStatus != null) {
                offer.setOfferStatus(offerStatus);
            }

            Contract contract = getContract();
            if (contract != null) {
                offer.setContract(contract);
            }

            specialOfferRepository.editSpecialOffer(offer);
            System.out.println("Special Offer " + offer.getOfferName() + " updated successfully.");
        } catch (SQLException e) {
            System.err.println("Error while updating special offer: " + e.getMessage());
        }
    }

    public void deleteSpecialOffer() {
        System.out.println("Please Provide the Offer ID to delete:");
        String offerId = sc.nextLine();

        
        specialOfferRepository.removeSpecialOffer(offerId);
        System.out.println("Special Offer " + offerId + " deleted successfully.");
       
    }

    private DiscountType handleDiscountType() {
        while (true) {
            System.out.println("Select Discount Type:");
            System.out.println("1. Percentage");
            System.out.println("2. Fixed Amount");
    
            try {
                int choice = sc.nextInt();
    
                switch (choice) {
                    case 1:
                        return DiscountType.PERCENTAGE;
                    case 2:
                        return DiscountType.FIX_AMOUNT;
                    default:
                        System.out.println("INVALID CHOICE!");
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input, please enter a number.");
                sc.nextLine();
            }
        }
    }
    

    private OfferStatus handleOfferStatus() {
        while (true) {
            System.out.println("Select Offer Status:");
            System.out.println("1. Active");
            System.out.println("2. Expired");
            System.out.println("3. Suspended");
    
            try {
                int choice = sc.nextInt();
    
                switch (choice) {
                    case 1:
                        return OfferStatus.ACTIVE;
                    case 2:
                        return OfferStatus.EXPIRED;
                    case 3:
                        return OfferStatus.SUSPENDED;
                    default:
                        System.out.println("INVALID CHOICE!");
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input, please enter a number.");
                sc.nextLine(); 
            }
        }
    }
    

    private Contract getContract() {
        System.out.println("Please Provide the Contract ID:");
        String contractId = sc.nextLine();

        Contract contract = new Contract();
        contract.setContractId(contractId);

        return contract;
    }
}
