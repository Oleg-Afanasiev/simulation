package com.afanasiev.telesens.simulate;

import java.util.*;

/**
 * Created by oleg on 12/10/15.
 */
public class RouteDispatcher implements Observer{

    private final int BUS_CAPACITY = 43; // bad idea (not here)

    private ReportCollector reportCollector;
    private int runInterval = 17; // min
    private Route routeForward;
    private Route routeBack;

    private int countBuses;
    private int breakForwardInterval = 10; // min
    private int breakBackInterval = 10;
    private List<Bus> freeBuses;
    private List<BusRun> busesInRunning;
    private Date timeFrom;

    public RouteDispatcher(Route route) {
        init();
        registerCircularRoute(route);
    }

    public RouteDispatcher(Route routeForward, Route routeBack) {
        init();
        registerSimpleRoute(routeForward, routeBack);
    }

    private void init() {
        reportCollector = ReportCollector.getInstance();
        freeBuses = new ArrayList<>();
        busesInRunning = new ArrayList<>();
    }

    public void registerCircularRoute(Route route) {
        if (route.getType() != TypeOfRoute.CIRCULAR)
            throw new IllegalArgumentException("Incorrect type of route.");

        this.routeForward = route;
    }

    public void registerSimpleRoute(Route routeForward, Route routeBack) {
        if (routeForward.getFirstNode() != routeBack.getLastNode() ||
                routeForward.getLastNode() != routeBack.getFirstNode())
            throw new IllegalArgumentException("Incorrect pair of routes.");

        this.routeForward = routeForward;
        this.routeBack = routeBack;
    }

    public void tick(Date curTime) {
        printReport();

        for (BusRun busRun : busesInRunning)
            busRun.tick(curTime);

        if (timeFrom == null)
            timeFrom = curTime;

        checkRunBuses();

        int diffMinutes = DateTimeHelper.diffMinutes(timeFrom, curTime);

        if (diffMinutes % runInterval == 0)
            runNextBus(curTime);
    }

    private void printReport() {
        if (routeForward.getType() == TypeOfRoute.SIMPLE)
            reportCollector.sendMessage(this.toString(),
                    String.format("Маршрут № %s %s - %s %s, кол-во автобусов: в рейсе %d, свободных: %d",
                            routeForward.getNumber(), routeForward.getDirect(), routeBack.getNumber(), routeBack.getDirect(),
                            busesInRunning.size(), freeBuses.size()));
        else
            reportCollector.sendMessage(this.toString(),
                    String.format("Маршрут № %s %s, кол-во автобусов: в рейсе %d, свободных: %d",
                            routeForward.getNumber(), routeForward.getDirect(), busesInRunning.size(), freeBuses.size()));

    }

    private void runNextBus(Date curTime) {
        if (freeBuses.size() == 0) {
            Random random = new Random();
            Bus bus = new Bus(BUS_CAPACITY, "АX-" + (random.nextInt(8999) + 1000) + "-AA");
            try {
                DaoUtils.setPrivateField(bus, "ID", random.nextInt(1000000));
            } catch(NoSuchFieldException | IllegalAccessException exc) {
                exc.printStackTrace();
            }

            freeBuses.add(bus);
        }

        Bus bus = freeBuses.get(0);
        freeBuses.remove(0);

        BusRun busRun = new BusRun(routeForward, routeBack, bus, breakForwardInterval, breakBackInterval);
        busesInRunning.add(busRun);

        busRun.start(curTime);
    }

    private void checkRunBuses() {
        Iterator<BusRun> busRunIterator = busesInRunning.iterator();
        BusRun next;

        while (busRunIterator.hasNext()) {
            next = busRunIterator.next();
            if (next.isCompleted()) {
                busRunIterator.remove();
                freeBuses.add(next.getBus());
            }
        }
    }

    @Override
    public String toString() {
        return "RouteDispatcher";
    }
}
