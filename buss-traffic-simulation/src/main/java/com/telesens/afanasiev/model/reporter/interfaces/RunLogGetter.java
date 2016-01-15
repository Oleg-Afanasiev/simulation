package com.telesens.afanasiev.model.reporter.interfaces;

import com.telesens.afanasiev.model.reporter.unit.RunLogUnit;

/**
 * Created by oleg on 1/10/16.
 */
public interface RunLogGetter {
    boolean isEmptyRunLogsQueue();
    RunLogUnit pollRunLog();
    boolean isFinishCollect();
}
