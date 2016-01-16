package com.telesens.afanasiev.model.simulation;

import com.telesens.afanasiev.model.identities.*;
import com.telesens.afanasiev.model.identities.impl.RoutePairImpl;

import java.util.*;

/**
 * Created by oleg on 12/9/15.
 */
public class TransportNetwork implements com.telesens.afanasiev.model.simulation.Observer, TransportMap {

    private volatile static TransportNetwork uniqueInstance;

    private Set<Arc<Station>> arcs;

    private Collection<Route<Station>> allRoutesBuff;
    private Collection<Route<Station>> circRoutes;
    private Collection<RoutePair<Station>> simpleRoutes;

    private TransportNetwork() {
        arcs = new HashSet<>();
        circRoutes = new ArrayList<>();
        simpleRoutes = new ArrayList<>();
        allRoutesBuff = new ArrayList<>();
    }

    public static TransportNetwork getInstance() {
        if (uniqueInstance == null) {
            synchronized (TransportNetwork.class) {
                if (uniqueInstance == null)
                    uniqueInstance = new TransportNetwork();
            }
        }

        return uniqueInstance;
    }

    public void registerCircularRoute(Route<Station> route) {
        readArcs(route);
        if (!isRegisteredRoute(route))
            circRoutes.add(route);
    }

    public void registerSimpleRoute(Route<Station> routeForward, Route<Station> routeBack) {
        RoutePair<Station> pair = new RoutePairImpl<>(routeForward, routeBack);
        registerSimpleRoute(pair);
    }

    public void registerSimpleRoute(RoutePair<Station> pair) {
        if (!isRegisteredRoute(pair.getForwardRoute()) && !isRegisteredRoute(pair.getBackRoute())) {
            readArcs(pair.getForwardRoute());
            readArcs(pair.getBackRoute());
            allRoutesBuff.add(pair.getForwardRoute());
            allRoutesBuff.add(pair.getBackRoute());
            simpleRoutes.add(pair);
        }
    }

    public Route getRouteById(long id) {
        for (Route route : allRoutesBuff) {
            if (route.getId() == id)
                return route;
        }
        return null;
    }

    public Route getRouteByNumber(String number) {
        for (Route route : allRoutesBuff) {
            if (route.getNumber().equals(number)) {
                return route;
            }
        }
        return null;
    }

    public Collection<Route<Station>> getAllCircRoutes() {
        return circRoutes;
    }

    public Collection<RoutePair<Station>> getAllSimpleRoutes() {
        return simpleRoutes;
    }

    public Collection<Route<Station>> getAllRoutes() {
        return allRoutesBuff;
    }

    public String allRoutesToString() {
        StringBuilder sb = new StringBuilder();

        for (Route route : allRoutesBuff)
            sb.append(String.format("%s %n%n", route));

        return sb.toString();
    }

    public void removeStation() {
        throw new UnsupportedOperationException();
    }

    public void addStation() {
        throw new UnsupportedOperationException();
    }

    public Station getStationById(long id) {
        for (Route<Station> route : allRoutesBuff) {
            for (Arc<Station> arc : route)
                if (arc.getNodeLeft().getId() == id)
                    return arc.getNodeLeft();
                else if (arc.getNodeRight().getId() == id)
                    return arc.getNodeRight();
        }

        return null;
    }

    public void tick(Date dateTime) {
        Collection<Station> listOfStations = getAllStations();

        for (Station station : listOfStations)
            station.tick(dateTime);
    }

    private Collection<Station> getAllStations() {
        Collection<Station> listOfStations = new ArrayList<>();
        Station startNode;
        Station finishNode;

        for (Arc<Station> arc : arcs) {
            startNode = arc.getNodeLeft();
                if (!listOfStations.contains(startNode))
                    listOfStations.add(startNode);

            finishNode = arc.getNodeRight();
                if (!listOfStations.contains(finishNode))
                    listOfStations.add(finishNode);
        }

        return listOfStations;
    }

    private void readArcs(Route<Station> route) {
        for (Arc<Station> arc : route)
            if (!arcs.contains(arc))
                arcs.add(arc);
    }

    private boolean isRegisteredRoute(Route<Station> route){
        for (Route<Station> routeBuff : allRoutesBuff)
            if (route.getId() == routeBuff.getId())
                return true;

        return false;
    }

}
