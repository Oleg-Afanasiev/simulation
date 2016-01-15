package com.telesens.afanasiev.model.reporter.unit;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

/**
 * Created by oleg on 1/8/16.
 */
@AllArgsConstructor
@Getter
public class StationLogUnit {
    private long stationId;
    private long busId;
    private long routeId;
    private Date timeBusStop;
    private String busNumber;
    private int takeInPassCount;
    private int getOffPassCount;
    private int stayPassCount;
}
