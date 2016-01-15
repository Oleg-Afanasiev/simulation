package com.telesens.afanasiev.model.Identities;

import com.telesens.afanasiev.model.reporter.interfaces.PassengerReporter;
import com.telesens.afanasiev.model.reporter.LogCollector;
import com.telesens.afanasiev.model.simulation.*;
import lombok.Getter;

import java.util.Date;

/**
 * Created by oleg on 12/17/15.
 */
public class Passenger implements Observer {
    @Getter private long ID;
    private String name;
    private long stationFromId;
    @Getter private long stationTargetId;
    private PassengerReporter reportCollector;
    private Date timeComeInBus;
    private Date timeGoOffBus;
    @Getter private int limitTimeWaiting;
    private TransportMap transportMap;

    public Passenger(long stationFromId, long stationTargetId, int limitTimeWaiting) {
        this("", stationFromId, stationTargetId, limitTimeWaiting);
    }

    public Passenger(String name, long stationFromId, long stationTargetId, int limitTimeWaiting) {
        this.name = name;
        this.stationFromId = stationFromId;
        this.stationTargetId = stationTargetId;
        this.limitTimeWaiting = limitTimeWaiting;

        reportCollector = LogCollector.getInstance();
        transportMap = TransportNetwork.getInstance();
    }

    public boolean isTarget(Station station) {
        return station.getId() == stationTargetId;
    }

    public void delivered(Bus bus, Station station, Date actualTime) {
        timeGoOffBus = actualTime;
        reportCollector.sendPassLogDelivered(this.ID, station.getId(), station.getName(), bus.getNumber(), timeComeInBus, timeGoOffBus);
    }

    public void welcomeToStation(Station stationFrom) {
        reportCollector.sendPassLogWelcomeToStation(ID, name, stationFrom.getId(), stationFrom.getName(),
                stationTargetId, transportMap.getStationById(stationTargetId).getName());
    }

    public boolean welcomeToBus(Route route, String busNumber, Date actualTime) {
        timeComeInBus = actualTime;
        reportCollector.sendPassLogWelcomeToBus(ID, stationFromId, busNumber);
        return true;
    }

    public void tick(Date curTime) {
        limitTimeWaiting--;

//        if (limitTimeWaiting == 0)
//            reportCollector.sendLogMessage(this.toString(), String.format(
//                    "Не дождался автобуса"));
    }

    @Override
    public String toString() {
        return String.format("Passenger: %s [ID: %d]", name, ID);
    }
}