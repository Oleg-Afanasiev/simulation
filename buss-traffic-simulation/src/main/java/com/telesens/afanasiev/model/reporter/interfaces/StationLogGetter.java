package com.telesens.afanasiev.model.reporter.interfaces;

import com.telesens.afanasiev.model.reporter.unit.StationLogUnit;

/**
 * Created by oleg on 1/10/16.
 */
public interface StationLogGetter {
    boolean isEmptyStationLogsQueue();
    StationLogUnit pollStationLog();
    boolean isFinishCollect();
}
