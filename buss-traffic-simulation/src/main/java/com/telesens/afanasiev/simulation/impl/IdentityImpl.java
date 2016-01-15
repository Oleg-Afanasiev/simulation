package com.telesens.afanasiev.simulation.impl;

import com.telesens.afanasiev.simulation.Identity;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Created by oleg on 1/14/16.
 */
@NoArgsConstructor
public class IdentityImpl implements Identity {
    private static final long serialVersionUID = 1L;
    @Getter
    private Long id;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (o == null || getClass() != o.getClass())
            return false;

        Identity identity = (Identity)o;
        return id == identity.getId();
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
