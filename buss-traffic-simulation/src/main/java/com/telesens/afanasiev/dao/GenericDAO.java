package com.telesens.afanasiev.dao;

import com.telesens.afanasiev.simulation.Identity;

import java.util.Collection;

/**
 * Created by oleg on 1/14/16.
 */
public interface GenericDAO<T extends Identity> {
    T getById(long id);
    Collection<T> getAll();
}
