package com.afanasiev.telesens.simulate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * Created by oleg on 12/9/15.
 */
public class PassengerSheduler implements Observer {

    private ReportCollector reportCollector;
    private Collection<Route<Station>> routes;
    private long iPassCounter = 1;

    public PassengerSheduler() {
        reportCollector = ReportCollector.getInstance();
    }

    public void tick(Date curTime) {
        reportCollector.sendMessage(this.toString(), "");

        for (Route<Station> route : routes)
            generatePassengersForRoute(route, curTime);

    }

    public void registerRoutes(Collection<Route<Station>> routes) {
        this.routes = routes;
    }

    @Override
    public String toString() {
        return "Passenger sheduler";
    }

    private void generatePassengersForRoute(Route<Station> route, Date curTime) {
        Station nextStation = route.getFirstNode();
        generatePassengersForStation(nextStation, curTime);

        for (Arc<Station> arc : route) {
            nextStation = arc.getOppositeNode(nextStation);
            generatePassengersForStation(nextStation, curTime);
        }
    }

    private void generatePassengersForStation(Station station, Date curTime) {
        long idFrom = station.getID();
        long idTo = idFrom == 8 ? 1 : 8;

        Passenger passenger = new Passenger(idFrom, idTo);
        try {
            DaoUtils.setPrivateField(passenger, "ID", iPassCounter++);
        } catch (NoSuchFieldException | IllegalAccessException exc) {
            exc.printStackTrace();
        }

        Collection<Passenger> passengers = new ArrayList<>();
        passengers.add(passenger);

        station.addPassengers(passengers);
    }
}
