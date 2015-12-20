package com.afanasiev.telesens.simulate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * Created by oleg on 12/9/15.
 */
public class TransportNetwork implements Observer, TransportMap {

    private Collection<Arc<Station>> arcs;
    private Collection<Route<Station>> routes;

    public TransportNetwork() {
        arcs = new ArrayList<>();
        routes = new ArrayList<>();
    }

    public void addArc(Arc<Station> arc) {
        arcs.add(arc);
    }

    public void createRoute(long id, String name, String description, Direct direct, double cost, Station startStation, Arc<Station>...listArcs)
        throws NoSuchFieldException, IllegalAccessException {
        Route route = new Route(name, description, direct, cost, startStation, listArcs);
        DaoUtils.setPrivateField(route, "ID", id);

        routes.add(route);
    }

    public Route getRouteById(long id) {
        for (Route route : routes) {
            if (route.getID() == id)
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
                if (arc.getNodeLeft().getID() == id)
                    return arc.getNodeLeft();
                else if (arc.getNodeRight().getID() == id)
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

}
