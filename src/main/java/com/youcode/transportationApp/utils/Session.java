package com.youcode.transportationApp.utils;

public class Session {

    private static Session instance;
    private String loggedEmail;

    private Session(){};

    public static Session getInstance(){
        if(instance == null){
            instance = new Session();
        }
        return instance;
    };

    public String getLoggedEmail() {
        return loggedEmail;
    }
    public void setLoggedEmail(String loggedEmail) {
        this.loggedEmail = loggedEmail;
    }
}
