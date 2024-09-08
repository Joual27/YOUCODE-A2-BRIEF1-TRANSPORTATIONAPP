package com.youcode.transportationApp.ui.process;

import com.youcode.transportationApp.contracts.Contract;
import com.youcode.transportationApp.contracts.ContractRepository;
import com.youcode.transportationApp.enums.DiscountType;
import com.youcode.transportationApp.enums.OfferStatus;
import com.youcode.transportationApp.specialOffers.SpecialOffer;
import com.youcode.transportationApp.specialOffers.SpecialOfferRepository;
import com.youcode.transportationApp.specialOffers.SpecialOfferService;
import com.youcode.transportationApp.specialOffers.interfaces.SpecialOfferRepositoryI;
import com.youcode.transportationApp.specialOffers.interfaces.SpecialOfferServiceI;
import com.youcode.transportationApp.utils.DatesValidator;

import java.util.Scanner;
import java.util.List;
import java.util.UUID;
import java.time.LocalDate;

import java.util.InputMismatchException;

public class SpecialOfferMenuProcess {

    private final SpecialOfferServiceI specialOfferService;
    private final SpecialOfferRepositoryI specialOfferRepository;
    private final static Scanner sc = new Scanner(System.in);
    private final DatesValidator validator;

    public SpecialOfferMenuProcess() {
        specialOfferRepository = new SpecialOfferRepository();
        specialOfferService = new SpecialOfferService(specialOfferRepository);
        validator = new DatesValidator();

    }

    public void handleFetchingAllSpecialOffers() {
        List<SpecialOffer> offers = specialOfferService.getAllSpecialOffers();
        if (offers.isEmpty()) {
            System.out.println("No available special offers");
            return;
        }

        String leftAlignFormat = "| %-12s | %-20s | %-25s | %-20s | %-20s | %-15s | %-15s | %-15s | %-20s |%n";

        System.out.format(
                "+--------------+----------------------+---------------------------+----------------------+----------------------+-----------------+-----------------+-----------------+----------------------+%n");
        System.out.format(
                "| Offer ID     | Offer Name           | Offer Description         | Starting Date        | End Date             | Discount Type   | Discount Value  | Offer Status    | Contract ID          |%n");
        System.out.format(
                "+--------------+----------------------+---------------------------+----------------------+----------------------+-----------------+-----------------+-----------------+----------------------+%n");

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

        System.out.format(
                "+--------------+----------------------+---------------------------+----------------------+----------------------+-----------------+-----------------+-----------------+----------------------+%n");
    }

    public void handleSpecialOfferCreation() {
        System.out.println("Please Provide the Offer Name:");
        String offerName = sc.nextLine();

        System.out.println("Please Provide the Offer Description:");
        String offerDescription = sc.nextLine();

        LocalDate startingDate;
        while (true) {
            int startingDateDay = validator.handleDays();
            int startingDateMonth = validator.handleMonths();
            int startingDateYear = validator.handleYear();

            startingDate = LocalDate.of(startingDateYear, startingDateMonth, startingDateDay);

            if (!validator.validateStartingDate(startingDate)) {
                System.out.println("Starting Date can't be after currentTime");
            } else {
                break;
            }
        }

        LocalDate endingDate;
        System.out.println("PLease provide the contract's Ending date : ");

        while (true) {
            int EndingDateDay = validator.handleDays();
            int EndingDateMonth = validator.handleMonths();
            int EndingDateYear = validator.handleYear();

            endingDate = LocalDate.of(EndingDateYear, EndingDateMonth, EndingDateDay);

            if (!validator.validateEndingDate(startingDate, endingDate)) {
                System.out.println("Ending Date can't be after starting Date");
            } else {
                break;
            }
        }

        DiscountType discountType = handleDiscountType();

        System.out.println("Please Provide the Discount Value:");
        double discountValue = sc.nextDouble();
        sc.nextLine();

        System.out.println("Please Provide the Conditions:");
        String conditions = sc.nextLine();

        OfferStatus offerStatus = handleOfferStatus();

        sc.nextLine();

        Contract contract = handleContract();

        SpecialOffer specialOffer = new SpecialOffer();
        specialOffer.setOfferId(UUID.randomUUID().toString());
        specialOffer.setOfferName(offerName);
        specialOffer.setOfferDescription(offerDescription);
        specialOffer.setStartingDate(startingDate);
        specialOffer.setEndDate(endingDate);
        specialOffer.setDiscountType(discountType);
        specialOffer.setDiscountValue(discountValue);
        specialOffer.setConditions(conditions);
        specialOffer.setOfferStatus(offerStatus);
        specialOffer.setContract(contract);

        specialOfferService.addSpecialOffer(specialOffer);
        System.out.println("Special Offer " + offerName + " created successfully.");

    }

    public void handleSpecialOfferUpdate() {
        System.out.println("Please Provide the Offer ID to update:");
        String offerId = sc.nextLine();

        SpecialOffer offer = specialOfferService.getSpecialOfferById(offerId);
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

        LocalDate startingDate;
        System.out.println("Current Starting Date: " + offer.getStartingDate());
        System.out.println("Press any key to update or press Enter to keep the same Date:");
        String startDateInput = sc.nextLine();
        if (startDateInput.trim().isEmpty()) {
            startingDate = offer.getStartingDate();
        } else {
            while (true) {
                int startingDateDay = validator.handleDays();
                int startingDateMonth = validator.handleMonths();
                int startingDateYear = validator.handleYear();
                startingDate = LocalDate.of(startingDateYear, startingDateMonth, startingDateDay);
                if (!validator.validateStartingDate(startingDate)) {
                    System.out.println("Starting Date can't be before the current time.");
                } else {
                    offer.setStartingDate(startingDate);
                    break;
                }
            }
        }

        offer.setStartingDate(startingDate);

        LocalDate endingDate;
        System.out.println("Current End Date: " + offer.getEndDate());
        System.out.println("Press any key to update or press Enter to keep the same Date:");
        String endDateInput = sc.nextLine();
        if (endDateInput.trim().isEmpty()) {
            offer.setEndDate(offer.getEndDate());
        } else {
            while (true) {
                int endingDateDay = validator.handleDays();
                int endingDateMonth = validator.handleMonths();
                int endingDateYear = validator.handleYear();

                endingDate = LocalDate.of(endingDateYear, endingDateMonth, endingDateDay);

                if (!validator.validateEndingDate(startingDate, endingDate)) {
                    System.out.println("Ending Date can't be before the Starting Date.");
                } else {
                    offer.setEndDate(endingDate);
                    break;
                }
            }
        }

        System.out.println("Current Discount Type: " + offer.getDiscountType());
        System.out.println("press any key to update discount type (or press Enter to keep the current):");
        String userInput = sc.nextLine();
        if (userInput.trim().isEmpty()) {
            offer.setDiscountType(offer.getDiscountType());
        } else {
            DiscountType discountType = handleDiscountType();
            if (discountType != null) {
                offer.setDiscountType(discountType);
            }
        }

        sc.nextLine();

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
        System.out.println("press any key to update offer status (or press Enter to keep the current):");

        String userInput2 = sc.nextLine();
        if (userInput2.trim().isEmpty()) {
            offer.setOfferStatus(offer.getOfferStatus());
        } else {
            OfferStatus offerStatus = handleOfferStatus();
            if (offerStatus != null) {
                offer.setOfferStatus(offerStatus);
            }
        }
        SpecialOffer updatedSpecialOffer = specialOfferService.updateSpecialOffer(offer);

        System.out.println("Special Offer " + updatedSpecialOffer.getOfferName() + " updated successfully.");
        
    }

    public void handleSpecialOfferDeletion(){
        System.out.println("Please Provide the Offer ID to delete:");
        String offerId = sc.nextLine();

        SpecialOffer deletedSpecialOffer = specialOfferService.deleteSpecialOffer(offerId);
        System.out.println("Special Offer " + deletedSpecialOffer.getOfferName() + " deleted successfully.");

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

    private Contract handleContract() {
        Contract c;
        while (true) {
            System.out.println("PLease provide the contract ID :");
            String contractId = sc.nextLine();

            ContractRepository cr = new ContractRepository();
            c = cr.getContractById(contractId);

            if (c != null) {
                break;
            }
        }

        return c;
    }

}
