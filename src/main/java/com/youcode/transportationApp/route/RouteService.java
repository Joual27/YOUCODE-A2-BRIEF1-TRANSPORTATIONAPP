package com.youcode.transportationApp.route;

import com.youcode.transportationApp.route.interfaces.RouteServiceI;

public class RouteService implements RouteServiceI {

    private final RouteRepository routeRepository;
    public RouteService(){
        routeRepository = new RouteRepository();
    }

    public Route getRouteByDepartureAndDestination(String departure, String destination){
        return routeRepository.findRouteByDepartureAndDestination(departure,destination);
    }


    public Route createRoute(Route route){
        routeRepository.createRoute(route);
        return route;
    }
}
