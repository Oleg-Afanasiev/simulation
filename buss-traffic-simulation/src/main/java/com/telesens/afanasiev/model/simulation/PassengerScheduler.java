package com.telesens.afanasiev.model.simulation;

import com.telesens.afanasiev.model.identities.*;
import com.telesens.afanasiev.model.helper.DaoUtils;
import com.telesens.afanasiev.model.rules.PassengerGenerationRules;


import java.util.Collection;
import java.util.Date;

/**
 * Created by oleg on 12/9/15.
 */
public class PassengerScheduler implements Observer {

    private Collection<Route<Station>> routes;
    private long iPassCounter = 1;
    private PassengerGenerationRules passGenerationRules;

    public PassengerScheduler(PassengerGenerationRules passGenerationRules) {
        this.passGenerationRules = passGenerationRules;
    }

    public void tick(Date curTime) {
        for (Route<Station> route : routes)
            generatePassengersForRoute(route, curTime);

    }

    public void registerRoutes(Collection<Route<Station>> routes) {
        this.routes = routes;
    }

    @Override
    public String toString() {
        return "Passenger scheduler";
    }

    private void generatePassengersForRoute(Route<Station> route, Date curTime) {
        Station station = route.getFirstNode();
        generatePassengersForStation(station, curTime);

        for (Arc<Station> arc : route) {
            station = arc.getOppositeNode(station);
            generatePassengersForStation(station, curTime);
        }
    }

    private void generatePassengersForStation(Station station, Date curTime) {

        long stationIdFrom = station.getId();
        PassengerGenerationTask passGenerationTask = passGenerationRules.peekRuleForStation(stationIdFrom);

        if (passGenerationTask == null) // task doesn't exist for this station
            return;

        if (!passGenerationTask.isActual(curTime)) {
            passGenerationRules.pollRuleForStation(stationIdFrom);
            passGenerationTask = passGenerationRules.peekRuleForStation(stationIdFrom);
        }

        Collection<Passenger> passengers = passGenerationTask.getPassengers(curTime);

        if (passengers.size() == 0)
            return;

        for (Passenger passenger : passengers)
            DaoUtils.setPrivateId(passenger, iPassCounter++);


        station.addPassengers(passengers);
    }
}
