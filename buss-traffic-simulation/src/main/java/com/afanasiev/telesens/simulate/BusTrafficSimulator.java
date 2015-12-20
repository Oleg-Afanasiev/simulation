package com.afanasiev.telesens.simulate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * Created by oleg on 12/9/15.
 */
public class BusTrafficSimulator {
    private Clock clock;
    private PassengerSheduler passengerSheduler;
    private Collection<RouteDispatcher> runsDispatcher;
    private TransportNetwork bussNetwork;
    private ReportCollector reportCollector;

    private Date fromDateTime;
    private Date toDateTime;

    public BusTrafficSimulator() {

        passengerSheduler = new PassengerSheduler();
        bussNetwork = new TransportNetwork();
        reportCollector = ReportCollector.getInstance();
        runsDispatcher = new ArrayList<>();

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
            reportCollector.sendMessage("Clock -> tick", DateTimeHelper.toString(curDateTime) + ": \n");
            clock.doTick();
            curDateTime = clock.getDateTime();

            passengerSheduler.tick(curDateTime);
            for (RouteDispatcher routeDispatcher : runsDispatcher)
                routeDispatcher.tick(curDateTime);

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

        // test simple route
        stations[0] = new Station("Героев Труда", (TransportMap)bussNetwork);
        stations[1] = new Station("Караван", (TransportMap)bussNetwork);
        stations[2] = new Station("Жилярди", (TransportMap)bussNetwork);
        stations[3] = new Station("Тахиаташская", (TransportMap)bussNetwork);
        stations[4] = new Station("Киевская", (TransportMap)bussNetwork);
        stations[5] = new Station("Горбатый мост", (TransportMap)bussNetwork);
        stations[6] = new Station("Московский проспект", (TransportMap)bussNetwork);
        stations[7] = new Station("Советская", (TransportMap)bussNetwork);

        stations[8] = new Station("Московский проспект", (TransportMap)bussNetwork);
        stations[9] = new Station("Горбатый мост", (TransportMap)bussNetwork);
        stations[10] = new Station("Киевская", (TransportMap)bussNetwork);
        stations[11] = new Station("Тахиаташская", (TransportMap)bussNetwork);
        stations[12] = new Station("Жилярди", (TransportMap)bussNetwork);
        stations[13] = new Station("Караван", (TransportMap)bussNetwork);

        for (int i = 0; i < countStations; i++) {
            DaoUtils.setPrivateField(stations[i], "ID", i + 1);
        }

//        bussNetwork.createRoute(1_272, "272", "", Direct.FORWARD, 5.50, stations[0],
//                new Arc<>(stations[0], stations[1], 3),
//                new Arc<>(stations[2], stations[1], 2),
//                new Arc<>(stations[2], stations[3], 5),
//                new Arc<>(stations[3], stations[4], 6),
//                new Arc<>(stations[4], stations[5], 5),
//                new Arc<>(stations[5], stations[6], 4),
//                new Arc<>(stations[6], stations[7], 4)
//                );
//
//        bussNetwork.createRoute(2_272, "272", "", Direct.BACK, 5.50, stations[7],
//                new Arc<>(stations[7], stations[8], 4),
//                new Arc<>(stations[8], stations[9], 4),
//                new Arc<>(stations[9], stations[10], 5),
//                new Arc<>(stations[10], stations[11], 6),
//                new Arc<>(stations[11], stations[12], 5),
//                new Arc<>(stations[12], stations[13], 2),
//                new Arc<>(stations[13], stations[0], 3)
//        );

        //runsDispatcher.add(new RouteDispatcher(bussNetwork.getRouteById(1_272), bussNetwork.getRouteById(2_272)));

        // test of "CIRCULAR" route
        int countCircStations = 10;
        Station[] circStations = new Station[countCircStations];

        circStations[0] = new Station("Льва Толстого", (TransportMap)bussNetwork);
        circStations[1] = new Station("11 Поликлиника", (TransportMap)bussNetwork);
        circStations[2] = new Station("531 микрорайон", (TransportMap)bussNetwork);
        circStations[3] = new Station("Сабурова Дача", (TransportMap)bussNetwork);
        circStations[4] = new Station("Барабашова", (TransportMap)bussNetwork);
        circStations[5] = new Station("Академика Павлова", (TransportMap)bussNetwork);
        circStations[6] = new Station("Студенческая", (TransportMap)bussNetwork);
        circStations[7] = new Station("Героев Труда", (TransportMap)bussNetwork);
        circStations[8] = new Station("531 микрорайон", (TransportMap)bussNetwork);
        circStations[9] = new Station("11 Поликлиника", (TransportMap)bussNetwork);

        for (int i = 0; i < countCircStations; i++) {
            DaoUtils.setPrivateField(circStations[i], "ID", i + 101);
        }

//        bussNetwork.createRoute(16, "16", "", Direct.FORWARD, 2.50, circStations[0],
//                new Arc<>(circStations[0], circStations[1], 3),
//                new Arc<>(circStations[1], circStations[2], 5),
//                new Arc<>(circStations[2], stations[0], 2),
//                new Arc<>(stations[0], stations[1], 3),
//                new Arc<>(stations[1], stations[2], 4),
//                new Arc<>(stations[2], stations[3], 5),
//                new Arc<>(stations[3], stations[4], 5),
//                new Arc<>(stations[4], circStations[3], 4),
//                new Arc<>(circStations[3], circStations[4], 5),
//                new Arc<>(circStations[4], circStations[5], 3),
//                new Arc<>(circStations[5], circStations[6], 4),
//                new Arc<>(circStations[6], circStations[7], 2),
//                new Arc<>(circStations[7], circStations[8], 2),
//                new Arc<>(circStations[8], circStations[9], 5),
//                new Arc<>(circStations[9], circStations[0], 3)
//        );
        //runsDispatcher.add(new RouteDispatcher(bussNetwork.getRouteById(16)));

        bussNetwork.createRoute(1, "test", "", Direct.FORWARD, 3, stations[0],
                new Arc<>(stations[0], stations[4], 6),
                new Arc<>(stations[4], stations[7], 9),
                new Arc<>(stations[7], stations[0], 14)
        );

        runsDispatcher.add(new RouteDispatcher(bussNetwork.getRouteById(1)));

        passengerSheduler.registerRoutes(bussNetwork.getAllRoutes());
    }
}
