package com.youcode.transportationApp.auth;

import com.youcode.transportationApp.auth.interfaces.AuthRepositoryI;
import com.youcode.transportationApp.database.DbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthRepository implements AuthRepositoryI {

    private Connection cnx;


    public AuthRepository(){
        try{
            this.cnx = DbConnection.getInstance().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Customer getUserByEmail(String email){
        String query = "SELECT * FROM customers WHERE email = ? and banned_at IS NULL";
        try{
            PreparedStatement stmt = cnx.prepareStatement(query);
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            Customer customer = new Customer();
            if(rs.next()){
                customer.setEmail(rs.getString("email"));
                customer.setFirstName(rs.getString("firstName"));
                customer.setLastName(rs.getString("lastName"));
                customer.setPhoneNumber(rs.getString("phoneNumber"));
            }
            return customer;
        }
        catch(SQLException e){
            e.printStackTrace();
            return null;
        }
    }
}
