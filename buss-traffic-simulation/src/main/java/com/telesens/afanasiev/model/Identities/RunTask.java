package com.telesens.afanasiev.model.Identities;

import java.util.Date;

/**
 * Created by oleg on 1/15/16.
 */
public interface RunTask extends Identity {
    long getRouteId();
    Date getTimeStart();
    int getBreakForwardDuration();
    int getBreakBackDuration();
    int compareTo(RunTask o);
}
