package com.youcode.transportationApp.auth;

import com.youcode.transportationApp.auth.interfaces.AuthRepositoryI;
import com.youcode.transportationApp.auth.interfaces.AuthServiceI;

public class AuthService implements AuthServiceI {

    private final AuthRepositoryI authRepository;

    public AuthService() {
        this.authRepository = new AuthRepository();
    }
    public Customer authenticateCustomer(String email){
        return authRepository.getUserByEmail(email);
    }
}
