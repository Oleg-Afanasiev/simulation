package com.telesens.afanasiev.model.simulation;

import com.telesens.afanasiev.model.identities.Station;

/**
 * Created by oleg on 12/20/15.
 */
public interface TransportMap {
    Station getStationById(long id);
}
