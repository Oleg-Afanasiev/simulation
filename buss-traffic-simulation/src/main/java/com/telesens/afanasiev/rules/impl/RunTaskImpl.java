package com.telesens.afanasiev.rules.impl;

import com.telesens.afanasiev.rules.RunTask;
import com.telesens.afanasiev.simulation.Identity;
import com.telesens.afanasiev.simulation.impl.IdentityImpl;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by oleg on 1/1/16.
 */
@Getter
@AllArgsConstructor
public class RunTaskImpl extends IdentityImpl implements RunTask, Identity, Comparable<RunTask> {
    private static final long serialVersionUID = 1L;

    private long routeId;
    private Date timeStart;
    private int breakForwardDuration;
    private int breakBackDuration;

    @SuppressWarnings("NullableProblems")
    @Override
    public int compareTo(RunTask o) {
        return Long.compare(this.timeStart.getTime(), o.getTimeStart().getTime());
    }
}
