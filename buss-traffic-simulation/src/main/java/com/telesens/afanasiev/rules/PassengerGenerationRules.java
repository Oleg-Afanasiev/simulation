package com.telesens.afanasiev.rules;

import com.telesens.afanasiev.helper.DateTimeHelper;

import java.io.Serializable;
import java.util.*;

/**
 * Created by oleg on 1/5/16.
 */
public class PassengerGenerationRules implements Serializable {
    private static final long serialVersionUID=1L;

    private Map<Long, Deque<PassengerGenerationUnit>> queueGenerationRules;

    public PassengerGenerationRules() {
        queueGenerationRules = new HashMap<>();
    }

    public Map<Long, Deque<PassengerGenerationUnit>> getQueueGenerationRules() {
        return queueGenerationRules;
    }

    public void setQueueGenerationRules(Map<Long, Deque<PassengerGenerationUnit>> queueGenerationRules) {
        this.queueGenerationRules = queueGenerationRules;
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

        queueGenerationRules.get(stationId).add(new PassengerGenerationUnit(stationId, timeFrom, duration, passCount, limitWaiting, passTargetSpreading));
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

    public PassengerGenerationUnit peekRuleForStation(long stationId) {
        return queueGenerationRules.get(stationId).peek();
    }

    public PassengerGenerationUnit pollRuleForStation(long stationId) {
        return queueGenerationRules.get(stationId).poll();
    }
}
