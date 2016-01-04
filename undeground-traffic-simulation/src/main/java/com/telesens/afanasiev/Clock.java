package com.telesens.afanasiev;

import java.util.Date;

/**
 * Created by oleg on 12/23/15.
 */
public class Clock {
    private Date actualTime;
    private final int ONE_MINUTE_IN_MILLIS = 60000; //milliseconds
    private final int TICK_STEP = 1 * ONE_MINUTE_IN_MILLIS;  // 1 minute

    public Clock(Date dateTimeStart) {
        actualTime = dateTimeStart;
    }

    public Date getActualTime() {
        return actualTime;
    }

    /**
     * Incrementing of actualTime always equals 1 minute
     */
    public void doTick() {
        actualTime = new Date(actualTime.getTime() + TICK_STEP);
    }
}
