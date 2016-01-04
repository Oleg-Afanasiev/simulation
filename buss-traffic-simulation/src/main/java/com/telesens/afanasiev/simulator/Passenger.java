package com.telesens.afanasiev.simulator;

import com.telesens.afanasiev.helper.DateTimeHelper;
import com.telesens.afanasiev.reporter.ReportCollector;

import java.util.Date;

/**
 * Created by oleg on 12/17/15.
 */
public class Passenger implements Observer {
    private long ID;
    private String name;
    private long stationIdFrom;
    private long stationIdTo;
    private ReportCollector reportCollector;
    private int limitTimeExpection;

    public Passenger(long stationIdFrom, long stationIdTo, int limitTimeExpection) {
        this("", stationIdFrom, stationIdTo, limitTimeExpection);
    }

    public Passenger(String name, long stationIdFrom, long stationIdTo, int limitTimeExpection) {
        this.name = name;
        this.stationIdFrom = stationIdFrom;
        this.stationIdTo = stationIdTo;
        this.limitTimeExpection = limitTimeExpection;

        reportCollector = ReportCollector.getInstance();
    }

    public long getID() {
        return ID;
    }

    public int getLimitTimeExpection() {
        return limitTimeExpection;
    }

    public long getTargetId() {
        return stationIdTo;
    }

    public boolean isTarget(Station station) {
        return station.getID() == stationIdTo;
    }

    public void delivered(Station station) {
        reportCollector.sendMessage(this.toString(), "Вышел на остановке \"" + station.toString() + "\"");
    }

    public boolean welcomeToBus(Route route) {
        return true;
    }

    public void tick(Date curTime) {
        limitTimeExpection--;

        if (limitTimeExpection == 0)
            reportCollector.sendMessage(this.toString(), String.format(
                    "Не дождался автобуса"));
    }

    @Override
    public String toString() {
        return String.format("Passenger: %s [ID: %d]", name, ID);
    }
}
