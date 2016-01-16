package com.telesens.afanasiev.model.identities.impl;

import com.telesens.afanasiev.model.identities.*;
import com.telesens.afanasiev.model.reporter.LogCollector;
import com.telesens.afanasiev.model.reporter.interfaces.StationReporter;

import com.telesens.afanasiev.model.simulation.Observer;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

/**
 * Created by oleg on 12/5/15.
 */
public class StationImpl extends IdentityImpl implements Station, Observer, Identity {
    private static final long serialVersionUID = 1L;

    private transient StationReporter logCollector;
    @Getter
    @Setter
    private String name;
    private transient LinkedList<Passenger> queueOfPassengers;

    public StationImpl() {
        logCollector = LogCollector.getInstance();
        queueOfPassengers = new LinkedList<>();
    }

    public StationImpl(String name) {
        this();
        this.name = name;
    }

    @Override
    public void addPassengers(Collection<Passenger> passengers) {

        for (Passenger passenger : passengers) {
            queueOfPassengers.addLast(passenger);
            passenger.welcomeToStation(this);
            logCollector.sendStationLogQueueSize(getId(), name, queueOfPassengers.size());
        }
    }

    @Override
    public Collection<Passenger> welcomeToBus(Bus bus, Route route, int getOffPassCount, Date actualTime) {

        Collection<Passenger> leavingPassengers = new ArrayList<>();

        for (int i = 0; i < bus.getFreeSeatsCount() && i < queueOfPassengers.size(); i++) {
            if (queueOfPassengers.get(i).welcomeToBus(route, bus.getNumber(), actualTime))
                leavingPassengers.add(queueOfPassengers.get(i));
        }

        queueOfPassengers.removeAll(leavingPassengers);
        logCollector.sendStationLogBusArrived(getId(), name, route.getId(), route.getNumber(), route.getDirect().toString(),
                bus.getNumber(), bus.getFreeSeatsCount(), getOffPassCount, leavingPassengers.size(), queueOfPassengers.size());

        return leavingPassengers;
    }

    @Override
    public void tick(Date curTime) {
        Iterator<Passenger> iterator = queueOfPassengers.iterator();
        Passenger nextPass;

        while(iterator.hasNext()) {
            nextPass = iterator.next();
            nextPass.tick(curTime);
            if (nextPass.getLimitTimeWaiting() == 0) {
                iterator.remove();
                logCollector.sendStationLogPassLeft(getId(), name, nextPass.getId(), queueOfPassengers.size());
            }
        }
    }

    @Override
    public String toString() {
        return String.format("\"%s\" [ID: %d]", name, getId());
    }
}
