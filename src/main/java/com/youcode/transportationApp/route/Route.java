package com.youcode.transportationApp.route;

public class Route {
    private String routeId;
    private String departure;
    private String destination;
    private Double distance;

    public Route() {}

    public String getRouteId() {
        return routeId;
    }
    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getDeparture() {
        return departure;
    }
    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getDestination() {
        return this.destination;
    }
    public void setDestination(String destination) {
        this.destination = destination;
    }
    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

}
