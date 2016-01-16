package com.telesens.afanasiev.model.identities.impl;

import com.telesens.afanasiev.model.identities.Direct;
import com.telesens.afanasiev.model.identities.Identity;
import com.telesens.afanasiev.model.identities.Route;
import com.telesens.afanasiev.model.identities.RoutePair;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by oleg on 1/15/16.
 */
@NoArgsConstructor
@Data
public class RoutePairImpl<T extends Identity> extends IdentityImpl implements RoutePair<T>, Identity {

    private static final long serialVersionUID = 1L;

    private Route<T> backRoute;
    private Route<T> forwardRoute;

    public RoutePairImpl(Route<T> forwardRoute, Route<T>  backRoute) {
        if (forwardRoute.getDirect() != Direct.FORWARD || backRoute.getDirect() != Direct.BACK)
            throw new IllegalArgumentException("Incorrect route direct");

        this.forwardRoute = forwardRoute;
        this.backRoute = backRoute;
    }
}
