package com.telesens.afanasiev;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by oleg on 12/23/15.
 */
public class TrainTask implements Serializable, Comparable<TrainTask> {
    private static final long serialVersionUID = -1L;

    private Date timeStart;
    //private Route route;
    private Station startFromNode;

    public TrainTask() {

    }

    public TrainTask(Date timeStart, Station startFromNode) {
        this.timeStart = timeStart;
        this.startFromNode = startFromNode;
    }

    public void setTimeStart(Date timeStart) {
        this.timeStart = timeStart;
    }

    public Date getTimeStart() {
        return timeStart;
    }

//    public void setRoute(Route route) {
//        this.route = route;
//    }
//
//    public Route getRoute() {
//        return route;
//    }

    public void setStartFromNode(Station startFromNode) {
        this.startFromNode = startFromNode;
    }

    public Station getStartFromNode() {
        return startFromNode;
    }

    @Override
    public int compareTo(TrainTask o) {
        return this.getTimeStart().after(o.getTimeStart()) ? 1 : -1;
    }

    @Override
    public String toString() {
        return "[start time: " + timeStart + ", station: " + startFromNode + "]";
    }
}
