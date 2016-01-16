package com.telesens.afanasiev.model.identities.impl;

import com.telesens.afanasiev.model.identities.*;
import com.telesens.afanasiev.model.reporter.interfaces.PassengerReporter;
import com.telesens.afanasiev.model.reporter.LogCollector;
import com.telesens.afanasiev.model.simulation.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * Created by oleg on 12/17/15.
 */
public class PassengerImpl extends IdentityImpl implements Passenger, Identity {

    private static final long serialVersionUID = 1L;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private long stationFromId;

    @Getter
    @Setter
    private long stationTargetId;

    @Getter
    @Setter
    private int limitTimeWaiting;

    private Date timeComeInBus;
    private Date timeGoOffBus;

    private transient TransportMap transportMap;
    private transient PassengerReporter reportCollector;

    public PassengerImpl() {
        reportCollector = LogCollector.getInstance();
        transportMap = TransportNetwork.getInstance();
    }
    public PassengerImpl(long stationFromId, long stationTargetId, int limitTimeWaiting) {
        this("", stationFromId, stationTargetId, limitTimeWaiting);
    }

    public PassengerImpl(String name, long stationFromId, long stationTargetId, int limitTimeWaiting) {
        this();
        this.name = name;
        this.stationFromId = stationFromId;
        this.stationTargetId = stationTargetId;
        this.limitTimeWaiting = limitTimeWaiting;
    }

    @Override
    public boolean isTarget(Station station) {
        return station.getId() == stationTargetId;
    }

    @Override
    public void delivered(Bus bus, Station station, Date actualTime) {
        timeGoOffBus = actualTime;
        reportCollector.sendPassLogDelivered(this.getId(), station.getId(), station.getName(), bus.getNumber(), timeComeInBus, timeGoOffBus);
    }

    @Override
    public void welcomeToStation(Station stationFrom) {
        reportCollector.sendPassLogWelcomeToStation(getId(), name, stationFrom.getId(), stationFrom.getName(),
                stationTargetId, transportMap.getStationById(stationTargetId).getName());
    }

    @Override
    public boolean welcomeToBus(Route route, String busNumber, Date actualTime) {
        timeComeInBus = actualTime;
        reportCollector.sendPassLogWelcomeToBus(getId(), stationFromId, busNumber);
        return true;
    }

    @Override
    public void tick(Date curTime) {
        limitTimeWaiting--;

//        if (limitTimeWaiting == 0)
//            reportCollector.sendLogMessage(this.toString(), String.format(
//                    "Не дождался автобуса"));
    }

    @Override
    public String toString() {
        return String.format("Passenger: %s [ID: %d]", name, getId());
    }
}
