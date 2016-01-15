package com.telesens.afanasiev.dao.impl.jaxb;

import com.telesens.afanasiev.dao.DAOException;
import com.telesens.afanasiev.dao.StationDAO;
import com.telesens.afanasiev.dao.impl.jaxb.schemes.BusNetwork;
import com.telesens.afanasiev.model.Identities.impl.StationImpl;
import com.telesens.afanasiev.model.helper.DaoUtils;
import com.telesens.afanasiev.model.Identities.Station;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by oleg on 1/12/16.
 */
public class StationDAOImpl implements StationDAO {

    private DAOLoaderData loaderData;

    public StationDAOImpl() {
        loaderData = DAOLoaderData.getInstance();
    }

    @Override
    public Station getById(long id) {
        BusNetwork data = loaderData.getBusNetwork();
        BusNetwork.Stations.Station stationData = parseById(data, id);
        if (stationData == null)
            throw new DAOException("Entity with specified ID wasn't found. Id = " + id);

        Station station = createStation(stationData);

        return station;
    }

    @Override
    public Collection<Station> getAll() {
        BusNetwork data = loaderData.getBusNetwork();
        Collection<BusNetwork.Stations.Station> stationsData = parseAll(data);
        Collection<Station> stations = new ArrayList<>();
        Station station;

        for (BusNetwork.Stations.Station stationData : stationsData) {
            station = createStation(stationData);
            stations.add(station);
        }

        return stations;
    }

    private BusNetwork.Stations.Station parseById(BusNetwork data, long id) {
        for (BusNetwork.Stations.Station stationData : data.getStations().getStation()) {
            if (stationData.getId() == id)
                return stationData;
        }
        return null;
    }

    private Collection<BusNetwork.Stations.Station> parseAll(BusNetwork data) {
        Collection<BusNetwork.Stations.Station> stationsData = new ArrayList<>();
        for (BusNetwork.Stations.Station stationData : data.getStations().getStation()) {
            stationsData.add(stationData);
        }
        return stationsData;
    }

    private Station createStation(BusNetwork.Stations.Station stationData) {
        long id = stationData.getId();
        String name = stationData.getName();

        Station station = new StationImpl();
        station.setName(name);

        try {
            DaoUtils.setPrivateField(station, "id", id);
        } catch(IllegalAccessException | NoSuchFieldException exc) {
            exc.printStackTrace();
        }

        return station;
    }
}
