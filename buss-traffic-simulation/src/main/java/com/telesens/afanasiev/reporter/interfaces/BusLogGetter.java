package com.telesens.afanasiev.reporter.interfaces;

import com.telesens.afanasiev.reporter.unit.BusLogUnit;

/**
 * Created by oleg on 1/10/16.
 */
public interface BusLogGetter {

    boolean isEmptyBusLogsQueue();
    BusLogUnit pollBusLog();
    boolean isFinishCollect();
}
