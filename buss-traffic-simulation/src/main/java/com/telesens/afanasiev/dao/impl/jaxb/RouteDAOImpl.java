package com.telesens.afanasiev.dao.impl.jaxb;

import com.telesens.afanasiev.dao.DAOException;
import com.telesens.afanasiev.dao.RouteDAO;
import com.telesens.afanasiev.dao.impl.jaxb.schemes.BusNetwork;
import com.telesens.afanasiev.helper.DaoUtils;
import com.telesens.afanasiev.simulation.Arc;
import com.telesens.afanasiev.simulation.Direct;
import com.telesens.afanasiev.simulation.Identity;
import com.telesens.afanasiev.simulation.Route;
import com.telesens.afanasiev.simulation.impl.RouteImpl;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.*;

/**
 * Created by oleg on 1/12/16.
 */
@AllArgsConstructor
@Getter
public class RouteDAOImpl<T extends Identity> implements RouteDAO <T> {

    private DAOLoaderData loader;
    private Map<Long, Arc<T>> arcsMap;
    private Map<Long, T> nodesMap;

    public RouteDAOImpl(Collection<T> nodes, Collection<Arc<T>> arcs) {
        loader = DAOLoaderData.getInstance();
        arcsMap = new HashMap<>();
        nodesMap = new HashMap<>();
        fillMaps(arcs, nodes);
    }

    @Override
    public Route<T> getById(long id) {
        BusNetwork data = loader.getBusNetwork();
        BusNetwork.Routes.Route routeData = parseById(data, id);

        if (routeData == null)
            throw new DAOException("Entity with specified ID wasn't found. Id = " + id);

        Route<T> route = createRoute(routeData);

        return route;
    }

    @Override
    public Collection<Route<T>> getAll() {
        BusNetwork data = loader.getBusNetwork();
        Collection<BusNetwork.Routes.Route> routesData = parseAll(data);
        Collection<Route<T>> routes = new ArrayList<>();
        Route<T> route;

        for (BusNetwork.Routes.Route routeData : routesData) {
            route = createRoute(routeData);
            routes.add(route);
        }

        return routes;
    }

    private void fillMaps(Collection<Arc<T>> arcs, Collection<T> nodes) {
        long id;
        for (Arc<T> arc : arcs) {
            id = arc.getId();

            if (arcsMap.containsKey(id))
                throw new DAOException("Several entity with same ID. id = " + id);

            arcsMap.put(id, arc);
        }

        for (T node : nodes) {
            id = node.getId();

            if (nodesMap.containsKey(id))
                throw new DAOException("Several entity with same ID. id = " + id);

            nodesMap.put(id, node);
        }
    }

    private BusNetwork.Routes.Route parseById(BusNetwork data, long id) {
        for (BusNetwork.Routes.Route routeData : data.getRoutes().getRoute()) {
            if (routeData.getId() == id)
                return routeData;
        }

        return null;
    }

    private Collection<BusNetwork.Routes.Route> parseAll(BusNetwork data) {
        Collection<BusNetwork.Routes.Route> routesData = new ArrayList<>();

        for (BusNetwork.Routes.Route routeData : data.getRoutes().getRoute()) {
            routesData.add(routeData);
        }

        return routesData;
    }

    private Route<T> createRoute(BusNetwork.Routes.Route routeData) {
        long id = routeData.getId();
        String number = routeData.getNumber();
        String description = routeData.getDescription();
        Direct direct = routeData.getDirect().equals(Direct.FORWARD.toString()) ? Direct.FORWARD : Direct.BACK;
        double cost = routeData.getCost();
        T firstNode = nodesMap.get(routeData.getFirstNodeId());
        List<Arc<T>> arcList = new ArrayList<>();
        Arc<T> arc;

        for (BusNetwork.Routes.Route.ArcsLink arcData : routeData.getArcsLink()) {
            arc = arcsMap.get(arcData.getArcId());
            arcList.add(arc);
        }

        Route<T> route = new RouteImpl<>(number, description, direct, cost, firstNode, arcList);

        try {
            DaoUtils.setPrivateField(route, "id", id);
        } catch(IllegalAccessException | NoSuchFieldException exc) {
            exc.printStackTrace();
        }

        return route;
    }
}
