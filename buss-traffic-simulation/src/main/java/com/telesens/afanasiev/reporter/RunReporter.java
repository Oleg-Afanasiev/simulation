package com.telesens.afanasiev.reporter;

import java.util.Date;

/**
 * Created by oleg on 1/9/16.
 */
public interface RunReporter {
    void sendRunLog(long routeId, String routeNumber, String busNumber, Date timeStart, Date timeFinish, int passDeliveredCount);
}
