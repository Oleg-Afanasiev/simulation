package com.afanasiev.telesens.simulate;

import java.util.Date;

/**
 * Created by oleg on 12/9/15.
 */
public class BusTrafficSimulator {
    private Clock clock;
    private PassengerSheduler passengerSheduler;
    private RouteDispatcher runsDispatcher;
    private TransportNetwork bussNetwork;
    private ReportCollector reportCollector;

    private Date fromDateTime;
    private Date toDateTime;

    public BusTrafficSimulator() {

        passengerSheduler = new PassengerSheduler();
        bussNetwork = new TransportNetwork();
        reportCollector = ReportCollector.getInstance();

        try {
            loadData();
        } catch(NoSuchFieldException | IllegalAccessException exc) {
            exc.printStackTrace();
        }
    }

    public void start(Date fromDateTime, Date toDateTime) {
        this.fromDateTime = fromDateTime;
        this.toDateTime = toDateTime;

        clock = new Clock(fromDateTime);
        Date curDateTime = fromDateTime;

        reportCollector.sendMessage(this.toString(), "\n****************START SIMULATION****************");

        while (!curDateTime.after(toDateTime)) {
            reportCollector.sendMessage("", "");
            reportCollector.sendMessage("Clock -> tick", DateTimeRepresenter.toString(curDateTime) + ": \n");
            clock.doTick();
            curDateTime = clock.getDateTime();

            passengerSheduler.tick(curDateTime);
            runsDispatcher.tick(curDateTime);

        }
        reportCollector.sendMessage(this.toString(), "\n****************FINISH SIMULATION****************");
    }

    public String allRoutesToString() {
        return bussNetwork.allRoutesToString();
    }

    @Override
    public String toString() {
        return "";
    }

    private void loadData() throws NoSuchFieldException, IllegalAccessException {
        int countStations = 14;
        Station[] stations = new Station[countStations];

        stations[0] = new Station("Героев Труда");
        stations[1] = new Station("Караван");
        stations[2] = new Station("Жилярди");
        stations[3] = new Station("Тахиаташская");
        stations[4] = new Station("Киевская");
        stations[5] = new Station("Горбатый мост");
        stations[6] = new Station("Московский проспект");
        stations[7] = new Station("Советская");

        stations[8] = new Station("Московский проспект");
        stations[9] = new Station("Горбатый мост");
        stations[10] = new Station("Киевская");
        stations[11] = new Station("Тахиаташская");
        stations[12] = new Station("Жилярди");
        stations[13] = new Station("Караван");

        for (int i = 0; i < countStations; i++) {
            DaoUtils.setPrivateField(stations[i], "ID", i + 1);
        }

        bussNetwork.createRoute(1_272, "272", "", Direct.FORWARD, 5.50, stations[0],
                new Arc<>(stations[0], stations[1], 3),
                new Arc<>(stations[2], stations[1], 2),
                new Arc<>(stations[2], stations[3], 5),
                new Arc<>(stations[3], stations[4], 6),
                new Arc<>(stations[4], stations[5], 5),
                new Arc<>(stations[5], stations[6], 4),
                new Arc<>(stations[6], stations[7], 4)
                );

        bussNetwork.createRoute(2_272, "272", "", Direct.BACK, 5.50, stations[7],
                new Arc<>(stations[7], stations[8], 4),
                new Arc<>(stations[8], stations[9], 4),
                new Arc<>(stations[9], stations[10], 5),
                new Arc<>(stations[10], stations[11], 6),
                new Arc<>(stations[11], stations[12], 5),
                new Arc<>(stations[12], stations[13], 2),
                new Arc<>(stations[13], stations[0], 3)
        );

        runsDispatcher = new RouteDispatcher(bussNetwork.getRouteById(1_272), bussNetwork.getRouteById(2_272));
    }
}
