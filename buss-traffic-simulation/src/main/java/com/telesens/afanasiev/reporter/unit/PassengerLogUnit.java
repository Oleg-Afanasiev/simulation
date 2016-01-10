package com.telesens.afanasiev.reporter.unit;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by oleg on 1/9/16.
 */
@AllArgsConstructor
@Getter
public class PassengerLogUnit {
    private long passengerId;
    private long stationId;
    @Setter private String busNumber;
    @Setter private Date timeComeIn;
    @Setter private Date timeGoOut;

    public PassengerLogUnit(long passengerId, long stationId) {
        this.passengerId = passengerId;
        this.stationId = stationId;
    }
}
