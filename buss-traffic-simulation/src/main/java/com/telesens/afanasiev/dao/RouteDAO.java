package com.telesens.afanasiev.dao;

import com.telesens.afanasiev.model.identities.Identity;
import com.telesens.afanasiev.model.identities.Route;
import com.telesens.afanasiev.model.identities.RoutePair;

import java.util.Collection;

/**
 * Created by oleg on 1/12/16.
 */
public interface RouteDAO<T extends Identity> extends GenericDAO<Route<T>> {
    Collection<Route<T>> getAllCircular(Collection<Route<T>> routes);
    Collection<RoutePair<T>> getAllSimple(Collection<Route<T>> routes);
}
