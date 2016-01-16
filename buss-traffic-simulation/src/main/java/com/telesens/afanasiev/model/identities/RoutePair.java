package com.telesens.afanasiev.model.identities;

/**
 * Created by oleg on 1/15/16.
 */
public interface RoutePair<T extends Identity> extends Identity {
    Route<T> getBackRoute();
    Route<T> getForwardRoute();
}
