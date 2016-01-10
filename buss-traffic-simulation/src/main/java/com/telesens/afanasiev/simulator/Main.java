package com.telesens.afanasiev.simulator;

import com.telesens.afanasiev.helper.DaoUtils;
import com.telesens.afanasiev.helper.DateTimeHelper;
import com.telesens.afanasiev.jaxb.logs.Buses;
import com.telesens.afanasiev.jaxb.logs.Passengers;
import com.telesens.afanasiev.jaxb.logs.Runs;
import com.telesens.afanasiev.jaxb.logs.Stations;
import com.telesens.afanasiev.loader.TimeTable;
import com.telesens.afanasiev.reporter.*;
import com.telesens.afanasiev.rules.PassengerGenerationRules;
import com.telesens.afanasiev.rules.PassengerTargetSpreading;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by oleg on 12/4/15.
 */
public class Main {

    private static BusTrafficSimulator simulator;

    private static TransportNetwork busNetwork;
    private static TimeTable timeTable;
    private static PassengerGenerationRules passGenerationRules;
    private static Date timeFrom;
    private static Date timeTo;

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {

        TransportNetwork tn;

        initFromTo();

        initBusNetwork();
        initTimeTable();
        initPassGenerationRules();

        serializationBusNetwork(busNetwork);
        serializationTimeTable(timeTable);
        serializationPassGenerationRules(passGenerationRules);

        busNetwork = deserializationBusNetwork();
        timeTable = deserializationTimeTable();
        passGenerationRules = deserializationPassGenerationRules();

        simulator = new BusTrafficSimulator(busNetwork, timeTable, passGenerationRules, timeFrom, timeTo);
        Thread tSimulator = new Thread(simulator);
        Thread tLogWriter = new Thread(new LogWriter());
        Thread tBusXmlWriter = new Thread(new XmlWriter("./src/main/resources/xml/logs/bus.xml",
                Buses.class, Buses.Log.class, new BusXmlWriter()));

        Thread tPassXmlWriter = new Thread(new XmlWriter("./src/main/resources/xml/logs/passengers.xml",
                Passengers.class, Passengers.Log.class, new PassengerXmlWriter()));

        Thread tStationXmlWriter = new Thread(new XmlWriter("./src/main/resources/xml/logs/station.xml",
                Stations.class, Stations.Log.class, new StationXmlWriter()));

        Thread tRouteXmlWriter = new Thread(new XmlWriter("./src/main/resources/xml/logs/route.xml",
                Runs.class, Runs.Log.class, new RouteXmlWriter()));

        tSimulator.start();
        tLogWriter.start();
        tBusXmlWriter.start();
        tPassXmlWriter.start();
        tStationXmlWriter.start();
        tRouteXmlWriter.start();

        //simulator.loadData(timeTable, passGenerationRules);
        //System.out.println(simulator.allRoutesToString());
    }

    private static void initBusNetwork() throws NoSuchFieldException, IllegalAccessException {

        busNetwork = TransportNetwork.getInstance();

        int countStations = 14;
        Station[] stations = new Station[countStations];

        // test simple route
        stations[0] = new Station("Героев Труда",  busNetwork);
        stations[1] = new Station("Караван",  busNetwork);
        stations[2] = new Station("Жилярди",  busNetwork);
        stations[3] = new Station("Тахиаташская", busNetwork);
        stations[4] = new Station("Киевская",  busNetwork);
        stations[5] = new Station("Горбатый мост",  busNetwork);
        stations[6] = new Station("Московский проспект",  busNetwork);
        stations[7] = new Station("Советская",  busNetwork);

        stations[8] = new Station("Московский проспект",  busNetwork);
        stations[9] = new Station("Горбатый мост",  busNetwork);
        stations[10] = new Station("Киевская",  busNetwork);
        stations[11] = new Station("Тахиаташская",  busNetwork);
        stations[12] = new Station("Жилярди",  busNetwork);
        stations[13] = new Station("Караван",  busNetwork);

        for (int i = 0; i < countStations; i++) {
            DaoUtils.setPrivateField(stations[i], "ID", i + 1);
        }

//        busNetwork.createRoute(1_272, "272", "", Direct.FORWARD, 5.50, stations[0],
//                new Arc<>(stations[0], stations[1], 3),
//                new Arc<>(stations[2], stations[1], 2),
//                new Arc<>(stations[2], stations[3], 5),
//                new Arc<>(stations[3], stations[4], 6),
//                new Arc<>(stations[4], stations[5], 5),
//                new Arc<>(stations[5], stations[6], 4),
//                new Arc<>(stations[6], stations[7], 4)
//                );
//
//        busNetwork.createRoute(2_272, "272", "", Direct.BACK, 5.50, stations[7],
//                new Arc<>(stations[7], stations[8], 4),
//                new Arc<>(stations[8], stations[9], 4),
//                new Arc<>(stations[9], stations[10], 5),
//                new Arc<>(stations[10], stations[11], 6),
//                new Arc<>(stations[11], stations[12], 5),
//                new Arc<>(stations[12], stations[13], 2),
//                new Arc<>(stations[13], stations[0], 3)
//        );

        //routesDispatcher.add(new RouteDispatcher(busNetwork.getRouteById(1_272), busNetwork.getRouteById(2_272)));

        // test of "CIRCULAR" route
        int countCircStations = 10;
        Station[] circStations = new Station[countCircStations];

        circStations[0] = new Station("Льва Толстого",  busNetwork);
        circStations[1] = new Station("11 Поликлиника",  busNetwork);
        circStations[2] = new Station("531 микрорайон",  busNetwork);
        circStations[3] = new Station("Сабурова Дача",  busNetwork);
        circStations[4] = new Station("Барабашова",  busNetwork);
        circStations[5] = new Station("Академика Павлова",  busNetwork);
        circStations[6] = new Station("Студенческая",  busNetwork);
        circStations[7] = new Station("Героев Труда",  busNetwork);
        circStations[8] = new Station("531 микрорайон",  busNetwork);
        circStations[9] = new Station("11 Поликлиника",  busNetwork);

        for (int i = 0; i < countCircStations; i++) {
            DaoUtils.setPrivateField(circStations[i], "ID", i + 101);
        }

//        busNetwork.createRoute(16, "16", "", Direct.FORWARD, 2.50, circStations[0],
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
        //routesDispatcher.add(new RouteDispatcher(busNetwork.getRouteById(16)));

//        busNetwork.createRoute(1, "test", "", Direct.FORWARD, 3, stations[0],
//                new Arc<>(stations[0], stations[4], 3),
//                new Arc<>(stations[4], stations[7], 2),
//                new Arc<>(stations[7], stations[0], 5)
//        );

        Route<Station> routeTest = new Route<>("test", "", Direct.FORWARD, 3, stations[0],
                new Arc<>(stations[0], stations[4], 3),
                new Arc<>(stations[4], stations[7], 2),
                new Arc<>(stations[7], stations[0], 5)
        );

        DaoUtils.setPrivateField(routeTest, "ID", 1);

        busNetwork.addRoute(routeTest);
    }

    private static void initFromTo() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2015, 11, 1, 7, 0, 0);
        timeFrom = DateTimeHelper.roundByMinutes(calendar.getTime());
        timeTo = DateTimeHelper.incHours(timeFrom, 2);
    }

    private static void initTimeTable() {
        timeTable = new TimeTable();
        int runInterval = 9;
        Date iTime;

        for (int m = 0; m < 120; m+= runInterval) {
            iTime = DateTimeHelper.incMinutes(timeFrom, m);
            timeTable.addTask(1L, iTime, 5, 0);
            //timeTable.addTask(1L, iTime, 4, 0);
        }
    }

    private static void initPassGenerationRules () {

        // for "Героев Труда"
        passGenerationRules = new PassengerGenerationRules();
        PassengerTargetSpreading passTargetSpreading = new PassengerTargetSpreading();

        passTargetSpreading.addTarget(5, 3);
        passTargetSpreading.addTarget(8, 7);
        passGenerationRules.addRule(1, timeFrom, 60, 200, 15, passTargetSpreading);

        passTargetSpreading = new PassengerTargetSpreading();
        passTargetSpreading.addTarget(5, 1);
        passTargetSpreading.addTarget(8, 1);
        passGenerationRules.addRule(1, 60, 300, 10, passTargetSpreading);

        // for "Киевская"
        passTargetSpreading = new PassengerTargetSpreading();
        passTargetSpreading.addTarget(1, 2);
        passTargetSpreading.addTarget(8, 8);
        passGenerationRules.addRule(5, timeFrom, 60, 100, 10, passTargetSpreading);

        passTargetSpreading = new PassengerTargetSpreading();
        passTargetSpreading.addTarget(1, 1);
        passTargetSpreading.addTarget(8, 9);
        passGenerationRules.addRule(5, 60, 150, 10, passTargetSpreading);

        // for "Советская"
        passTargetSpreading = new PassengerTargetSpreading();
        passTargetSpreading.addTarget(1, 1);
        passGenerationRules.addRule(8, timeFrom, 60, 250, 15, passTargetSpreading);

        passTargetSpreading = new PassengerTargetSpreading();
        passTargetSpreading.addTarget(1, 1);
        passGenerationRules.addRule(8, 60, 350, 10, passTargetSpreading);
    }

    private static void serializationBusNetwork(TransportNetwork busNetwork) {
        try (XMLEncoder xmlEncoder = new XMLEncoder(new FileOutputStream("./src/main/resources/xml/busnetwork.xml"))) {
            xmlEncoder.writeObject(busNetwork);
            xmlEncoder.flush();
        } catch(FileNotFoundException exc) {
            exc.printStackTrace();
        }
    }

    private static  void serializationTimeTable(TimeTable timeTable) {
        try (XMLEncoder xmlEncoder = new XMLEncoder(new FileOutputStream("./src/main/resources/xml/timetable.xml"))) {
            xmlEncoder.writeObject(timeTable);
            xmlEncoder.flush();
        } catch (FileNotFoundException exc) {
            exc.printStackTrace();
        }
    }

    private static void serializationPassGenerationRules(PassengerGenerationRules passGenerationRules) {
        try (XMLEncoder xmlEncoder = new XMLEncoder(new FileOutputStream("./src/main/resources/xml/passgeneration.xml"))) {
            xmlEncoder.writeObject(passGenerationRules);
            xmlEncoder.flush();
        } catch(FileNotFoundException exc) {
            exc.printStackTrace();
        }
    }

    private static TransportNetwork deserializationBusNetwork() {
        TransportNetwork busNetwork = TransportNetwork.getInstance();
        try(XMLDecoder xmlDecoder = new XMLDecoder(new FileInputStream("./src/main/resources/xml/busnetwork.xml"))) {
            busNetwork = (TransportNetwork)xmlDecoder.readObject();
        } catch(FileNotFoundException exc) {
            exc.printStackTrace();
        }

        return busNetwork;
    }

    private static TimeTable deserializationTimeTable() {
        TimeTable timeTable = null;
        try (XMLDecoder xmlDecoder = new XMLDecoder(new FileInputStream("./src/main/resources/xml/timetable.xml"))) {
            timeTable = (TimeTable)xmlDecoder.readObject();
        } catch(FileNotFoundException exc) {
            exc.printStackTrace();
        }

        return timeTable;
    }

    private static PassengerGenerationRules deserializationPassGenerationRules() {
        PassengerGenerationRules passGenerationRules = null;
        try (XMLDecoder xmlDecoder = new XMLDecoder(new FileInputStream("./src/main/resources/xml/passgeneration.xml"))) {
            passGenerationRules = (PassengerGenerationRules)xmlDecoder.readObject();
        } catch(FileNotFoundException exc) {
            exc.printStackTrace();
        }

        return passGenerationRules;
    }

}
