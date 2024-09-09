package com.youcode.transportationApp.auth.interfaces;

import com.youcode.transportationApp.auth.Customer;

public interface AuthRepositoryI {
   public Customer getCustomerByEmail(String email);

   public Customer createCustomer(Customer customer);

   public void updateCustomer(Customer customer);
}
