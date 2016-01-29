package com.telesens.afanasiev.model.simulation;

import com.telesens.afanasiev.model.identities.*;
import com.telesens.afanasiev.model.identities.impl.BusImpl;
import com.telesens.afanasiev.model.helper.DaoUtils;
import com.telesens.afanasiev.model.reporter.LogCollector;
import com.telesens.afanasiev.model.reporter.interfaces.RouteReporter;

import java.util.*;

/**
 * Created by oleg on 12/10/15.
 */
public class RouteDispatcher implements Observer{

    private final int BUS_CAPACITY = 43; // bad idea (not here)

    private RouteReporter reportCollector;

    private Route<Station> routeForward;
    private Route<Station> routeBack;

    private List<Bus> waitingBuses;
    private List<BusRun> busesInRunning;
    private Date timeFrom;

    public RouteDispatcher(Route<Station> route) {
        init();
        registerCircularRoute(route);
    }

    public RouteDispatcher(Route<Station> routeForward, Route<Station> routeBack) {
        init();
        registerSimpleRoute(routeForward, routeBack);
    }

    private void init() {
        reportCollector = LogCollector.getInstance();
        waitingBuses = new ArrayList<>();
        busesInRunning = new ArrayList<>();
    }

    public void registerCircularRoute(Route<Station> route) {
        if (route.getType() != TypeOfRoute.CIRCULAR)
            throw new IllegalArgumentException("Incorrect type of route.");

        this.routeForward = route;
    }

    public void registerSimpleRoute(Route<Station> routeForward, Route<Station> routeBack) {
        this.routeForward = routeForward;
        this.routeBack = routeBack;
    }

    public long getRouteForwardId() {
        return routeForward.getId();
    }

    public void tick(Date curTime) {
        printReport();

        for (BusRun busRun : busesInRunning)
            busRun.tick(curTime);

        if (timeFrom == null)
            timeFrom = curTime;

        checkRunBuses();
    }

    private void printReport() {
        if (routeForward.getType() == TypeOfRoute.SIMPLE)
            reportCollector.sendRouteLog(String.format("%s %s - %s %s",
                            routeForward.getNumber(), routeForward.getDirect(), routeBack.getNumber(), routeBack.getDirect()),
                    busesInRunning.size(), waitingBuses.size());
        else
            reportCollector.sendRouteLog(String.format("%s %s",
                            routeForward.getNumber(), TypeOfRoute.CIRCULAR.toString()),
                    busesInRunning.size(), waitingBuses.size());

    }

    public void runNextBus(RunTask runTask) {
        if (waitingBuses.size() == 0) {
            Random random = new Random();
            Bus bus = new BusImpl(BUS_CAPACITY, "АX-" + (random.nextInt(8999) + 1000) + "-AA");

            DaoUtils.setPrivateId(bus, random.nextInt(1000000));

            waitingBuses.add(bus);
        }

        Bus bus = waitingBuses.get(0);
        waitingBuses.remove(0);

        BusRun busRun = new BusRun(routeForward, routeBack, bus, runTask.getBreakForwardDuration(), runTask.getBreakBackDuration());
        busesInRunning.add(busRun);

        busRun.start(runTask.getTimeStart());
    }

    private void checkRunBuses() {
        Iterator<BusRun> busRunIterator = busesInRunning.iterator();
        BusRun next;

        while (busRunIterator.hasNext()) {
            next = busRunIterator.next();
            if (next.isCompleted()) {
                busRunIterator.remove();
                waitingBuses.add(next.getBus());
            }
        }
    }

    @Override
    public String toString() {
        return "RouteDispatcher";
    }
}
