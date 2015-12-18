package com.afanasiev.telesens.simulate;

import java.util.Date;

/**
 * Created by oleg on 12/17/15.
 */
public class Passenger implements Observer {
    private long ID;
    private String name;
    private long stationFromID;
    private long stationToID;
    private ReportCollector reportCollector;

    public Passenger(long stationFromID, long stationToID) {
        this("", stationFromID, stationToID);
    }

    public Passenger(String name, long stationFromID, long stationToID) {
        this.name = name;
        this.stationFromID = stationFromID;
        this.stationToID = stationToID;
        reportCollector = ReportCollector.getInstance();
    }

    public boolean welcomeToBus(Route route) {
        return true;
    }

    public void tick(Date dateTime) {
        reportCollector.sendMessage(this.toString(), "tick");
    }

    public boolean isTarget(Station station) {
        return station.getID() == stationToID;
    }

    public void delivered(Station station) {
        reportCollector.sendMessage(this.toString(), "delivered to " + station.toString());
    }

    @Override
    public String toString() {
        return String.format("Passenger: %s [ID: %d]", name, ID);
    }
}
