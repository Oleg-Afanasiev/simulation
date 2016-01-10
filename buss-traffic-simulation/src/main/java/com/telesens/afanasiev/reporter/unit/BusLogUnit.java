package com.telesens.afanasiev.reporter.unit;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

/**
 * Created by oleg on 1/8/16.
 */
@AllArgsConstructor
@Getter
public class BusLogUnit {
    private Date timeStop;
    private long routeId;
    private long stationId;
    private String busNumber;
    private int busCapacity;
    private int getOffPassCount;
    private int takeInPassCount;
    private int insidePassCount;
}
