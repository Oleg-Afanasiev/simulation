package com.telesens.afanasiev.model.reporter.interfaces;

import com.telesens.afanasiev.model.reporter.unit.PassengerLogUnit;

/**
 * Created by oleg on 1/10/16.
 */
public interface PassengerLogGetter {
    boolean isEmptyPassLogsQueue();
    PassengerLogUnit pollPassLog();
    boolean isFinishCollect();
}
