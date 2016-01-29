package com.telesens.afanasiev.model.simulation;

import com.telesens.afanasiev.model.identities.*;
import com.telesens.afanasiev.model.reporter.interfaces.BusReporter;
import com.telesens.afanasiev.model.reporter.LogCollector;
import com.telesens.afanasiev.model.reporter.interfaces.RunReporter;

import lombok.Getter;

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

    private BusReporter logCollector;

    @Getter
    private Bus bus;
    private Route<Station> routeForward;
    private Route<Station> routeBack; // if routeBack == null => type of routeForward is CIRCULAR
    private Route<Station> currentRoute;

    private int breakForwardDuration;
    private int breakBackDuration;
    private Queue<Phase> phase;
    private Date timeStart;

    private int passDeliveredCount;

    private int remainTimeToNextStation;
    private Iterator<Arc<Station>> arcIterator;
    private Station nextStation;

    public BusRun(Route<Station> routeForward, Route<Station> routeBack, Bus bus, int breakForwardDuration, int breakBackDuration) {
        this.bus = bus;
        this.breakForwardDuration = breakForwardDuration;
        this.breakBackDuration = breakBackDuration;
        this.routeForward = routeForward;
        this.routeBack = routeBack;


        logCollector = LogCollector.getInstance();
        phase = new ArrayDeque<>();
        phase.add(Phase.runForward);
        phase.add(Phase.breakForward);

        if (routeForward.getType() == TypeOfRoute.SIMPLE) {
            phase.add(Phase.runBack);
            phase.add(Phase.breakBack);
        }

        phase.add(Phase.completed);
    }

    public boolean isCompleted() {
        return phase.peek() == Phase.completed;
    }

    public void start(Date actualTime) {
        timeStart = actualTime;
        passDeliveredCount = 0;
        currentRoute = routeForward;
        arcIterator = currentRoute.iterator();

        Station startStation = currentRoute.getFirstNode();
        logCollector.sendBusLogRunProgress(bus.getNumber(), "<<<Начал рейс >>>");

        doStop(startStation, actualTime);
    }

    public void tick(Date actualTime) {
        remainTimeToNextStation--;

        if (remainTimeToNextStation == 0) {

            switch (phase.peek()) {
                case runForward:
                case runBack:
                    doStop(nextStation, actualTime);
                    break;

                case breakForward:
                case breakBack:
                    finishBreak(actualTime);
                    break;
            }
        }
    }

    private void doStop(Station station, Date actualTime) {
        logCollector.sendBusLogArriveStation(bus.getNumber(), station.getId(), station.getName(),
                bus.getPassengersCount(), bus.getFreeSeatsCount());

        int getOffPassCount = bus.getOffPassengers(station, actualTime);
        passDeliveredCount += getOffPassCount;
        Collection<Passenger> takeInPassengers = new ArrayList<>();
        boolean hasNext = false;

        if (arcIterator.hasNext()) {
            hasNext = true;
            takeInPassengers = station.welcomeToBus(bus, currentRoute, getOffPassCount, actualTime);
            bus.takePassengersIn(takeInPassengers);

            Arc<Station> nextArc = arcIterator.next();
            remainTimeToNextStation = nextArc.getDuration();
            nextStation = nextArc.getOppositeNode(station);
        } else {
            changePhaseOfRun(actualTime);
        }

        logCollector.sendBusLogDoStop(bus.getNumber(), currentRoute.getId(), currentRoute.getNumber(), station.getId(), station.getName(),
                getOffPassCount, takeInPassengers.size(), bus.getPassengersCount(), bus.getFreeSeatsCount());

        if (hasNext)
            logCollector.sendBusLogRunProgress(bus.getNumber(), "Начал движение к следующей остановке");
    }

    private void finishBreak(Date actualTime) {
        logCollector.sendBusLogRunProgress(bus.getNumber(), "<<< Закончился перерыв >>> ");
        changePhaseOfRun(actualTime);
    }

    private void changePhaseOfRun(Date actualTime) {
        switch (phase.peek()) {
            case runForward:
                phase.poll();
                startBreakForward(actualTime);
                break;

            case runBack:
                phase.poll();
                startBreakBack(actualTime);
                break;

            case breakForward:
                phase.poll();
                if (routeForward.getType() == TypeOfRoute.SIMPLE)
                    startBack(actualTime);
                else  //... == TypeOfRoute.CIRCULAR
                    finishRun();
                break;

            case breakBack:
                phase.poll();
                finishRun();
                break;
        }
    }

    private void startBreakForward(Date timeFinish) {
        ((RunReporter)logCollector).sendRunLog(currentRoute.getId(), currentRoute.getNumber(), bus.getNumber(),
                timeStart, timeFinish, passDeliveredCount);

        remainTimeToNextStation = breakForwardDuration;
        logCollector.sendBusLogRunProgress(bus.getNumber(), "<<< Встал на перерыв >>>");
    }

    private void startBreakBack(Date timeFinish) {
        ((RunReporter)logCollector).sendRunLog(currentRoute.getId(), currentRoute.getNumber(), bus.getNumber(),
                timeStart, timeFinish, passDeliveredCount);
        remainTimeToNextStation = breakBackDuration;
        logCollector.sendBusLogRunProgress(bus.getNumber(), "<<< Встал на перерыв >>>");
    }

    private void startBack(Date actualTime) {
        timeStart = actualTime;
        passDeliveredCount = 0;
        currentRoute = routeBack;
        arcIterator = routeBack.iterator();
        nextStation = routeBack.getFirstNode();

        logCollector.sendBusLogRunProgress(bus.getNumber(), "<<< Начал обратный рейс >>>");

        doStop(nextStation, actualTime);
    }

    private void finishRun() {
        logCollector.sendBusLogRunProgress(bus.getNumber(), "<<< Завершил рейс >>>");
    }

}
