package com.telesens.afanasiev.reporter.interfaces;

import com.telesens.afanasiev.reporter.unit.PassengerLogUnit;

/**
 * Created by oleg on 1/10/16.
 */
public interface PassengerLogGetter {
    boolean isEmptyPassLogsQueue();
    PassengerLogUnit pollPassLog();
    boolean isFinishCollect();
}
