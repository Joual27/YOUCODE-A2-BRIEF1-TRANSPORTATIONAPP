package com.youcode.transportationApp.auth.interfaces;

import com.youcode.transportationApp.auth.Customer;

public interface AuthServiceI {
    public Customer authenticateCustomer(String email);
}
