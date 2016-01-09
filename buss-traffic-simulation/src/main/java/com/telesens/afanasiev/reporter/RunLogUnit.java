package com.telesens.afanasiev.reporter;

import java.util.Date;

/**
 * Created by oleg on 1/9/16.
 */
public class RunLogUnit {
    private long routeId;
    private String routeNumber;
    private String busNumber;
    private Date timeStart;
    private Date timeFinish;
    private int passDelieveredCount;

    public RunLogUnit(long routeId, String routeNumber, String busNumber, Date timeStart, Date timeFinish, int passDelieveredCount) {
        this.routeId = routeId;
        this.routeNumber = routeNumber;
        this.busNumber = busNumber;
        this.timeStart = timeStart;
        this.timeFinish = timeFinish;
        this.passDelieveredCount = passDelieveredCount;
    }

    public long getRouteId() {
        return routeId;
    }

    public String getRouteNumber() {
        return routeNumber;
    }

    public String getBusNumber() {
        return busNumber;
    }

    public Date getTimeStart() {
        return timeStart;
    }

    public Date getTimeFinish() {
        return timeFinish;
    }

    public int getPassDelieveredCount() {
        return passDelieveredCount;
    }
}
