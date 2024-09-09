package com.youcode.transportationApp.ui.process;

import com.youcode.transportationApp.auth.AuthService;
import com.youcode.transportationApp.auth.Customer;
import com.youcode.transportationApp.auth.interfaces.AuthServiceI;
import com.youcode.transportationApp.utils.Session;
import com.youcode.transportationApp.utils.Validator;

import java.util.Scanner;

public class CustomerMenuProcess {

    private final AuthServiceI authService;
    private final Scanner sc;

    public CustomerMenuProcess() {
        authService = new AuthService();
        sc = new Scanner(System.in);
    }

    public void handleCustomerProfileUpdate(){
        System.out.println("Updating Profile Of Customer : " + Session.getInstance().getLoggedEmail());
        Customer loggedCustomer = authService.getCustomerByEmail(Session.getInstance().getLoggedEmail());

        System.out.println("Current First Name : " + loggedCustomer.getFirstName());
        System.out.println("Enter a new value to update or click enter to keep same value !");
        String firstName = sc.nextLine();

        String newFirstName = firstName.trim().isEmpty() ? loggedCustomer.getFirstName() : firstName;

        System.out.println("Current Family Name : " + loggedCustomer.getFamilyName());
        System.out.println("Enter a new value to update or click enter to keep same value !");

        String familyName = sc.nextLine();

        String newFamilyName = familyName.trim().isEmpty() ? loggedCustomer.getFamilyName() : familyName;

        System.out.println("Current Phone Number : " + loggedCustomer.getPhoneNumber());
        String phoneNumber;
        while (true){
            System.out.println("Enter a new value to update or click enter to keep same value !");
            phoneNumber = sc.nextLine();
            if(Validator.validatePhoneNumber(phoneNumber)){
                loggedCustomer.setPhoneNumber(phoneNumber);
                break;
            }
            else if(phoneNumber.trim().isEmpty()){
               phoneNumber = loggedCustomer.getPhoneNumber();
               break;
            }
            else{
                System.out.println("Invalid phone number ! Try Again !");
            }
        }
        Customer customerToUpdate = new Customer();
        customerToUpdate.setFirstName(newFirstName);
        customerToUpdate.setFamilyName(newFamilyName);
        customerToUpdate.setPhoneNumber(phoneNumber);
        customerToUpdate.setEmail(Session.getInstance().getLoggedEmail());

        Customer updatedCustomer =  authService.updateCustomer(customerToUpdate);
        System.out.println("Customer profile updated successfully !");
    }
}
