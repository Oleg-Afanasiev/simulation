package com.telesens.afanasiev.simulator;

import com.telesens.afanasiev.reporter.PassengerReporter;
import com.telesens.afanasiev.reporter.LogCollector;

import java.util.Date;

/**
 * Created by oleg on 12/17/15.
 */
public class Passenger implements Observer {
    private long ID;
    private String name;
    private long stationFromId;
    private long stationTargetId;
    private PassengerReporter reportCollector;
    private Date timeComeInBus;
    private Date timeGoOffBus;
    private int limitTimeWaiting;
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

    public long getID() {
        return ID;
    }

    public int getLimitTimeWaiting() {
        return limitTimeWaiting;
    }

    public long getTargetId() {
        return stationTargetId;
    }

    public boolean isTarget(Station station) {
        return station.getID() == stationTargetId;
    }

    public void delivered(Bus bus, Station station, Date actualTime) {
        timeGoOffBus = actualTime;
        reportCollector.sendPassLogDelivered(this.ID, station.getID(), station.getName(), bus.getNumber(), timeComeInBus, timeGoOffBus);
    }

    public void welcomeToStation(Station stationFrom) {
        reportCollector.sendPassLogWelcomeToStation(ID, name, stationFrom.getID(), stationFrom.getName(),
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
