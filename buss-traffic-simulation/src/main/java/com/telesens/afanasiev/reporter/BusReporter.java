package com.telesens.afanasiev.reporter;

/**
 * Created by oleg on 1/8/16.
 */
public interface BusReporter {
    void sendBusLogArriveStation(String busNumber, long stationId, String stationName, int passInsideCount, int freeSeatsCount);
    void sendBusLogDoStop(String busNumber, long routeId, String routeName, long stationId, String stationName,
                          int getOffPassCount, int takeInPassCount, int passInsideCount, int freeSeatsCount);

    void sendBusLogRunProgress(String busNumber, String msg);
}
