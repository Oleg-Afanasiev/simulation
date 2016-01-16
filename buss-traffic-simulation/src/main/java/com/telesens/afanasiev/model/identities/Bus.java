package com.telesens.afanasiev.model.identities;

import java.util.Collection;
import java.util.Date;

/**
 * Created by oleg on 1/15/16.
 */
public interface Bus extends Identity {
    int getCapacity();
    String getNumber();

    int getFreeSeatsCount();
    int getPassengersCount();
    void takePassengersIn(Collection<Passenger> satisfiedPassengers);
    int getOffPassengers(Station station, Date actualTime);
}
