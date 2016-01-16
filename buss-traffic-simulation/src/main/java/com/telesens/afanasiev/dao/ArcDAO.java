package com.telesens.afanasiev.dao;


import com.telesens.afanasiev.model.identities.Arc;
import com.telesens.afanasiev.model.identities.Identity;

/**
 * Created by oleg on 1/12/16.
 */
public interface ArcDAO<T extends Identity> extends GenericDAO<Arc<T>> {
}
