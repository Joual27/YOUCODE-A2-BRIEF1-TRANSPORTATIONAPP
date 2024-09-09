package com.youcode.transportationApp.auth.interfaces;

import com.youcode.transportationApp.auth.Customer;

public interface AuthServiceI {
    public Customer getCustomerByEmail(String email);

    public Customer createCustomer(Customer customer);

    public Customer updateCustomer(Customer customer);
}
