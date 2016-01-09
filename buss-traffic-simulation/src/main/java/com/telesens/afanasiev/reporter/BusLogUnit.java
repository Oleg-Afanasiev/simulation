package com.telesens.afanasiev.reporter;

import java.util.Date;

/**
 * Created by oleg on 1/8/16.
 */
public class BusLogUnit {
    private String busNumber;
    private Date timeStop;
    private long stationId;
    private long routeId;
    private int takeInPassCount;
    private int getOffPassCount;
    private int insidePassCount;
    private int busCapacity;

    public BusLogUnit(Date timeStop, long routeId, long stationId,  String busNumber, int busCapacity,
                      int getOffPassCount, int takeInPassCount, int insidePassCount) {
        this.timeStop = timeStop;
        this.stationId = stationId;
        this.routeId = routeId;
        this.busNumber = busNumber;
        this.takeInPassCount = takeInPassCount;
        this.getOffPassCount = getOffPassCount;
        this.insidePassCount = insidePassCount;
        this.busCapacity = busCapacity;
    }

    public Date getTimeStop() {
        return timeStop;
    }

    public long getStationId() {
        return stationId;
    }

    public long getRouteId() {
        return routeId;
    }

    public String getBusNumber() {
        return busNumber;
    }

    public int getTakeInPassCount() {
        return takeInPassCount;
    }

    public int getGetOffPassCount() {
        return getOffPassCount;
    }

    public int getInsidePassCount() {
        return insidePassCount;
    }

    public int getBusCapacity() {
        return busCapacity;
    }
}
