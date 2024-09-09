package com.youcode.transportationApp.auth;

import com.youcode.transportationApp.auth.interfaces.AuthRepositoryI;
import com.youcode.transportationApp.auth.interfaces.AuthServiceI;

public class AuthService implements AuthServiceI {

    private final AuthRepositoryI authRepository;

    public AuthService() {
        this.authRepository = new AuthRepository();
    }
    public Customer getCustomerByEmail(String email){
        return authRepository.getCustomerByEmail(email);
    }

    public Customer createCustomer(Customer customer){
        return authRepository.createCustomer(customer);
    }

    public Customer updateCustomer(Customer customer){
        authRepository.updateCustomer(customer);
        return customer;
    }
}
