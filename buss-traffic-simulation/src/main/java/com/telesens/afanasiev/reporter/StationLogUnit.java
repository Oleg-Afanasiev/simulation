package com.telesens.afanasiev.reporter;

import java.util.Date;

/**
 * Created by oleg on 1/8/16.
 */
public class StationLogUnit {
    private long stationId;
    private Date timeBusStop;
    private long busId;
    private long routeId;
    private String busNumber;
    private int takeInPassCount;
    private int getOffPassCount;
    private int stayPassCount;

    public StationLogUnit(long stationId, long busId, long routeId, Date timeBusStop, String busNumber,
                          int takeInPassCount, int getOffPassCount, int stayPassCount) {
        this.stationId = stationId;
        this.busId = busId;
        this.routeId = routeId;
        this.timeBusStop = timeBusStop;
        this.busNumber = busNumber;
        this.takeInPassCount = takeInPassCount;
        this.getOffPassCount = getOffPassCount;
        this.stayPassCount = stayPassCount;
    }

    public long getStationId() {
        return stationId;
    }

    public long getBusId() {
        return busId;
    }

    public long getRouteId() {
        return routeId;
    }

    public Date getTimeBusStop() {
        return timeBusStop;
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

    public int getStayPassCount() {
        return stayPassCount;
    }
}
