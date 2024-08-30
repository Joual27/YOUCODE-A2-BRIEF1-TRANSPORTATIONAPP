package com.youcode.transportationApp.database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.youcode.transportationApp.config.DatabaseConfig;

public class DbConnection {
    private static DbConnection instance;
    private Connection connection;
    private String url;
    private String username;
    private String password;

    private DbConnection () throws SQLException {
        this.url = DatabaseConfig.getUrl();
        this.password = DatabaseConfig.getPassword();
        this.username = DatabaseConfig.getUsername();

        this.connection = DriverManager.getConnection(url, username, password);
    }


    public static DbConnection getInstance() throws SQLException {
       if (instance == null){
         instance = new DbConnection();
       }

       return instance;
    }

    public Connection getConnection(){
        return connection;
    }
}
