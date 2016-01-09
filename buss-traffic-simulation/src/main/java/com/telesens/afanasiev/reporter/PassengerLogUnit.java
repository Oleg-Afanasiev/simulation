package com.telesens.afanasiev.reporter;

import java.util.Date;

/**
 * Created by oleg on 1/9/16.
 */
public class PassengerLogUnit {
    private long passengerId;
    private long stationId;
    private String busNumber;
    private Date timeComeIn;
    private Date timeGoOut;

    public PassengerLogUnit(long passengerId, long stationId) {
        this.passengerId = passengerId;
        this.stationId = stationId;
    }

    public PassengerLogUnit(long passengerId, long stationId, String busNumber, Date timeComeIn, Date timeGoOut) {
        this.passengerId = passengerId;
        this.stationId = stationId;
        this.busNumber = busNumber;
        this.timeComeIn = timeComeIn;
        this.timeGoOut = timeGoOut;
    }

    public long getPassengerId() {
        return passengerId;
    }

    public long getStationId() {
        return stationId;
    }

    public String getBusNumber() {
        return busNumber;
    }

    public Date getTimeComeIn() {
        return timeComeIn;
    }

    public Date getTimeGoOut() {
        return timeGoOut;
    }

    public void setBusNumber(String busNumber) {
        this.busNumber = busNumber;
    }

    public void setTimeComeIn(Date timeComeIn) {
        this.timeComeIn = timeComeIn;
    }

    public void setTimeGoOut(Date timeGoOut) {
        this.timeGoOut = timeGoOut;
    }
}
