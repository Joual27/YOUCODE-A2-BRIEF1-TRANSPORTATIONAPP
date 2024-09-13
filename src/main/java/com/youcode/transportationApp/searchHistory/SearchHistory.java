package com.youcode.transportationApp.searchHistory;

import com.youcode.transportationApp.auth.Customer;

public class SearchHistory {
    private String departure;
    private String destination;
    private int numberOfOccurrences;
    private Customer customer;

    public SearchHistory() {}

    public String getDeparture() {
        return departure;
    }
    public void setDeparture(String departure) {
        this.departure = departure;
    }
    public String getDestination() {
        return destination;
    }
    public void setDestination(String destination) {
        this.destination = destination;
    }
    public int getNumberOfOccurrences() {
        return numberOfOccurrences;
    }

    public void setNumberOfOccurrences(int numberOfOccurrences) {
        this.numberOfOccurrences = numberOfOccurrences;
    }

    public void incrementNumberOfOccurrences() {
        this.numberOfOccurrences += 1;
    }

    public Customer getCustomer() {
        return customer;
    }
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
