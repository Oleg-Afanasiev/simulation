package com.telesens.afanasiev.simulator;

import com.telesens.afanasiev.reporter.LogCollector;
import com.telesens.afanasiev.reporter.interfaces.StationReporter;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.*;

/**
 * Created by oleg on 12/5/15.
 */
public class Station implements Observer, Serializable {
    private static final long serialVersionUID = 1;

    private transient StationReporter logCollector;
    @Getter @Setter private  TransportMap transportNetWork;
    @Getter @Setter private long ID;
    @Getter @Setter private String name;
    private transient LinkedList<Passenger> queueOfPassengers;

    public Station() {
        logCollector = LogCollector.getInstance();
        queueOfPassengers = new LinkedList<>();
    }

    public Station(String name, TransportMap transportNetWork) {
        this.name = name;
        this.transportNetWork = transportNetWork;
        logCollector = LogCollector.getInstance();
        queueOfPassengers = new LinkedList<>();
    }

    public void addPassengers(Collection<Passenger> passengers) {

        for (Passenger passenger : passengers) {
            queueOfPassengers.addLast(passenger);
            passenger.welcomeToStation(this);
            logCollector.sendStationLogQueueSize(ID, name, queueOfPassengers.size());
        }
    }

    public Collection<Passenger> welcomeToBus(Bus bus, Route route, int getOffPassCount, Date actualTime) {

        Collection<Passenger> leavingPassengers = new ArrayList<>();

        for (int i = 0; i < bus.getFreeSeatsCount() && i < queueOfPassengers.size(); i++) {
            if (queueOfPassengers.get(i).welcomeToBus(route, bus.getNumber(), actualTime))
                leavingPassengers.add(queueOfPassengers.get(i));
        }

        queueOfPassengers.removeAll(leavingPassengers);
        logCollector.sendStationLogBusArrived(ID, name, route.getID(), route.getNumber(), route.getDirect().toString(),
                bus.getNumber(), bus.getFreeSeatsCount(), getOffPassCount, leavingPassengers.size(), queueOfPassengers.size());

        return leavingPassengers;
    }

    public void tick(Date curTime) {
        Iterator<Passenger> iterator = queueOfPassengers.iterator();
        Passenger nextPass;

        while(iterator.hasNext()) {
            nextPass = iterator.next();
            nextPass.tick(curTime);
            if (nextPass.getLimitTimeWaiting() == 0) {
                iterator.remove();
                logCollector.sendStationLogPassLeft(ID, name, nextPass.getID(), queueOfPassengers.size());
            }
        }
    }

    @Override
    public String toString() {
        return String.format("\"%s\" [ID: %d]", name, ID);
    }
}
