package com.telesens.afanasiev.reporter;

import java.util.Date;

/**
 * Created by oleg on 1/8/16.
 */
public interface PassengerReporter {
    void sendPassLogDelivered(long passengerId, long stationId, String stationName, String busNumber, Date timeComeInBus, Date timeGoOffBus);
    void sendPassLogWelcomeToStation(long passengerId, String name, long stationFromId, String stationFromName, long stationTargetId, String stationTargetName);
    void sendPassLogWelcomeToBus(long passengerId, long stationId, String busNumber);
}
