package com.telesens.afanasiev.reporter.interfaces;

import java.util.Date;

/**
 * Created by oleg on 1/8/16.
 */
public interface ClockReporter {
    void sendLogTick(Date actualTime);
}
