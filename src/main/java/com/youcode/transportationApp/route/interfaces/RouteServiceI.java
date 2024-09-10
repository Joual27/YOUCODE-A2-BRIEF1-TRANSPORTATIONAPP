package com.youcode.transportationApp.route.interfaces;

import com.youcode.transportationApp.route.Route;

public interface RouteServiceI {
    public Route getRouteByDepartureAndDestination(String departure, String destination);

    public Route createRoute(Route route);
}
