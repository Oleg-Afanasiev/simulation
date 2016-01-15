package com.telesens.afanasiev.model.Identities;

import com.telesens.afanasiev.model.rules.PassengerTargetSpreading;

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
