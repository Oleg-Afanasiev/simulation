package com.telesens.afanasiev.model.reporter.interfaces;

import com.telesens.afanasiev.model.reporter.unit.BusLogUnit;

/**
 * Created by oleg on 1/10/16.
 */
public interface BusLogGetter {

    boolean isEmptyBusLogsQueue();
    BusLogUnit pollBusLog();
    boolean isFinishCollect();
}
