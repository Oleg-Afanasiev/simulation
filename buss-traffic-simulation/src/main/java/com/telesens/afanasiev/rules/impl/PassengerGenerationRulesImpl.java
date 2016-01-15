package com.telesens.afanasiev.rules.impl;

import com.telesens.afanasiev.helper.DateTimeHelper;
import com.telesens.afanasiev.rules.PassengerGenerationRules;
import com.telesens.afanasiev.rules.PassengerGenerationTask;
import com.telesens.afanasiev.simulation.Identity;
import com.telesens.afanasiev.simulation.impl.IdentityImpl;
import lombok.Data;

import java.io.Serializable;
import java.util.*;

/**
 * Created by oleg on 1/5/16.
 */
@Data
public class PassengerGenerationRulesImpl extends IdentityImpl implements PassengerGenerationRules, Identity {
    private static final long serialVersionUID=1L;

    private Map<Long, Deque<PassengerGenerationTaskImpl>> queueGenerationRules;

    public PassengerGenerationRulesImpl() {
        queueGenerationRules = new HashMap<>();
    }

    @Override
    public void addRule(long stationId, Date timeFrom, int duration, int passCount, int limitWaiting, PassengerTargetSpreading passTargetSpreading)
        throws IllegalArgumentException {
        if (!queueGenerationRules.containsKey(stationId))
            queueGenerationRules.put(stationId, new ArrayDeque<>());
        else {
            Date timeFromPrev = queueGenerationRules.get(stationId).peekLast().getTimeFrom();
            int durationPrev = queueGenerationRules.get(stationId).peekLast().getDuration();

            if (DateTimeHelper.diffMinutes(timeFromPrev, timeFrom) != durationPrev)
                throw new IllegalArgumentException("Incorrect argument 'timeFrom'");
        }

        queueGenerationRules.get(stationId).add(new PassengerGenerationTaskImpl(stationId, timeFrom, duration, passCount, limitWaiting, passTargetSpreading));
    }

    @Override
    public void addRule(long stationId, int duration, int passCount, int limitWaiting, PassengerTargetSpreading passTargetSpreading)
            throws UnsupportedOperationException {
        if (!queueGenerationRules.containsKey(stationId))
            throw new UnsupportedOperationException("Cannot be apply this version 'addRule' to empty 'queueGenerationRules'");

        Date timeFromPrev = queueGenerationRules.get(stationId).peekLast().getTimeFrom();
        int durationPrev = queueGenerationRules.get(stationId).peekLast().getDuration();

        Date timeFrom = DateTimeHelper.incMinutes(timeFromPrev, durationPrev);
        addRule(stationId, timeFrom, duration, passCount, limitWaiting, passTargetSpreading);
    }

    @Override
    public void addRule(PassengerGenerationTask task) {
        addRule(task.getStationId(), task.getTimeFrom(), task.getDuration(), task.getPassCount(),
                task.getMinutesLimitWaiting(), task.getPassTargetSpreading());
    }

    @Override
    public PassengerGenerationTask peekRuleForStation(long stationId) {
        return queueGenerationRules.get(stationId).peek();
    }

    @Override
    public PassengerGenerationTask pollRuleForStation(long stationId) {
        return queueGenerationRules.get(stationId).poll();
    }
}
