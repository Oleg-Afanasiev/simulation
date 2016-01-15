package com.telesens.afanasiev.rules.impl;

import com.telesens.afanasiev.rules.PassengerTarget;
import com.telesens.afanasiev.simulation.Identity;
import com.telesens.afanasiev.simulation.impl.IdentityImpl;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by oleg on 1/14/16.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PassengerTargetImpl extends IdentityImpl implements PassengerTarget, Identity {
    private long stationId;
    private int factor;
}
