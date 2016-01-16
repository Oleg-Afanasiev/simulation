package com.telesens.afanasiev.model.identities.impl;

import com.telesens.afanasiev.model.identities.Bus;
import com.telesens.afanasiev.model.identities.Identity;
import com.telesens.afanasiev.model.identities.Passenger;
import com.telesens.afanasiev.model.identities.Station;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

/**
 * Created by oleg on 12/18/15.
 */
@Data
@NoArgsConstructor
public class BusImpl extends IdentityImpl implements Bus, Identity {

    private static final long serialVersionUID = 1L;

    private int capacity;
    private String number;

    private List<Passenger> passengers;

    public BusImpl(int capacity, String number) {
        if (capacity <= 0)
            throw new IllegalArgumentException("Incorrect capacity of the bus");

        this.capacity = capacity;
        this.number = number;

        passengers = new LinkedList<>();
    }

    @Override
    public int getFreeSeatsCount() {
        return capacity - passengers.size();
    }

    @Override
    public int getPassengersCount() {
        return passengers.size();
    }

    @Override
    public void takePassengersIn(Collection<Passenger> satisfiedPassengers) {
        passengers.addAll(satisfiedPassengers);
    }

    @Override
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
        return String.format("Автобус: %s [ID: %d]", number, getId());
    }
}
