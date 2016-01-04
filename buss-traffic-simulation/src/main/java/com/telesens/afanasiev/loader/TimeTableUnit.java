package com.telesens.afanasiev.loader;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by oleg on 1/1/16.
 */
public class TimeTableUnit implements Serializable, Comparable<TimeTableUnit> {
    private static final long serialVersionUID = 1L;

    private long idRoute;
    private Date timeStart;
    private int breakForwardDuration;
    private int breakBackDuration;

    public TimeTableUnit() {}

    public TimeTableUnit(long idRoute, Date timeStart, int breakForwardDuration, int breakBackDuration) {
        this.idRoute = idRoute;
        this.timeStart = timeStart;
        this.breakForwardDuration = breakForwardDuration;
        this.breakBackDuration = breakBackDuration;
    }

    public long getIdRoute() {
        return idRoute;
    }

    public void setIdRoute(long idRoute) {
        this.idRoute = idRoute;
    }

    public Date getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(Date timeStart) {
        this.timeStart = timeStart;
    }

    public int getBreakForwardDuration() {
        return breakForwardDuration;
    }

    public void setBreakForwardDuration(int breakForwardDuration) {
        this.breakForwardDuration = breakForwardDuration;
    }

    public int getBreakBackDuration() {
        return breakBackDuration;
    }

    public void setBreakBackDuration(int breakBackDuration) {
        this.breakBackDuration = breakBackDuration;
    }

    @Override
    public int compareTo(TimeTableUnit o) {
        return Long.compare(this.timeStart.getTime(), o.timeStart.getTime());
    }
}
