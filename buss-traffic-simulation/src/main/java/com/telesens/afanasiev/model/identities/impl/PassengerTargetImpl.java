package com.telesens.afanasiev.model.identities.impl;

import com.telesens.afanasiev.model.identities.PassengerTarget;
import com.telesens.afanasiev.model.identities.Identity;
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
