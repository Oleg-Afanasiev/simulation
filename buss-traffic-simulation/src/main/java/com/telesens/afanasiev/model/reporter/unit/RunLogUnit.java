package com.telesens.afanasiev.model.reporter.unit;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

/**
 * Created by oleg on 1/9/16.
 */
@AllArgsConstructor
@Getter
public class RunLogUnit {
    private long routeId;
    private String routeNumber;
    private String busNumber;
    private Date timeStart;
    private Date timeFinish;
    private int passDeliveredCount;
}
