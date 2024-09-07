package com.youcode.transportationApp.ui.process;

import com.youcode.transportationApp.contracts.Contract;
import com.youcode.transportationApp.contracts.ContractRepository;
import com.youcode.transportationApp.contracts.ContractService;
import com.youcode.transportationApp.contracts.interfaces.ContractRepositoryI;
import com.youcode.transportationApp.contracts.interfaces.ContractServiceI;
import com.youcode.transportationApp.enums.ContractStatus;
import com.youcode.transportationApp.partners.Partner;
import com.youcode.transportationApp.partners.PartnerRepository;
import com.youcode.transportationApp.partners.PartnerService;
import com.youcode.transportationApp.partners.interfaces.PartnerRepositoryI;
import com.youcode.transportationApp.partners.interfaces.PartnerServiceI;
import com.youcode.transportationApp.utils.DatesValidator;

import java.util.List;
import java.util.Scanner;
import java.util.UUID;

import java.time.LocalDate;
import java.util.InputMismatchException;



public class ContractMenuProcess {


    private final ContractServiceI contractService;
    private final ContractRepositoryI contractRepository;
    private final DatesValidator validator;
    private final static Scanner sc = new Scanner(System.in);


    public ContractMenuProcess(){
        this.contractRepository = new ContractRepository();
        contractService = new ContractService(contractRepository);
        validator = new DatesValidator();

    }
    
    public void handleFetchingAllContracts(){
        List<Contract> contracts = contractService.getAllContracts();
        if (contracts.isEmpty()) {
            System.out.println("No available contracts");
            return;
        }

        String leftAlignFormat = "| %-12s | %-12s | %-15s | %-15s | %-10s | %-25s | %-12s | %-20s |%n";

        System.out.format("+--------------+--------------+-----------------+-----------------+------------+---------------------------+--------------+----------------------+%n");
        System.out.format("| Contract ID  | Partner ID   | Starting Date   | Ending Date     | Special Rate | Agreement Conditions       | Renewable    | Contract Status       |%n");
        System.out.format("+--------------+--------------+-----------------+-----------------+------------+---------------------------+--------------+----------------------+%n");

        for (Contract contract : contracts) {
            System.out.format(leftAlignFormat,
                    contract.getContractId(),
                    contract.getPartner().getPartnerId(),
                    contract.getStartingDate(),
                    contract.getEndDate(),
                    contract.getSpecialRate(),
                    contract.getAgreementConditions(),
                    contract.isRenewable(),
                    contract.getContractStatus());
        }

        System.out.format("+--------------+--------------+-----------------+-----------------+------------+---------------------------+--------------+----------------------+%n");
    }

    public void handleContractCreation(){

        PartnerRepositoryI pr = new PartnerRepository();
        PartnerServiceI ps = new PartnerService(pr);
        System.out.println("PLease provide the contract's starting date : ");

        LocalDate startingDate;
        while (true) {
            int startingDateDay = validator.handleDays();
            int startingDateMonth = validator.handleMonths();
            int startingDateYear = validator.handleYear();

            startingDate = LocalDate.of(startingDateYear, startingDateMonth, startingDateDay);

            if(!validator.validateStartingDate(startingDate)){
                System.out.println("Starting Date can't be after currentTime");
            }
            else{
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

                if(!validator.validateEndingDate(startingDate,endingDate)){
                    System.out.println("Ending Date can't be after starting Date");
                }
                else{
                    break;
                }
            }


        System.out.println("PLease provide the Special Rate of this contract : ");
        Double specialRate = sc.nextDouble();
        sc.nextLine();

        System.out.println("PLease provide the Agreement conditions of this contract : ");
        String agreementConditions = sc.nextLine();

        boolean renewable = handleRenewable();
        ContractStatus contractStatus = handleContractStatus();

         String partnerId;

        while(true){
            System.out.println("Please enter the ID of the partner that this contract belongs to");
            partnerId = sc.nextLine();

            if( ps.getPartnerById(partnerId) != null ){
                break;
            }
        }

        Contract newContract = new Contract();
        newContract.setContractId(UUID.randomUUID().toString());
        newContract.setStartingDate(startingDate);
        newContract.setEndDate(endingDate);
        newContract.setSpecialRate(specialRate);
        newContract.setAgreementConditions(agreementConditions);
        newContract.setRenewable(renewable);
        newContract.setContractStatus(contractStatus);
        Partner partner = new Partner();
        partner.setPartnerId(partnerId);
        newContract.setPartner(partner);

        Contract createdContract = contractService.addContract(newContract);
        System.out.println("Contract " + createdContract.getContractId() + " Created Successfully !");
        
    }


    public void handleContractUpdate(){
        handleFetchingAllContracts();
        System.out.println("Please enter the ID of the contract you want to update:");
    
        String id = sc.nextLine();
    
        Contract existingContract = contractService.getContractById(id);
        if (existingContract == null) {
            System.out.println("No available contract with this ID");
            return;
        }
    
        System.out.println("Updating Contract " + existingContract.getContractId());
    
        LocalDate startingDate;
        System.out.println("Current Start Date: " + existingContract.getStartingDate());
        System.out.println("Press any key to update or enter to keep same Date");
        String userInput = sc.nextLine();
        if (userInput.isEmpty()) {
            startingDate = existingContract.getStartingDate();
        } else {
            while (true) {
                int startingDateDay = validator.handleDays();
                int startingDateMonth = validator.handleMonths();
                int startingDateYear = validator.handleYear();
    
                startingDate = LocalDate.of(startingDateYear, startingDateMonth, startingDateDay);
    
                if (!validator.validateStartingDate(startingDate)) {
                    System.out.println("Starting Date can't be before current time.");
                } else {
                    break;
                }
            }
        }
    
        LocalDate endDate;
        System.out.println("Current End Date: " + existingContract.getEndDate());
        System.out.println("Press any key to update or enter to keep same Date");
        String userInput2 = sc.nextLine();
        if (userInput2.isEmpty()) {
            endDate = existingContract.getEndDate();
        } else {
            while (true) {
                int endingDateDay = validator.handleDays();
                int endingDateMonth = validator.handleMonths();
                int endingDateYear = validator.handleYear();
    
                endDate = LocalDate.of(endingDateYear, endingDateMonth, endingDateDay);
    
                if (!validator.validateEndingDate(startingDate, endDate)) {
                    System.out.println("Ending Date can't be before Starting Date.");
                } else {
                    break;
                }
            }
        }
    
        System.out.println("Current Special Rate: " + existingContract.getSpecialRate());
        System.out.println("Enter new value or leave blank to keep current:");
        String specialRateInput = sc.nextLine();
        double specialRate = specialRateInput.isEmpty() ? existingContract.getSpecialRate() : Double.parseDouble(specialRateInput);
    
        System.out.println("Current Agreement Conditions: " + existingContract.getAgreementConditions());
        System.out.println("Enter new value or leave blank to keep current:");
        String agreementConditions = sc.nextLine();
        if (agreementConditions.isEmpty()) {
            agreementConditions = existingContract.getAgreementConditions();
        }
    
        
        System.out.println("Current Renewable Status: " + (existingContract.isRenewable() ? "Yes" : "No"));
        System.out.println("Enter 1 for Yes, 2 for No, or leave blank to keep current:");
        String renewableInput = sc.nextLine();
        boolean renewable = renewableInput.isEmpty() ? existingContract.isRenewable() : renewableInput.equals("1");
    
        
        ContractStatus contractStatus = handleContractStatusUpdate(existingContract.getContractStatus());
    
        Contract contractToUpdate = new Contract(
                existingContract.getContractId(),
                startingDate,
                endDate,
                specialRate,
                agreementConditions,
                renewable,
                contractStatus,
                existingContract.getPartner()
        );

        Contract updatedContract = contractService.updateContract(contractToUpdate);
        System.out.println("Contract " + updatedContract.getContractId() + " updated successfully!");

    }


    public void handleContractDeletion(){
        handleFetchingAllContracts();

        System.out.println("Please enter the ID of the contract you want to delete");

        String id = sc.nextLine();
 
        Contract existingContract = contractService.getContractById(id);
        if (existingContract == null) {
            System.out.println("No available contract with this ID");
            return;
        } else {
            Contract deletedContract = contractService.deleteContract(id);
            System.out.println("Contract "+ deletedContract.getContractId() +" deleted successfully");
        }

    }
    private boolean handleRenewable(){
        boolean renewable;
        while(true){
            System.out.println("Is this contract renewable ?");
            System.out.println("1. Yes\t2. No");
            int choice = sc.nextInt();
            
            if(choice == 1){
                renewable = true;
                break;
            }
            else if (choice == 2 ){
                renewable = false;
                break;
            }
            else{
                System.out.println("Invalid choice");
            }
        }
        return renewable;
    }

    private ContractStatus handleContractStatus(){
        while (true) {
            System.out.println("Please select the Contract STatus : \n");
            System.out.println("1. Ongoing\t2. Ended\t3. Suspended");

            try{
                int choice = sc.nextInt();

                switch(choice){
                    case 1 :
                        return ContractStatus.ONGOING;
                    case 2 :
                        return ContractStatus.ENDED;
                    case 3 :
                        return ContractStatus.SUSPENDED;
                    default:
                        System.out.println("Invalid choice, TRY AGAIN !");           
                }
            }
            catch(InputMismatchException e){
                System.out.println("PLease provide a number !");
                sc.nextLine();
            }
        }
    }

    public ContractStatus handleContractStatusUpdate(ContractStatus currentStatus){
        while (true) {
            try {
                System.out.println("Current Contract Status: " + currentStatus);
                System.out.println("Enter 0 to keep the current value.");
                System.out.println("1. Ongoing\t2. Ended\t3. Suspended");

                int choice = sc.nextInt();
                sc.nextLine(); 

                switch (choice) {
                    case 0:
                        return currentStatus;
                    case 1:
                        return ContractStatus.ONGOING;
                    case 2:
                        return ContractStatus.ENDED;
                    case 3:
                        return ContractStatus.SUSPENDED;
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
