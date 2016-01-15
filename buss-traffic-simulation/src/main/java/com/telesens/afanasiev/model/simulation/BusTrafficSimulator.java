package com.telesens.afanasiev.model.simulation;

import com.telesens.afanasiev.model.Identities.Route;
import com.telesens.afanasiev.model.Identities.Station;
import com.telesens.afanasiev.model.Identities.TransportNetwork;
import com.telesens.afanasiev.model.helper.DateTimeHelper;
import com.telesens.afanasiev.model.rules.PassengerGenerationRules;
import com.telesens.afanasiev.model.rules.RunTimetable;
import com.telesens.afanasiev.model.reporter.LogCollector;
import com.telesens.afanasiev.model.reporter.interfaces.SimulatorReporter;

import java.util.*;

/**
 * Created by oleg on 12/9/15.
 */
public class BusTrafficSimulator implements Runnable {
    private Clock clock;
    private PassengerScheduler passengerScheduler;
    private Collection<RouteDispatcher> routesDispatcher;
    private TransportNetwork busNetwork;
    private RunTimetable runTimetable;

    private SimulatorReporter reportCollector;

    private Date timeFrom;
    private Date timeTo;

    public BusTrafficSimulator(TransportNetwork busNetwork, RunTimetable runTimetable, PassengerGenerationRules passGenerationRules,
                               Date timeFrom, Date timeTo)
            throws NoSuchFieldException, IllegalAccessException {

        this.busNetwork = busNetwork;
        reportCollector = LogCollector.getInstance();
        routesDispatcher = new ArrayList<>();
        passengerScheduler = new PassengerScheduler(passGenerationRules);
        this.runTimetable = runTimetable;
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;

        for (Route<Station> route : busNetwork.getAllRoutes())
            routesDispatcher.add(new RouteDispatcher(route));

        passengerScheduler.registerRoutes(busNetwork.getAllRoutes());
    }

    public void start() {

        clock = new Clock(timeFrom);
        Date actualTime = timeFrom;

        reportCollector.sendSimulatorLogStart(timeFrom);

        // The main loop of the simulation
        while (actualTime.before(timeTo)) {

            passengerScheduler.tick(actualTime);
            long routeId;
            Date timeStart;
            for (RouteDispatcher routeDispatcher : routesDispatcher) {
                routeDispatcher.tick(actualTime);
                routeId = routeDispatcher.getRouteForwardId();

                // in this loop we can start several buses at same time
                while (runTimetable.isExistTask(routeId)) {
                    timeStart = runTimetable.peekTask(routeId).getTimeStart();
                    if (DateTimeHelper.diffMinutes(timeStart, actualTime) != 0)
                        break;

                    routeDispatcher.runNextBus(runTimetable.pollTask(routeId));
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
    public void run() {
        start();
    }
}
