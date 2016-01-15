package com.telesens.afanasiev.rules;

import com.telesens.afanasiev.rules.impl.PassengerTargetSpreading;
import com.telesens.afanasiev.simulation.Identity;

import java.util.Date;

/**
 * Created by oleg on 1/14/16.
 */
public interface PassengerGenerationRules extends Identity {
    void addRule(long stationId, Date timeFrom, int duration, int passCount, int limitWaiting, PassengerTargetSpreading passTargetSpreading);
    void addRule(long stationId, int duration, int passCount, int limitWaiting, PassengerTargetSpreading passTargetSpreading);
    void addRule(PassengerGenerationTask task);
    PassengerGenerationTask pollRuleForStation(long stationId);
    PassengerGenerationTask peekRuleForStation(long stationId);
}
