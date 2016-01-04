package com.telesens.afanasiev;

import java.io.Serializable;
import java.util.*;

/**
 * Created by oleg on 12/23/15.
 */
public class Station implements Serializable {
    private static final long serialVersionUID = -1L;
    private long ID;
    private String name;
    private int passengersCountInHour;
    private Map<Long, Integer> queuePassForward;
    private Map<Long, Integer> queuePassBack;

    public Station() {
        queuePassForward = new HashMap<>();
        queuePassBack = new HashMap<>();
    }

    public Station (long id, String name, int nPassengersHour) {
        this.ID = id;
        this.name = name;
        this.passengersCountInHour = nPassengersHour;

        queuePassForward = new HashMap<>();
        queuePassBack = new HashMap<>();
    }

    public void initTargetStations(boolean forward, Long... listTargetStationsID) {
        if (forward)
            for (int i = 0; i < listTargetStationsID.length; i++)
                queuePassForward.put(listTargetStationsID[i], 0);
        else
            for (int i = 0; i < listTargetStationsID.length; i++)
                queuePassBack.put(listTargetStationsID[i], 0);
    }

    public long getID() {
        return ID;
    }

    public void setID(long id) {
        ID = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPassengersCountInHour() {
        return passengersCountInHour;
    }

    public void setPassengersCountInHour(int passengersCountInHour) {
        this.passengersCountInHour = passengersCountInHour;
    }

    public int passengersCount(boolean forward) {
        Map<Long, Integer> queue = forward ? queuePassForward : queuePassBack;
        int sum = 0;

        for (Map.Entry<Long, Integer> entry : queue.entrySet())
            sum += entry.getValue();

        return sum;
    }

    public void arrivePassengers(long targetStationID, int passangersCount) {
        int prevCount;

        if (queuePassForward.containsKey(targetStationID)) {
            prevCount = queuePassForward.get(targetStationID);
            queuePassForward.put(targetStationID, prevCount + passangersCount);
        }
        else if (queuePassBack.containsKey(targetStationID)) {
            prevCount = queuePassBack.get(targetStationID);
            queuePassBack.put(targetStationID, prevCount + passangersCount);
        }
    }

    public void arriveTrain(Train train, boolean forward) {
        if (forward)
            train.putPassengers(queuePassForward);
        else
            train.putPassengers(queuePassBack);
    }

    @Override
    public boolean equals(Object other) {
        return this.ID == ((Station)other).ID;
    }

    @Override
    public String toString() {
        return getName();
    }
}
