package com.youcode.transportationApp.route.interfaces;

import com.youcode.transportationApp.route.Route;

public interface RouteRepositoryI {

    public Route findRouteByDepartureAndDestination(String departure, String destination);

    public void createRoute(Route route);
}
