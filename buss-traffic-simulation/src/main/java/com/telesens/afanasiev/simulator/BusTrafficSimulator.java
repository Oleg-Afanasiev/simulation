package com.telesens.afanasiev.simulator;

import com.telesens.afanasiev.helper.DateTimeHelper;
import com.telesens.afanasiev.loader.TimeTable;
import com.telesens.afanasiev.reporter.LogCollector;
import com.telesens.afanasiev.reporter.SimulatorReporter;
import com.telesens.afanasiev.rules.PassengerGenerationRules;

import java.util.*;

/**
 * Created by oleg on 12/9/15.
 */
public class BusTrafficSimulator implements Runnable {
    private Clock clock;
    private PassengerSheduler passengerSheduler;
    private Collection<RouteDispatcher> routesDispatcher;
    private TransportNetwork busNetwork;
    private TimeTable timeTable;

    private SimulatorReporter reportCollector;

    private Date timeFrom;
    private Date timeTo;

    public BusTrafficSimulator(TransportNetwork busNetwork, TimeTable timeTable, PassengerGenerationRules passGenerationRules,
                               Date timeFrom, Date timeTo)
            throws NoSuchFieldException, IllegalAccessException {

        this.busNetwork = busNetwork;
        reportCollector = LogCollector.getInstance();
        routesDispatcher = new ArrayList<>();
        passengerSheduler = new PassengerSheduler(passGenerationRules);
        this.timeTable = timeTable;
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;

        for (Route<Station> route : busNetwork.getAllRoutes())
            routesDispatcher.add(new RouteDispatcher(route));

        passengerSheduler.registerRoutes(busNetwork.getAllRoutes());
    }

    public void start() {

        clock = new Clock(timeFrom);
        Date actualTime = timeFrom;

        reportCollector.sendSimulatorLogStart(timeFrom);

        // The main loop of the simulation
        while (actualTime.before(timeTo)) {

            passengerSheduler.tick(actualTime);
            long routeId;
            Date timeStart;
            for (RouteDispatcher routeDispatcher : routesDispatcher) {
                routeDispatcher.tick(actualTime);
                routeId = routeDispatcher.getRouteForwardId();

                // in this loop we can start several buses at same time
                while (timeTable.isExistTask(routeId)) {
                    timeStart = timeTable.peekTask(routeId).getTimeStart();
                    if (DateTimeHelper.diffMinutes(timeStart, actualTime) != 0)
                        break;

                    routeDispatcher.runNextBus(timeTable.pollTask(routeId));
                }
            }

            busNetwork.tick(actualTime);
            clock.doTick();
            actualTime = clock.getActualTime();
        } // while
        reportCollector.sendSimulatorLogFinish();
    }

    public String allRoutesToString() {
        return busNetwork.allRoutesToString();
    }

    @Override
    public String toString() {
        return "";
    }

    @Override
    public void run() {
        start();
    }
}
