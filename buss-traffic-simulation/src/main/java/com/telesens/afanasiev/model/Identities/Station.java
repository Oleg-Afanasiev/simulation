package com.telesens.afanasiev.model.Identities;
import com.telesens.afanasiev.model.simulation.Observer;

import java.util.Collection;
import java.util.Date;

/**
 * Created by oleg on 1/14/16.
 */
public interface Station extends Identity, Observer {
    String getName();
    void setName(String name);
    Collection<Passenger> welcomeToBus(Bus bus, Route route, int getOffPassCount, Date actualTime);
    void addPassengers(Collection<Passenger> passengers);
}
