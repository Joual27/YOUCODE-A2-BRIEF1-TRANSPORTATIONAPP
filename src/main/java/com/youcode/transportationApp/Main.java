package com.youcode.transportationApp;

import java.sql.Connection;
import java.sql.SQLException;

import com.youcode.transportationApp.database.DbConnection;
import com.youcode.transportationApp.ui.Menu;



public class Main{
   public static void main(String[] args) {
       try{
         DbConnection dbConnection = DbConnection.getInstance();
         Connection cnx = dbConnection.getConnection();
         if( cnx != null && !cnx.isClosed()){
            System.out.println("Connected to db successfully !");
            Menu menu = new Menu();
            menu.startMenu();
         }
         else{
            System.out.println("unable to connect to db !");
         }
       }
       catch(SQLException e){
           e.printStackTrace();
       }
   }
}