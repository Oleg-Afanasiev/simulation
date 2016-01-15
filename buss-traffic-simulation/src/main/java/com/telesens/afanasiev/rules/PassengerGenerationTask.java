package com.telesens.afanasiev.rules;

import com.telesens.afanasiev.rules.impl.PassengerTargetSpreading;
import com.telesens.afanasiev.simulation.Identity;
import com.telesens.afanasiev.simulation.Passenger;

import java.util.Collection;
import java.util.Date;

/**
 * Created by oleg on 1/14/16.
 */
public interface PassengerGenerationTask extends Identity{
    long getStationId();
    Date getTimeFrom();
    int getDuration();
    int getPassCount();
    int getMinutesLimitWaiting();
    PassengerTargetSpreading getPassTargetSpreading();

    boolean isActual(Date actualTime);
    Collection<Passenger> getPassengers(Date actualTime);
}
