package com.telesens.afanasiev.simulator;

import com.telesens.afanasiev.helper.DaoUtils;
import com.telesens.afanasiev.rules.PassengerGenerationRules;
import com.telesens.afanasiev.rules.PassengerGenerationUnit;

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

        long stationIdFrom = station.getID();
        PassengerGenerationUnit passGenerationUnit = passGenerationRules.peekRuleForStation(stationIdFrom);

        if (!passGenerationUnit.isActual(curTime)) {
            passGenerationRules.pollRuleForStation(stationIdFrom);
            passGenerationUnit = passGenerationRules.peekRuleForStation(stationIdFrom);
        }

        Collection<Passenger> passengers = passGenerationUnit.getPassengers(curTime);

        if (passengers.size() == 0)
            return;

        try {
            for (Passenger passenger : passengers)
                DaoUtils.setPrivateField(passenger, "ID", iPassCounter++);
        } catch (NoSuchFieldException | IllegalAccessException exc) {
            exc.printStackTrace();
        }

        station.addPassengers(passengers);
    }
}
