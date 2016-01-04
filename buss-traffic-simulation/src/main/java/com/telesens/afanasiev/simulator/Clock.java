package com.telesens.afanasiev.simulator;

import com.telesens.afanasiev.helper.DateTimeHelper;

import java.util.Date;

/**
 * Created by oleg on 12/4/15.
 */
public class Clock {
    private Date curTime;
//    private final int ONE_MINUTE_IN_MILLIS = 60000; //milliseconds
//    private final int TICK_STEP = 1 * ONE_MINUTE_IN_MILLIS;  // 1 minute

    public Clock(Date dateTimeStart) {
        curTime = DateTimeHelper.roundByMinutes(dateTimeStart);
    }

    public Date getCurTime() {
        return curTime;
    }

    /**
     * Incrementing of curTime always equals 1 minute
     */
    public void doTick() {
        curTime = DateTimeHelper.incMinute(curTime);
    }
}
