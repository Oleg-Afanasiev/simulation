package com.telesens.afanasiev.model.reporter.interfaces;

/**
 * Created by oleg on 1/8/16.
 */
public interface RouteReporter {
    void sendRouteLog(String routeNumber, int busesRunCount, int busesWaitingCount);
}
