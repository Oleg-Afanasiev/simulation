package com.telesens.afanasiev.model.reporter.interfaces;

import java.util.Date;

/**
 * Created by oleg on 1/8/16.
 */
public interface SimulatorReporter {
    void sendSimulatorLogStart(Date startTime);
    void sendSimulatorLogFinish();
}
