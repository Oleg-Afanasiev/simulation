package com.telesens.afanasiev.simulator;

import lombok.Getter;

import java.util.*;

/**
 * Created by oleg on 12/18/15.
 */
public class Bus {
    private long ID;
    @Getter private final int CAPACITY;

    @Getter private String number;
    private List<Passenger> passengers;

    public Bus(int capacity, String number) {
        if (capacity <= 0)
            throw new IllegalArgumentException("Incorrect capacity of the bus");

        this.CAPACITY = capacity;
        this.number = number;

        passengers = new LinkedList<>();
    }

    public int getFreeSeatsCount() {
        return CAPACITY - passengers.size();
    }

    public int getPassengersCount() {
        return passengers.size();
    }

    public void takePassengersIn(Collection<Passenger> satisfiedPassengers) {
        passengers.addAll(satisfiedPassengers);
    }

    public int getOffPassengers(Station station, Date actualTime) {
        Iterator<Passenger> iterator = passengers.iterator();
        Passenger passenger;
        int passCount = 0;

        while (iterator.hasNext()) {
            passenger = iterator.next();
            if (passenger.isTarget(station)) {
                passenger.delivered(this, station, actualTime);
                iterator.remove();
                passCount++;
            }
        }

        return passCount;
    }

    @Override
    public String toString() {
        return String.format("Автобус: %s [ID: %d]", number, ID);
    }
}
