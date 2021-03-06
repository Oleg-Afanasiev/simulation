package com.telesens.afanasiev.model.simulation;

import com.telesens.afanasiev.model.helper.DateTimeHelper;
import com.telesens.afanasiev.model.reporter.interfaces.ClockReporter;
import com.telesens.afanasiev.model.reporter.LogCollector;

import java.util.Date;

/**
 * Created by oleg on 12/4/15.
 */
public class Clock {
    private Date actualTime;
    private ClockReporter reportCollector;

    public Clock(Date dateTimeStart) {
        actualTime = DateTimeHelper.roundByMinutes(dateTimeStart);
        reportCollector = LogCollector.getInstance();
    }

    public Date getActualTime() {
        return actualTime;
    }

    /**
     * Step incrementing of time always equals 1 minute
     */
    public void doTick() {
        actualTime = DateTimeHelper.incMinute(actualTime);
        reportCollector.sendLogTick(actualTime);
    }
}
