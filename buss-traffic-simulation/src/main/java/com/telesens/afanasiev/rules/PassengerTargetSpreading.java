package com.telesens.afanasiev.rules;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by oleg on 1/5/16.
 */
public class PassengerTargetSpreading implements Serializable{
    private static final long serialVersionUID = 1L;
    private Map<Long, Integer> targetSpreading;
    private int sum;

    public PassengerTargetSpreading() {
        targetSpreading = new HashMap<>();
    }

    public Map<Long, Integer> getTargetSpreading() {
        return targetSpreading;
    }

    public void setTargetSpreading(Map<Long, Integer> targetSpreading) {
        this.targetSpreading = targetSpreading;
        updateSum();
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
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
