package com.telesens.afanasiev.model.identities;

import com.telesens.afanasiev.model.simulation.Observer;
import java.util.Date;

/**
 * Created by oleg on 1/15/16.
 */
public interface Passenger extends Identity, Observer {
    String getName();
    long getStationTargetId();
    int getLimitTimeWaiting();

    boolean isTarget(Station station);
    void delivered(Bus bus, Station station, Date actualTime);
    void welcomeToStation(Station stationFrom);
    boolean welcomeToBus(Route route, String busNumber, Date actualTime);
}
