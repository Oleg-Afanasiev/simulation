package com.telesens.afanasiev.simulator;

import com.telesens.afanasiev.reporter.ReportCollector;

import java.util.*;

/**
 * Created by oleg on 12/5/15.
 */
public class Station implements Observer {
    private ReportCollector reportCollector;
    private TransportMap transportNetWork;
    private long ID;
    private String name;
    private LinkedList<Passenger> queueOfPassengers;

    public Station(String name, TransportMap transportNetWork) {
        this.name = name;
        this.transportNetWork = transportNetWork;
        reportCollector = ReportCollector.getInstance();
        queueOfPassengers = new LinkedList<>();
    }

    public long getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addPassengers(Collection<Passenger> passengers) {
        Station targetStation;

        for (Passenger passenger : passengers) {
            queueOfPassengers.addLast(passenger);
            targetStation = transportNetWork.getStationById(passenger.getTargetId());

            reportCollector.sendMessage(this.toString(),
                    String.format("Пассажир ID: %d, пришел на остановку \"%s\". Ему нужно на: \"%s\"",
                            passenger.getID(), this.getName(), targetStation.getName()));

            reportCollector.sendMessage(this.toString(),
                    String.format("Кол-во пассажиров на остановке %d", queueOfPassengers.size()));
        }
    }

    public Collection<Passenger> welcomeToBus(Bus bus, Route route) {
        reportCollector.sendMessage(this.toString(),
                String.format("Прибыл автобус № %s, маршрут № %s - %s, свободных мест %d",
                        bus.getNumber(), route.getNumber(), route.getDirect(), bus.getFreeSeats()));

        Collection<Passenger> leavingPassengers = new ArrayList<>();

        for (int i = 0; i < bus.getFreeSeats() && i < queueOfPassengers.size(); i++) {
            if (queueOfPassengers.get(i).welcomeToBus(route))
                leavingPassengers.add(queueOfPassengers.get(i));
        }

        queueOfPassengers.removeAll(leavingPassengers);

        reportCollector.sendMessage(this.toString(),
                String.format("В автобус № %s сели %d пассажиров, осталось на остановке %d",
                        bus.getNumber(), leavingPassengers.size(), queueOfPassengers.size()));

        return leavingPassengers;
    }

    public void tick(Date curTime) {
        Iterator<Passenger> iterator = queueOfPassengers.iterator();
        Passenger nextPass;

        while(iterator.hasNext()) {
            nextPass = iterator.next();
            nextPass.tick(curTime);
            if (nextPass.getLimitTimeExpection() == 0) {
                reportCollector.sendMessage(this.toString(),
                        String.format("Пассажир [ID = %d] покинул остановку", nextPass.getID()));
                iterator.remove();
            }
        }
    }

    @Override
    public String toString() {
        return String.format("\"%s\" [ID: %d]", name, ID);
    }
}
