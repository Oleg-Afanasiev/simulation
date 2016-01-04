package com.telesens.afanasiev.simulator;

import com.telesens.afanasiev.reporter.ReportCollector;

import java.util.*;

/**
 * Created by oleg on 12/18/15.
 */
public class BusRun implements Observer{
    enum Phase  {
        runForward,
        breakForward,
        runBack,
        breakBack,
        completed;
    }

    private ReportCollector reportCollector;

    private Bus bus;
    private Route<Station> routeForward;
    private Route<Station> routeBack; // if routeBack == null => type of routeForward is CIRCULAR
    private Route<Station> currentRoute;

    private int breakForwardDuration;
    private int breakBackDuration;
    private Queue<Phase> phase;

    private int remainTimeToNextStation;
    private Iterator<Arc<Station>> arcIterator;
    private Station nextStation;

    public BusRun(Route<Station> routeForward, Route<Station> routeBack, Bus bus, int breakForwardDuration, int breakBackDuration) {
        this.bus = bus;
        this.breakForwardDuration = breakForwardDuration;
        this.breakBackDuration = breakBackDuration;
        this.routeForward = routeForward;
        this.routeBack = routeBack;


        reportCollector = ReportCollector.getInstance();
        phase = new ArrayDeque<>();
        phase.add(Phase.runForward);
        phase.add(Phase.breakForward);

        if (routeForward.getType() == TypeOfRoute.SIMPLE) {
            phase.add(Phase.runBack);
            phase.add(Phase.breakBack);
        }

        phase.add(Phase.completed);
    }

    public Bus getBus() {
        return bus;
    }

    public boolean isCompleted() {
        return phase.peek() == Phase.completed;
    }

    public void start(Date curTime) {
        currentRoute = routeForward;
        arcIterator = currentRoute.iterator();

        Station startStation = currentRoute.getFirstNode();
        reportCollector.sendMessage(bus.toString(), "<<<Начал рейс >>>");

        doStop(startStation);
    }

    public void tick(Date curTime) {
        remainTimeToNextStation--;

        if (remainTimeToNextStation == 0) {

            switch (phase.peek()) {
                case runForward:
                case runBack:
                    doStop(nextStation);
                    break;

                case breakForward:
                case breakBack:
                    finishBreak();
                    break;
            }
        }
    }

    private void doStop(Station station) {
        reportCollector.sendMessage(bus.toString(),
                String.format("Прибыл на остановку %s, кол-во пассажиров в салоне %s, свободных мест %d.",
                        station, bus.getCountPassengers(), bus.getFreeSeats()));

        int passDebussedCount = bus.debusPassengersOut(station);

        reportCollector.sendMessage(bus.toString(),
                String.format("Высадил пассажиров %d, осталось свободных мест %d.",
                        passDebussedCount, bus.getFreeSeats()));

        if (arcIterator.hasNext()) {
            Collection<Passenger> passengersEnteredIn = station.welcomeToBus(bus, currentRoute);
            bus.takePassengersIn(passengersEnteredIn);

            reportCollector.sendMessage(bus.toString(),
                    String.format("Принял пассажиров %d, осталось свободных мест %d.",
                            passengersEnteredIn.size(), bus.getFreeSeats()));

            Arc<Station> nextArc = arcIterator.next();
            remainTimeToNextStation = nextArc.getDuration();
            nextStation = nextArc.getOppositeNode(station);
            reportCollector.sendMessage(bus.toString(),
                    String.format("Начал движение к следующей остановке."));
        } else {
            changePhaseOfRun();
        }
    }

    private void finishBreak() {
        reportCollector.sendMessage(bus.toString(), "<<< Закончился перерыв >>> ");
        changePhaseOfRun();
    }

    private void changePhaseOfRun() {
        switch (phase.peek()) {
            case runForward:
                phase.poll();
                startBreakForward();
                break;

            case runBack:
                phase.poll();
                startBreakBack();
                break;

            case breakForward:
                phase.poll();
                if (routeForward.getType() == TypeOfRoute.SIMPLE)
                    startBack();
                else  //... == TypeOfRoute.CIRCULAR
                    finishRun();
                break;

            case breakBack:
                phase.poll();
                finishRun();
                break;
        }
    }

    private void startBreakForward() {
        remainTimeToNextStation = breakForwardDuration;
        reportCollector.sendMessage(bus.toString(), "<<< Встал на перерыв >>>");
    }

    private void startBreakBack() {
        remainTimeToNextStation = breakBackDuration;
        reportCollector.sendMessage(bus.toString(), "<<< Встал на перерыв >>>");
    }

    private void startBack() {
        currentRoute = routeBack;
        arcIterator = routeBack.iterator();
        nextStation = routeBack.getFirstNode();

        reportCollector.sendMessage(bus.toString(), "<<< Начал обратный рейс >>>");

        doStop(nextStation);
    }

    private void finishRun() {
        reportCollector.sendMessage(bus.toString(),
                String.format("<<< Завершил рейс >>>"));
    }

}
