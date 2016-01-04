package com.telesens.afanasiev;

import java.util.*;

/**
 * Created by oleg on 12/23/15.
 */
public class Train {
    private ReportCollector reportCollector;
    private long ID;
    private final int MAX_CAPACITY = 220;
    private int number;
    private Map<Long, Integer> movingPassengers;

    public Train(long id, int number) {
        this.ID = id;
        this.number = number;
        movingPassengers = new HashMap<>();
        reportCollector = ReportCollector.getInstance();
    }

    public long getID() {
        return ID;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getPassengersCount() {
        int sum = 0;

        for (Map.Entry<Long, Integer> entry : movingPassengers.entrySet())
            sum += entry.getValue();

        return sum;
    }

    public int getFreeSeatsCount() {
        return MAX_CAPACITY - getPassengersCount();
    }

    public void putPassengers(Map<Long, Integer> passengersAtStation) {
        int i = 0;
        int sum = 0;
        int prevCount;
        int freeSeatsCount;

        for (Map.Entry<Long, Integer> entry : passengersAtStation.entrySet()) {
            freeSeatsCount = getFreeSeatsCount();
            if (i + entry.getValue() <= freeSeatsCount) {
                sum += entry.getValue();
                prevCount = movingPassengers.get(entry.getKey()) == null ? 0 : movingPassengers.get(entry.getKey());
                movingPassengers.put(entry.getKey(), prevCount + entry.getValue());
                passengersAtStation.put(entry.getKey(), 0);
            }
            else {
                sum += freeSeatsCount - i;
                prevCount = movingPassengers.get(entry.getKey()) == null ? 0 : movingPassengers.get(entry.getKey());
                movingPassengers.put(entry.getKey(), prevCount +  freeSeatsCount - i);
                prevCount = passengersAtStation.get(entry.getKey()) == null ? 0 : passengersAtStation.get(entry.getKey());
                passengersAtStation.put(entry.getKey(), prevCount - freeSeatsCount + i);
                break;
            }
        }

        if (sum > 0)
            reportCollector.sendMessage(this.toString(),
                String.format("принял %d в поезд", sum));
    }

    public void debusPassengersOut(Long stationId) {
        int n = movingPassengers.get(stationId) == null ? 0 : movingPassengers.get(stationId);

        reportCollector.sendMessage(this.toString(),
                String.format("%d пассажиров вышли из поезда", n));

        movingPassengers.put(stationId, 0);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(String.format("train \"%d\" [ID = %d]", getNumber(), getID()));
        return sb.toString();
    }
}
