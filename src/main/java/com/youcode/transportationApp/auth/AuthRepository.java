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

    public Customer getCustomerByEmail(String email){
        String query = "SELECT * FROM customers WHERE email = ? and banned_at IS NULL";
        try{
            PreparedStatement stmt = cnx.prepareStatement(query);
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            Customer customer = new Customer();
            if(rs.next()){
                customer.setEmail(rs.getString("email"));
                customer.setFirstName(rs.getString("firstName"));
                customer.setFamilyName(rs.getString("familyName"));
                customer.setPhoneNumber(rs.getString("phoneNumber"));
                return customer;
            }
            return null;
        }
        catch(SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public Customer createCustomer(Customer customer){
        String query = "INSERT INTO customers VALUES ( ? , ? , ? , ? )";
        try{
            PreparedStatement stmt = cnx.prepareStatement(query);
            stmt.setString(1, customer.getEmail());
            stmt.setString(2, customer.getFirstName());
            stmt.setString(3, customer.getFamilyName());
            stmt.setString(4, customer.getPhoneNumber());
            stmt.executeUpdate();
            Customer c = new Customer();
            c.setEmail(customer.getEmail());
            c.setFirstName(customer.getFirstName());
            c.setFamilyName(customer.getFamilyName());
            c.setPhoneNumber(customer.getPhoneNumber());
            return c;
        }
        catch(SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public void updateCustomer(Customer customer){
        String query = "UPDATE customers SET firstName = ?, familyName = ?, phoneNumber = ? WHERE email = ?";
        try{
            PreparedStatement stmt = cnx.prepareStatement(query);
            stmt.setString(1, customer.getFirstName());
            stmt.setString(2, customer.getFamilyName());
            stmt.setString(3, customer.getPhoneNumber());
            stmt.setString(4, customer.getEmail());
            stmt.executeUpdate();

        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }
}
