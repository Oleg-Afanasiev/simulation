package com.telesens.afanasiev.rules;

import com.telesens.afanasiev.simulation.Identity;

/**
 * Created by oleg on 1/14/16.
 */
public interface PassengerTarget extends Identity {
    long getStationId();
    int getFactor();
}
