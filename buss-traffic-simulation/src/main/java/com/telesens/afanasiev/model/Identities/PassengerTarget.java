package com.telesens.afanasiev.model.Identities;

import com.telesens.afanasiev.model.Identities.Identity;

/**
 * Created by oleg on 1/14/16.
 */
public interface PassengerTarget extends Identity {
    long getStationId();
    int getFactor();
}
