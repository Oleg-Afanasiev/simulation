package com.afanasiev.telesens.simulate;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by oleg on 12/18/15.
 */
public class Bus {
    private long ID;
    private int CAPACITY;

    private String number;
    private List<Passenger> passengers;

    public Bus(int capacity, String number) {
        if (capacity <= 0)
            throw new IllegalArgumentException("Incorrect capacity of the bus");

        this.CAPACITY = capacity;
        this.number = number;

        passengers = new LinkedList<>();
    }

    public String getNumber() {
        return number;
    }

    public int getFreeSeats() {
        return CAPACITY - passengers.size();
    }

    public int getCountPassengers() {
        return passengers.size();
    }

    public void takePassengersIn(Collection<Passenger> satisfiedPassengers) {
        passengers.addAll(satisfiedPassengers);
    }

    public void debusPassengerOut(Station station) {
        for (Passenger passenger : passengers) {
            if (passenger.isTarget(station)) {
                passengers.remove(passenger);
                passenger.delivered(station);
            }
        }
    }

    @Override
    public String toString() {
        return String.format("Автобус: %s [ID: %d]", number, ID);
    }
}