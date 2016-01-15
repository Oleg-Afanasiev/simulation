package com.telesens.afanasiev.model.Identities.impl;

import com.telesens.afanasiev.model.Identities.RunTask;
import com.telesens.afanasiev.model.Identities.Identity;
import lombok.AllArgsConstructor;
import lombok.Getter;

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
