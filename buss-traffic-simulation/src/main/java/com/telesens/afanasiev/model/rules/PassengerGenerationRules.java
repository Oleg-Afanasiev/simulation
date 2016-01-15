package com.telesens.afanasiev.model.rules;

import com.telesens.afanasiev.model.Identities.impl.PassengerGenerationTaskImpl;
import com.telesens.afanasiev.model.helper.DateTimeHelper;
import com.telesens.afanasiev.model.Identities.PassengerGenerationTask;

import lombok.Data;

import java.util.*;

/**
 * Created by oleg on 1/5/16.
 */
@Data
public class PassengerGenerationRules  {
    private static final long serialVersionUID=1L;

    private Map<Long, Deque<PassengerGenerationTaskImpl>> queueGenerationRules;

    public PassengerGenerationRules() {
        queueGenerationRules = new HashMap<>();
    }

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

    public void addRule(long stationId, int duration, int passCount, int limitWaiting, PassengerTargetSpreading passTargetSpreading)
            throws UnsupportedOperationException {
        if (!queueGenerationRules.containsKey(stationId))
            throw new UnsupportedOperationException("Cannot be apply this version 'addRule' to empty 'queueGenerationRules'");

        Date timeFromPrev = queueGenerationRules.get(stationId).peekLast().getTimeFrom();
        int durationPrev = queueGenerationRules.get(stationId).peekLast().getDuration();

        Date timeFrom = DateTimeHelper.incMinutes(timeFromPrev, durationPrev);
        addRule(stationId, timeFrom, duration, passCount, limitWaiting, passTargetSpreading);
    }

    public void addRule(PassengerGenerationTask task) {
        addRule(task.getStationId(), task.getTimeFrom(), task.getDuration(), task.getPassCount(),
                task.getMinutesLimitWaiting(), task.getPassTargetSpreading());
    }

    public PassengerGenerationTask peekRuleForStation(long stationId) {
        return queueGenerationRules.get(stationId).peek();
    }

    public PassengerGenerationTask pollRuleForStation(long stationId) {
        return queueGenerationRules.get(stationId).poll();
    }
}
