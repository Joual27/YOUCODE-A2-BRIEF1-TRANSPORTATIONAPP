package com.youcode.transportationApp.searchHistory.interfaces;

import com.youcode.transportationApp.searchHistory.SearchHistory;

import java.util.List;

public interface SearchHistoryRepositoryI {
    public List<SearchHistory> getTopSearchedTrips(String customerEmail);
    public void updateCustomerSearchHistory(SearchHistory searchHistory, String customerEmail);
    public SearchHistory getSearchHistoryElementByDepartureAndDestination(String departure, String destination , String customerEmail);
    public void createSearchHistoryElement(SearchHistory searchHistory);
}
