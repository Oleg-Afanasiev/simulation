package com.afanasiev.telesens.simulate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;

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
                String.format("Автобус № %s принял %d пассажиров, осталось на остановке %d",
                        bus.getNumber(), leavingPassengers.size(), queueOfPassengers.size()));

        return leavingPassengers;
    }

    public void tick(Date dateTime) {

    }

    @Override
    public String toString() {
        return String.format("\"%s\" [ID: %d]", name, ID);
    }
}
