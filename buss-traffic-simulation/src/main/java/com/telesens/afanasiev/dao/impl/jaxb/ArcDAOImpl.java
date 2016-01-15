package com.telesens.afanasiev.dao.impl.jaxb;

import com.telesens.afanasiev.dao.ArcDAO;
import com.telesens.afanasiev.dao.DAOException;
import com.telesens.afanasiev.dao.impl.jaxb.schemes.BusNetwork;
import com.telesens.afanasiev.helper.DaoUtils;
import com.telesens.afanasiev.simulation.Arc;
import com.telesens.afanasiev.simulation.Identity;
import com.telesens.afanasiev.simulation.impl.ArcImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by oleg on 1/12/16.
 */
public class ArcDAOImpl<T extends Identity> implements ArcDAO<T> {

    private DAOLoaderData loader;
    private Map<Long, T> nodeMap;

    public ArcDAOImpl(Collection<T> nodes) {
        loader = DAOLoaderData.getInstance();
        initStationMap(nodes);
    }

    @Override
    public Arc<T> getById(long id) {
        BusNetwork data = loader.getBusNetwork();
        BusNetwork.Arcs.Arc arcData = parseById(data, id);

        if (arcData == null)
            throw new DAOException("Entity with specified ID wasn't found. Id = " + id);

        Arc<T> arc = createArc(arcData);

        return arc;
    }

    @Override
    public Collection<Arc<T>> getAll() {
        BusNetwork data = loader.getBusNetwork();
        Collection<BusNetwork.Arcs.Arc> arcsData = parseAll(data);
        Collection<Arc<T>> arcs = new ArrayList<>();
        Arc<T> arc;

        for (BusNetwork.Arcs.Arc arcData : arcsData) {
            arc = createArc(arcData);
            arcs.add(arc);
        }

        return arcs;
    }

    private void initStationMap(Collection<T> nodes) {
        long id;
        nodeMap = new HashMap<>();
        for (T node : nodes) {
            id = node.getId();

            if (nodeMap.containsKey(id))
                throw new DAOException("Several stations have same ID. id = " + id);

            nodeMap.put(id, node);
        }
    }

    private BusNetwork.Arcs.Arc parseById(BusNetwork data, long id) {

        for (BusNetwork.Arcs.Arc arcData : data.getArcs().getArc()) {
            if (arcData.getId() == id)
                return arcData;
        }

        return null;
    }

    private Collection<BusNetwork.Arcs.Arc> parseAll(BusNetwork data) {
        Collection<BusNetwork.Arcs.Arc> arcsData = new ArrayList<>();

        for (BusNetwork.Arcs.Arc arcData : data.getArcs().getArc()) {
            arcsData.add(arcData);
        }

        return arcsData;
    }

    private Arc<T> createArc(BusNetwork.Arcs.Arc arcData) {
        long id = arcData.getId();
        T nodeLeft = nodeMap.get(arcData.getNodeLeftId());
        T nodeRight = nodeMap.get(arcData.getNodeRightId());
        int duration = arcData.getDuration();

        ArcImpl<T> arc = new ArcImpl<>();
        arc.setDuration(duration);
        arc.setNodeLeft(nodeLeft);
        arc.setNodeRight(nodeRight);

        try {
            DaoUtils.setPrivateField(arc, "id", id);
        } catch (IllegalAccessException | NoSuchFieldException exc) {
            exc.printStackTrace();
        }

        return arc;
    }
}
