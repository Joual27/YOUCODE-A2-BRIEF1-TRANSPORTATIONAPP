package com.youcode.transportationApp.auth.interfaces;

import com.youcode.transportationApp.auth.Customer;

public interface AuthRepositoryI {
   public Customer getUserByEmail(String email);

//   public User createUser(User user);
}
