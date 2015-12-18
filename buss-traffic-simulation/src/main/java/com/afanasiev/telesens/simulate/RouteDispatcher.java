package com.afanasiev.telesens.simulate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by oleg on 12/10/15.
 */
public class RouteDispatcher implements Observer {

    private final int BUS_CAPACITY = 43; // bad idea (not here)

    private ReportCollector reportCollector;
    private int interval = 7; // min
    private Route routeForward;
    private Route routeBack;

    private int countBuses;
    private int breakInterval = 15; // min
    private List<Bus> freeBuses;
    private List<BusRun> busesInRunning;
    private Date timeFrom;

    public RouteDispatcher(Route route) {
        this(route, null);
    }

    public RouteDispatcher(Route routeForward, Route routeBack) {
        reportCollector = ReportCollector.getInstance();
        freeBuses = new ArrayList<>();
        busesInRunning = new ArrayList<>();

        registerSimpleRoute(routeForward, routeBack);
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
        reportCollector.sendMessage(this.toString(),
                String.format("Маршрут № %s %s - %s %s, кол-во автобусов: в рейсе %d, свободных: %d",
                        routeForward.getNumber(), routeForward.getDirect(), routeBack.getNumber(), routeBack.getDirect(),
                        busesInRunning.size(), freeBuses.size()));

        for (BusRun busRun : busesInRunning)
            busRun.tick(curTime);

        if (timeFrom == null)
            timeFrom = curTime;

        int diffMinutes = DateTimeRepresenter.diffMinutes(timeFrom, curTime);

        if (diffMinutes % interval == 0)
            runNextBus(curTime);
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

        BusRun busRun = new BusRun(routeForward, routeBack, bus, breakInterval);
        busesInRunning.add(busRun);

        busRun.start(curTime);
    }

    @Override
    public String toString() {
        return "RouteDispatcher";
    }
}
