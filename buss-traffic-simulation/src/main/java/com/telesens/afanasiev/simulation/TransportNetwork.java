package com.telesens.afanasiev.simulation;

import com.telesens.afanasiev.helper.DaoUtils;
import com.telesens.afanasiev.simulation.impl.RouteImpl;

import java.util.*;

/**
 * Created by oleg on 12/9/15.
 */
public class TransportNetwork implements Observer, TransportMap {

    private volatile static TransportNetwork uniqueInstance;

    private Set<Arc<Station>> arcs;
    private Collection<Route<Station>> routes;

    private TransportNetwork() {
        arcs = new HashSet<>();
        routes = new ArrayList<>();
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

    @SafeVarargs
    public final void createRoute(long id, String number, String description, Direct direct, double cost, Station firstStation, Arc<Station>...listArcs)
        throws NoSuchFieldException, IllegalAccessException {

        createRoute(id, number, description, direct, cost, firstStation, Arrays.asList(listArcs));
    }

    public void createRoute(long id, String number, String description, Direct direct, double cost, Station firstStation, List<Arc<Station>> listArcs)
            throws NoSuchFieldException, IllegalAccessException {
        arcs.addAll(listArcs);

        Route<Station> route = new RouteImpl<>(number, description, direct, cost, firstStation, listArcs);
        DaoUtils.setPrivateField(route, "ID", id);

        routes.add(route);
    }

    public void addRoute(Route<Station> route) {
        for (Arc<Station> arc : route)
            arcs.add(arc);

        routes.add(route);
    }

    public Route getRouteById(long id) {
        for (Route route : routes) {
            if (route.getId() == id)
                return route;
        }
        return null;
    }

    public Route getRouteByNumber(String number) {
        for (Route route : routes) {
            if (route.getNumber().equals(number)) {
                return route;
            }
        }
        return null;
    }

    public Collection<Route<Station>> getAllRoutes() {
        return routes;
    }

    public String allRoutesToString() {
        StringBuilder sb = new StringBuilder();

        for (Route route : routes)
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
        for (Route<Station> route : routes) {
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

    private void addArcs(Arc<Station>...listArcs) {
        for (Arc<Station> arc : listArcs)
            if (!arcs.contains(arc))
                arcs.add(arc);
    }

}
