package com.youcode.transportationApp.searchHistory.interfaces;

import com.youcode.transportationApp.searchHistory.SearchHistory;

import java.util.List;

public interface SearchHistoryServiceI {

    public List<SearchHistory> getTop3Searches(String customerEmail);
    public void updateCustomerSearchHistory(SearchHistory searchHistory, String customerEmail);
//
    public SearchHistory getSearchHistoryElementByDepartureAndDestination(String departure, String destination , String customerEmail);
    public void saveSearchHistoryElement(SearchHistory searchHistory);
}
