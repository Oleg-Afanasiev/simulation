package com.telesens.afanasiev.loader;

import com.telesens.afanasiev.dao.ArcDAO;
import com.telesens.afanasiev.dao.RouteDAO;
import com.telesens.afanasiev.dao.StationDAO;
import com.telesens.afanasiev.dao.impl.jaxb.ArcDAOImpl;
import com.telesens.afanasiev.dao.impl.jaxb.RouteDAOImpl;
import com.telesens.afanasiev.dao.impl.jaxb.StationDAOImpl;
import com.telesens.afanasiev.helper.DaoUtils;
import com.telesens.afanasiev.simulation.Arc;
import com.telesens.afanasiev.simulation.Route;
import com.telesens.afanasiev.simulation.Station;
import com.telesens.afanasiev.simulation.impl.ArcImpl;
import com.telesens.afanasiev.simulation.impl.RouteImpl;
import com.telesens.afanasiev.simulation.TransportNetwork;

import java.util.*;

/**
 * Created by oleg on 1/13/16.
 */
public class TransportNetworkLoader {

//    private Map<Long, Station> stationsMap = new HashMap<>();
//    private Map<Long, Arc<Station>> arcsMap = new HashMap<>();
//    private List<Route<Station>> routes = new ArrayList<>();

    public TransportNetwork loadBusNetwork() {

        StationDAO stationDAO = new StationDAOImpl();
        Collection<Station> stations = stationDAO.getAll();

        ArcDAO<Station> arcDAO = new ArcDAOImpl<>(stations);
        Collection<Arc<Station>> arcs = arcDAO.getAll();

        RouteDAO<Station> routeDAO = new RouteDAOImpl<Station>(stations, arcs);
        Collection<Route<Station>> routes = routeDAO.getAll();

        TransportNetwork busNetwork = TransportNetwork.getInstance();

        for (Route<Station> route : routes)
            busNetwork.addRoute(route);

        return busNetwork;
    }
//
//    private void createStations() throws DataLoader.NoValidLoadDataException {
//
//        StationDAO stationDAO = new StationDAOImpl();
//        Collection<Station> stations = stationDAO.getAll();
//        long id;
//
//        for (Station station : stations) {
//            id = station.getId();
//            if (stationsMap.containsKey(id))
//                throw new DataLoader.NoValidLoadDataException("No valid load data: several stationsMap have equals id");
//
//            stationsMap.put(id, station);
//        }
//    }
//
//    private void createArcs()
//            throws NoSuchFieldException, IllegalAccessException, DataLoader.NoValidLoadDataException {
//
//        long arcId;
//        long nodeLeftId;
//        long nodeRightId;
//        Arc<Station> arc;
//
//        ArcDA
//
//        for (ArcDAO<StationDAO> arcDAO : arcsDAO) {
//            arcId = arcDAO.getId();
//            nodeLeftId = arcDAO.getNodeLeft().getId();
//            nodeRightId = arcDAO.getNodeRight().getId();
//
//            if (!stationsMap.containsKey(nodeLeftId) || !stationsMap.containsKey(nodeRightId))
//                throw new DataLoader.NoValidLoadDataException("No valid load data: unknown station by id");
//
//            arc = new ArcImpl<>(stationsMap.get(nodeLeftId), stationsMap.get(nodeRightId), arcDAO.getDuration());
//            DaoUtils.setPrivateField(arc, "ID", arcId);
//
//            if (arcsMap.containsKey(arcId))
//                throw new DataLoader.NoValidLoadDataException("No valid load data: several arcsMap with same id");
//
//            arcsMap.put(arcId, arc);
//        }
//    }
//
//    private void createRoutes()
//            throws NoSuchFieldException, IllegalAccessException, DataLoader.NoValidLoadDataException {
//
//        Station firstStation;
//        long firstStationId;
//        long arcId;
//        List<Arc<Station>> routeArcs = new ArrayList<>();
//        Route<Station> route;
//
//        for (RouteDAO<StationDAO> routeDAO : routesDAO) {
//            firstStationId = routeDAO.getFirstNode().getId();
//
//            if (!stationsMap.containsKey(firstStationId))
//                throw new DataLoader.NoValidLoadDataException("No valid load data: unknown station by id");
//
//            firstStation = stationsMap.get(firstStationId);
//            for (ArcDAO<StationDAO> arcDAO : routeDAO.getArcs()) {
//                if (!arcsMap.containsKey(arcDAO.getId()))
//                    throw new DataLoader.NoValidLoadDataException("No valid load data: unknown arc by id");
//
//                arcId = arcDAO.getId();
//                routeArcs.add(arcsMap.get(arcId));
//            }
//
//            route = new RouteImpl<>(routeDAO.getNumber(), routeDAO.getDescription(), routeDAO.getDirect(), routeDAO.getCost(),
//                    firstStation, routeArcs);
//
//            DaoUtils.setPrivateField(route, "ID", routeDAO.getId());
//
//            routes.add(route);
//        }
//    }
}
