package com.telesens.afanasiev;

import java.util.Date;

/**
 * Created by oleg on 12/23/15.
 */
public class Simulator implements Runnable {
    private final int DELTA_TIME = 100; // 1 minute
    private ReportCollector reportCollector;
    private Clock clock;
    private Date timeFrom;
    private Date timeTo;
    private TrafficDispatcher trafficDispatcher;

    public Simulator(Date timeFrom, Date timeTo, TrafficDispatcher trafficDispatcher) {
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
        this.trafficDispatcher = trafficDispatcher;

        clock = new Clock(timeFrom);
        reportCollector = ReportCollector.getInstance();
    }

    @Override
    public void run() {
        reportCollector.sendMessage("Simulator", "Симуляция движения поездов началась");

        do {
            reportCollector.sendMessage("",
                    "\n               <----------" + clock.getActualTime().toString() + "---------->");
            trafficDispatcher.tick(clock.getActualTime());
            clock.doTick();
            try {
                Thread.sleep(DELTA_TIME);
            } catch(InterruptedException exc) {
                exc.printStackTrace();
            }

        } while(clock.getActualTime().before(timeTo));

        reportCollector.sendMessage("Simulator", "Симуляция движения поездов завершена");
    }
}
