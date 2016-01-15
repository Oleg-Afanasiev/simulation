package com.telesens.afanasiev.model.reporter.interfaces;

/**
 * Created by oleg on 1/8/16.
 */
public interface StationReporter {
    void sendStationLogQueueSize(long stationId, String stationName, int queueSize);
    void sendStationLogBusArrived(long stationId, String stationName,
                                  long routeId, String routeNumber, String routeDirect, String busNumber,
                                  int freeSeats, int getOffPassCount, int takeInPassCount, int stayPassCount);

    void sendStationLogPassLeft(long stationId, String stationName, long passId, int queueSize);
}
