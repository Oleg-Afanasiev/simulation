package com.afanasiev.telesens.simulate;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by oleg on 12/18/15.
 */
public class BusRun implements Observer{
    private ReportCollector reportCollector;
    private Bus bus;
    private Route<Station> routeForward;
    private Route<Station> routeBack;
    private int breakInterval;

    private int remainTimeToNextStation;
    private Iterator<Arc<Station>> arcIterator;
    private Station nextStation;

    public BusRun(Route<Station> routeForward, Route<Station> routeBack, Bus bus, int breakInterval) {
        this.bus = bus;
        this.breakInterval = breakInterval;
        this.routeForward = routeForward;
        this.routeBack = routeBack;

        reportCollector = ReportCollector.getInstance();
    }

    public void start(Date curTime) {
        arcIterator = routeForward.iterator();
        Arc<Station> firstArc = arcIterator.next();
        remainTimeToNextStation = firstArc.getDuration();
        Station startStation = routeForward.getFirstNode();
        reportCollector.sendMessage(bus.toString(), "Начал рейс.");

        doStop(startStation);
        nextStation = firstArc.getOppositeNode(startStation);
    }

    public void tick(Date curTime) {
        remainTimeToNextStation--;

        if (remainTimeToNextStation == 0) {
            doStop(nextStation);

            if (arcIterator.hasNext()) {
                Arc<Station> nextArc = arcIterator.next();
                remainTimeToNextStation = nextArc.getDuration();
                nextStation = nextArc.getOppositeNode(nextStation);
                reportCollector.sendMessage(bus.toString(),
                    String.format("Начал движение к следующей остановке."));
            } else {
                reportCollector.sendMessage(bus.toString(),
                    String.format("Завершил рейс."));
            }
        }
    }

    private void doStop(Station station) {
        reportCollector.sendMessage(bus.toString(),
                String.format("Прибыл на остановку %s, кол-во пассажиров в салоне %s, свободных мест %d.",
                        station, bus.getCountPassengers(), bus.getFreeSeats()));

        Collection<Passenger> leavingPassengers = station.welcomeToBus(bus, routeForward);
        bus.takePassengersIn(leavingPassengers);

        reportCollector.sendMessage(bus.toString(),
                String.format("Принял пассажиров %d, осталось свободных мест %d.",
                        leavingPassengers.size(), bus.getFreeSeats()));
    }
}
