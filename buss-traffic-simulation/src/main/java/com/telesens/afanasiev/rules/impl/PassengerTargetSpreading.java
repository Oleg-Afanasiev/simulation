package com.telesens.afanasiev.rules.impl;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by oleg on 1/5/16.
 */
@Data
public class PassengerTargetSpreading implements Serializable{
    private static final long serialVersionUID = 1L;
    private Map<Long, Integer> targetSpreading;
    private int sum;

    public PassengerTargetSpreading() {
        targetSpreading = new HashMap<>();
    }

    public void addTarget(long stationTargetId, int factor) {
        targetSpreading.put(stationTargetId, factor);
        updateSum();
    }

    public long generateTargetId() {
        int i = new Random().nextInt(sum);
        int curSum = 0;

        for (Map.Entry<Long, Integer> entry : targetSpreading.entrySet())
            if (i < curSum + entry.getValue())
                return entry.getKey();
            else
                curSum += entry.getValue();

        return 0;
    }

    private void updateSum() {
        sum = 0;
        for (Map.Entry<Long, Integer> entry : targetSpreading.entrySet()) {
            sum += entry.getValue();
        }
    }
}
