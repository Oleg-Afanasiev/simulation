package com.telesens.afanasiev.model.loader;

import com.telesens.afanasiev.dao.ArcDAO;
import com.telesens.afanasiev.dao.RouteDAO;
import com.telesens.afanasiev.dao.StationDAO;
import com.telesens.afanasiev.dao.impl.jaxb.ArcDAOImpl;
import com.telesens.afanasiev.dao.impl.jaxb.RouteDAOImpl;
import com.telesens.afanasiev.dao.impl.jaxb.StationDAOImpl;
import com.telesens.afanasiev.model.Identities.Arc;
import com.telesens.afanasiev.model.Identities.Route;
import com.telesens.afanasiev.model.Identities.Station;
import com.telesens.afanasiev.model.Identities.TransportNetwork;

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

        TransportNetwork busNetwork = TransportNetwork.getInstance();

        for (Route<Station> route : routes)
            busNetwork.addRoute(route);

        return busNetwork;
    }
}
