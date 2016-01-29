package com.telesens.afanasiev.model.reporter.unit;

import lombok.Data;

import java.util.Date;

/**
 * Created by oleg on 1/8/16.
 */
@Data
public class TotalStatistic {
    private Date startTime;
    private Date finishTime;
    private int passTransportedCount;
    private int passNotTransportedCount;
    private int runCount;
    private int bussesCount;
    private double fillingBusses; // in percent
}
