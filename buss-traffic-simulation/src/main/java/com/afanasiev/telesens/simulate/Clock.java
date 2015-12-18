package com.afanasiev.telesens.simulate;

import java.util.Date;

/**
 * Created by oleg on 12/4/15.
 */
public class Clock {
    private Date dateTime;
    private final int ONE_MINUTE_IN_MILLIS = 60000; //milliseconds
    private final int TICK_STEP = 1 * ONE_MINUTE_IN_MILLIS;  // 1 minute

    public Clock(Date dateTimeStart) {
        dateTime = dateTimeStart;
    }

    public Date getDateTime() {
        return dateTime;
    }

    /**
     * Incrementing of dateTime always equals 1 minute
     */
    public void doTick() {
        dateTime = new Date(dateTime.getTime() + TICK_STEP);
    }
}
