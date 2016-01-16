package com.telesens.afanasiev.dao.impl.jaxb;

import com.telesens.afanasiev.dao.DAOException;
import com.telesens.afanasiev.dao.RouteDAO;
import com.telesens.afanasiev.dao.impl.jaxb.schemes.BusNetwork;
import com.telesens.afanasiev.model.identities.*;
import com.telesens.afanasiev.model.identities.impl.RouteImpl;
import com.telesens.afanasiev.model.helper.DaoUtils;
import com.telesens.afanasiev.model.identities.impl.RoutePairImpl;
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
    private Map<Long, Route<T>> routesMap;

    public RouteDAOImpl(Collection<T> nodes, Collection<Arc<T>> arcs) {
        loader = DAOLoaderData.getInstance();
        arcsMap = new HashMap<>();
        nodesMap = new HashMap<>();
        routesMap = new HashMap<>();
        fillMap(nodesMap, nodes);
        fillMap(arcsMap, arcs);
    }

    @Override
    public Route<T> getById(long id) {
        BusNetwork data = loader.getBusNetworkData();
        BusNetwork.Routes.Route routeData = parseById(data, id);

        if (routeData == null)
            throw new DAOException("Entity with specified ID wasn't found. Id = " + id);

        Route<T> route = createRoute(routeData);

        return route;
    }

    @Override
    public Collection<Route<T>> getAll() {
        BusNetwork data = loader.getBusNetworkData();
        Collection<BusNetwork.Routes.Route> routesData = parseAll(data);
        Collection<Route<T>> routes = new ArrayList<>();
        Route<T> route;

        for (BusNetwork.Routes.Route routeData : routesData) {
            route = createRoute(routeData);
            routes.add(route);
        }

        return routes;
    }

    @Override
    public Collection<Route<T>> getAllCircular(Collection<Route<T>> routes) {
        routesMap.clear();
        fillMap(routesMap, routes);
        BusNetwork data = loader.getBusNetworkData();
        Collection<BusNetwork.CircularRoutes.RouteLink>  routeLinksData =  parseAllCircular(data);
        Collection<Route<T>> routesCirc = new ArrayList<>();

        long id;
        for (BusNetwork.CircularRoutes.RouteLink routeLinkData : routeLinksData) {
            id = routeLinkData.getRouteId();

            if (!routesMap.containsKey(id))
                throw new DAOException("Entity with specified ID wasn't found. Id = " + id);

            routesCirc.add(routesMap.get(id));
        }

        return routesCirc;

    }

    @Override
    public Collection<RoutePair<T>> getAllSimple(Collection<Route<T>> routes) {
        routesMap.clear();
        fillMap(routesMap, routes);
        BusNetwork data = loader.getBusNetworkData();
        Collection<BusNetwork.SimpleRoutes.Pair>  routePairsData =  parseAllSimple(data);
        Collection<RoutePair<T>> routePairs = new ArrayList<>();

        long forwId;
        long backId;
        RoutePairImpl<T> routePair;
        for (BusNetwork.SimpleRoutes.Pair routePairData : routePairsData) {
            forwId = routePairData.getRouteForwardId();
            backId = routePairData.getRouteBackId();

            if (!routesMap.containsKey(forwId))
                throw new DAOException("Entity with specified ID wasn't found. Id = " + forwId);

            if (!routesMap.containsKey(backId))
                throw new DAOException("Entity with specified ID wasn't found. Id = " + backId);

            routePair = new RoutePairImpl<>(routesMap.get(forwId), routesMap.get(backId));

            routePairs.add(routePair);
        }

        return routePairs;
    }

    private <E extends Identity>void fillMap(Map<Long, E> map, Collection<E> identities) {
        long id;
        for (E identity : identities) {
            id = identity.getId();

            if (map.containsKey(id))
                throw new DAOException("Several entity with same ID. id = " + id);

            map.put(id, identity);
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

    private Collection<BusNetwork.CircularRoutes.RouteLink> parseAllCircular(BusNetwork data) {
        Collection<BusNetwork.CircularRoutes.RouteLink> routeLinks = new ArrayList<>();

        for (BusNetwork.CircularRoutes.RouteLink routeLink : data.getCircularRoutes().getRouteLink()) {
            routeLinks.add(routeLink);
        }

        return routeLinks;
    }

    private Collection<BusNetwork.SimpleRoutes.Pair> parseAllSimple(BusNetwork data) {
        Collection<BusNetwork.SimpleRoutes.Pair> routePairsData = new ArrayList<>();

        for (BusNetwork.SimpleRoutes.Pair routePair : data.getSimpleRoutes().getPair()) {
            routePairsData.add(routePair);
        }

        return routePairsData;
    }

    private Route<T> createRoute(BusNetwork.Routes.Route routeData) {
        long id = routeData.getId();
        String number = routeData.getNumber();
        String description = routeData.getDescription();
        Direct direct = routeData.getDirect().equals(Direct.FORWARD.name()) ? Direct.FORWARD : Direct.BACK;
        double cost = routeData.getCost();
        List<Arc<T>> arcList = new ArrayList<>();
        Arc<T> arc;

        if (!nodesMap.containsKey(routeData.getFirstNodeId()))
            throw new DAOException("Entity with specified ID wasn't found. Id = " + routeData.getFirstNodeId());

        T firstNode = nodesMap.get(routeData.getFirstNodeId());

        for (BusNetwork.Routes.Route.ArcsLink arcData : routeData.getArcsLink()) {
            if (!arcsMap.containsKey(arcData.getArcId()))
                throw new DAOException("Entity with specified ID wasn't found. Id = " + arcData.getArcId());

            arc = arcsMap.get(arcData.getArcId());
            arcList.add(arc);
        }

        Route<T> route = new RouteImpl<>(number, description, direct, cost, firstNode, arcList);
        DaoUtils.setPrivateId(route,  id);

        return route;
    }
}
