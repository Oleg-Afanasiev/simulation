package com.telesens.afanasiev.loader;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by oleg on 1/1/16.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeTableUnit implements Serializable, Comparable<TimeTableUnit> {
    private static final long serialVersionUID = 1L;

    private long idRoute;
    private Date timeStart;
    private int breakForwardDuration;
    private int breakBackDuration;

    @SuppressWarnings("NullableProblems")
    @Override
    public int compareTo(TimeTableUnit o) {
        return Long.compare(this.timeStart.getTime(), o.timeStart.getTime());
    }
}
