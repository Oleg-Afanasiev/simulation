package com.afanasiev.telesens.simulate;

import java.util.Date;

/**
 * Created by oleg on 12/9/15.
 */
public class PassengerSheduler implements Observer {

    private ReportCollector reportCollector;

    public PassengerSheduler() {
        reportCollector = ReportCollector.getInstance();
    }

    public void tick(Date dateTime) {
        reportCollector.sendMessage(this.toString(), "");
    }

    @Override
    public String toString() {
        return "Passenger sheduler";
    }
}
