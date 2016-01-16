package com.telesens.afanasiev.model.loader;

import com.telesens.afanasiev.dao.ArcDAO;
import com.telesens.afanasiev.dao.RouteDAO;
import com.telesens.afanasiev.dao.StationDAO;
import com.telesens.afanasiev.dao.impl.jaxb.ArcDAOImpl;
import com.telesens.afanasiev.dao.impl.jaxb.RouteDAOImpl;
import com.telesens.afanasiev.dao.impl.jaxb.StationDAOImpl;
import com.telesens.afanasiev.model.identities.Arc;
import com.telesens.afanasiev.model.identities.Route;
import com.telesens.afanasiev.model.identities.RoutePair;
import com.telesens.afanasiev.model.identities.Station;
import com.telesens.afanasiev.model.simulation.TransportNetwork;

import java.util.*;

/**
 * Created by oleg on 1/13/16.
 */
public class TransportNetworkLoader {

    public TransportNetwork loadBusNetwork() {

        StationDAO stationDAO = new StationDAOImpl();
        Collection<Station> stations = stationDAO.getAll();

        ArcDAO<Station> arcDAO = new ArcDAOImpl<>(stations);
        Collection<Arc<Station>> arcs = arcDAO.getAll();

        RouteDAO<Station> routeDAO = new RouteDAOImpl<>(stations, arcs);
        Collection<Route<Station>> routes = routeDAO.getAll();

        Collection<Route<Station>> circRoutes = routeDAO.getAllCircular(routes);
        Collection<RoutePair<Station>> simpleRoutes = routeDAO.getAllSimple(routes);

        TransportNetwork busNetwork = TransportNetwork.getInstance();

        for (Route<Station> route : circRoutes)
            busNetwork.registerCircularRoute(route);

        for (RoutePair<Station> pairRoute : simpleRoutes)
            busNetwork.registerSimpleRoute(pairRoute);

        return busNetwork;
    }
}
