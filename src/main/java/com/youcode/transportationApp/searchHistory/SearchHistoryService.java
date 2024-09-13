package com.youcode.transportationApp.searchHistory;

import com.youcode.transportationApp.searchHistory.interfaces.SearchHistoryServiceI;

import java.util.List;

public class SearchHistoryService implements SearchHistoryServiceI {

    private final SearchHistoryRepository searchHistoryRepository = new SearchHistoryRepository();

    @Override
    public List<SearchHistory> getTop3Searches(String customerEmail){
        return searchHistoryRepository.getTopSearchedTrips(customerEmail);
    }

    @Override
    public void updateCustomerSearchHistory(SearchHistory searchHistory, String customerEmail){
        searchHistoryRepository.updateCustomerSearchHistory(searchHistory, customerEmail);
    }

    @Override
    public SearchHistory getSearchHistoryElementByDepartureAndDestination(String departure, String destination,String customerEmail){
        return searchHistoryRepository.getSearchHistoryElementByDepartureAndDestination(departure, destination , customerEmail);
    }

    @Override
    public void saveSearchHistoryElement(SearchHistory searchHistory){
        searchHistoryRepository.createSearchHistoryElement(searchHistory);
    }

}
