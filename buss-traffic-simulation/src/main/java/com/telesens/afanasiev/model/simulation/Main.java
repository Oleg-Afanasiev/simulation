package com.telesens.afanasiev.model.simulation;

import com.telesens.afanasiev.dao.DAOException;
import com.telesens.afanasiev.model.identities.Direct;
import com.telesens.afanasiev.model.helper.DateTimeHelper;
import com.telesens.afanasiev.dao.impl.jaxb.schemes.BusNetwork;
import com.telesens.afanasiev.dao.impl.jaxb.schemes.PassGenRules;
import com.telesens.afanasiev.logs.shemes.Buses;
import com.telesens.afanasiev.logs.shemes.Passengers;
import com.telesens.afanasiev.logs.shemes.Runs;
import com.telesens.afanasiev.logs.shemes.Stations;
import com.telesens.afanasiev.model.loader.DataLoader;
import com.telesens.afanasiev.model.reporter.*;
import com.telesens.afanasiev.model.rules.PassengerGenerationRules;
import com.telesens.afanasiev.model.rules.RunTimetable;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.*;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by oleg on 12/4/15.
 */
public class Main {

    private static BusTrafficSimulator simulator;

    private static final String pathBusLogXml = "./src/main/java/com/telesens/afanasiev/logs/bus.xml";
    private static final String pathPassLogXml = "./src/main/java/com/telesens/afanasiev/logs/passengers.xml";
    private static final String pathRouteLogXml = "./src/main/java/com/telesens/afanasiev/logs/route.xml";
    private static final String pathStationLogXml = "./src/main/java/com/telesens/afanasiev/logs/station.xml";

    private static final String pathButNetLoadXml = "./src/main/resources/xml/load/busnetwork.xml";
    private static final String pathPassGenXml = "./src/main/resources/xml/load/runtimetable.xml";
    private static final String pathRunTimetableXml = "./src/main/resources/xml/load/passgeneration.xml";

    private static Date timeFrom;
    private static Date timeTo;

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {

        DataLoader dataLoader = new DataLoader();
        com.telesens.afanasiev.dao.impl.jaxb.schemes.RunTimetable xmlRunTimetable;
        PassGenRules xmlPassGenRules;
        BusNetwork xmlBusNetwork;

        initFromTo();

        //xmlBusNetwork = initBusNetwork();
        xmlRunTimetable = initTimeTable();
        xmlPassGenRules = initPassGenerationRules();

        //writeXmlButNetwork(xmlBusNetwork);
        writeXmlTimeTable(xmlRunTimetable);
        writeXmlPassGenRules(xmlPassGenRules);

        TransportNetwork busNetwork;
        try {
            busNetwork = dataLoader.loadBusNetwork();
        } catch (IllegalArgumentException | DAOException exc) {
            System.out.println("Не удалось загрузить схему маршрутов");
            exc.printStackTrace();
            return;
        }
        RunTimetable runTimetable = dataLoader.loadRunTimetable();
        PassengerGenerationRules passGenerationRules = dataLoader.loadPassGenRules();

        simulator = new BusTrafficSimulator(busNetwork, runTimetable, passGenerationRules, timeFrom, timeTo);

        Thread tSimulator = new Thread(simulator);
        Thread tLogWriter = new Thread(new LogWriter());
        Thread tBusXmlWriter = new Thread(new XmlWriter(pathBusLogXml, Buses.class, Buses.Log.class, new BusXmlWriter()));

        Thread tPassXmlWriter = new Thread(new XmlWriter(pathPassLogXml, Passengers.class, Passengers.Log.class, new PassengerXmlWriter()));
        Thread tStationXmlWriter = new Thread(new XmlWriter(pathStationLogXml, BusNetwork.Stations.class, Stations.Log.class, new StationXmlWriter()));
        Thread tRouteXmlWriter = new Thread(new XmlWriter(pathRouteLogXml, Runs.class, Runs.Log.class, new RouteXmlWriter()));

        tSimulator.start();
        tLogWriter.start();
        tBusXmlWriter.start();
        tPassXmlWriter.start();
        tStationXmlWriter.start();
        tRouteXmlWriter.start();
    }

    private static BusNetwork initBusNetwork() throws NoSuchFieldException, IllegalAccessException {
        BusNetwork busNetwork = new BusNetwork();
        //busNetwork = TransportNetwork.getInstance();

        final int N_STATIONS = 18;
        BusNetwork.Stations.Station[] stations = new BusNetwork.Stations.Station[N_STATIONS];

        for (int i = 0; i < N_STATIONS; i++) {
            stations[i] = new BusNetwork.Stations.Station();
            stations[i].setId(i+1);
        }

        // test simple route
        stations[0].setName("Героев Труда");
        stations[1].setName("Караван");
        stations[2].setName("Жилярди");
        stations[3].setName("Тахиаташская");
        stations[4].setName("Киевская");
        stations[5].setName("Горбатый мост");
        stations[6].setName("Московский проспект");
        stations[7].setName("Советская");

        stations[8].setName("Московский проспект");
        stations[9].setName("Горбатый мост");
        stations[10].setName("Киевская");
        stations[11].setName("Тахиаташская");
        stations[12].setName("Жилярди");
        stations[13].setName("Караван");

        stations[14].setName("Парк Горького");
        stations[15].setName("Барабашова");
        stations[16].setName("Горбатый мост");
        stations[17].setName("Площадь Фейербаха");
        stations[18].setName("Площадь Восстания");

//        busNetwork.createRoute(1_272, "272", "", Direct.FORWARD, 5.50, stations[0],
//                new ArcImpl<>(stations[0], stations[1], 3),
//                new ArcImpl<>(stations[2], stations[1], 2),
//                new ArcImpl<>(stations[2], stations[3], 5),
//                new ArcImpl<>(stations[3], stations[4], 6),
//                new ArcImpl<>(stations[4], stations[5], 5),
//                new ArcImpl<>(stations[5], stations[6], 4),
//                new ArcImpl<>(stations[6], stations[7], 4)
//                );
//
//        busNetwork.createRoute(2_272, "272", "", Direct.BACK, 5.50, stations[7],
//                new ArcImpl<>(stations[7], stations[8], 4),
//                new ArcImpl<>(stations[8], stations[9], 4),
//                new ArcImpl<>(stations[9], stations[10], 5),
//                new ArcImpl<>(stations[10], stations[11], 6),
//                new ArcImpl<>(stations[11], stations[12], 5),
//                new ArcImpl<>(stations[12], stations[13], 2),
//                new ArcImpl<>(stations[13], stations[0], 3)
//        );

        //routesDispatcher.add(new RouteDispatcher(busNetwork.getRouteById(1_272), busNetwork.getRouteById(2_272)));

        // test of "CIRCULAR" route
        int countCircStations = 10;
        BusNetwork.Stations.Station[] circStations = new BusNetwork.Stations.Station[countCircStations];

        for (int i = 0; i < countCircStations; i++) {
            circStations[i] = new BusNetwork.Stations.Station();
            circStations[i].setId(i + 101);
        }

        circStations[0].setName("Льва Толстого");
        circStations[1].setName("11 Поликлиника");
        circStations[2].setName("531 микрорайон");
        circStations[3].setName("Сабурова Дача");
        circStations[4].setName("Барабашова");
        circStations[5].setName("Академика Павлова");
        circStations[6].setName("Студенческая");
        circStations[7].setName("Героев Труда");
        circStations[8].setName("531 микрорайон");
        circStations[9].setName("11 Поликлиника");



//        busNetwork.createRoute(16, "16", "", Direct.FORWARD, 2.50, circStations[0],
//                new ArcImpl<>(circStations[0], circStations[1], 3),
//                new ArcImpl<>(circStations[1], circStations[2], 5),
//                new ArcImpl<>(circStations[2], stations[0], 2),
//                new ArcImpl<>(stations[0], stations[1], 3),
//                new ArcImpl<>(stations[1], stations[2], 4),
//                new ArcImpl<>(stations[2], stations[3], 5),
//                new ArcImpl<>(stations[3], stations[4], 5),
//                new ArcImpl<>(stations[4], circStations[3], 4),
//                new ArcImpl<>(circStations[3], circStations[4], 5),
//                new ArcImpl<>(circStations[4], circStations[5], 3),
//                new ArcImpl<>(circStations[5], circStations[6], 4),
//                new ArcImpl<>(circStations[6], circStations[7], 2),
//                new ArcImpl<>(circStations[7], circStations[8], 2),
//                new ArcImpl<>(circStations[8], circStations[9], 5),
//                new ArcImpl<>(circStations[9], circStations[0], 3)
//        );
        //routesDispatcher.add(new RouteDispatcher(busNetwork.getRouteById(16)));

//        busNetwork.createRoute(1, "test", "", Direct.FORWARD, 3, stations[0],
//                new ArcImpl<>(stations[0], stations[4], 3),
//                new ArcImpl<>(stations[4], stations[7], 2),
//                new ArcImpl<>(stations[7], stations[0], 5)
//        );

        BusNetwork.Stations stats = new BusNetwork.Stations();
        BusNetwork.Arcs arcs = new BusNetwork.Arcs();
        BusNetwork.Routes routes = new BusNetwork.Routes();
        BusNetwork.Arcs.Arc arc;
        BusNetwork.Routes.Route.ArcsLink arcLink;
        BusNetwork.Routes.Route route = new BusNetwork.Routes.Route();

        stats.getStation().add(stations[0]);
        stats.getStation().add(stations[4]);
        stats.getStation().add(stations[7]);

        arc = new BusNetwork.Arcs.Arc();
        arc.setId(101);
        arc.setNodeLeftId(stations[0].getId());
        arc.setNodeRightId(stations[4].getId());
        arc.setDuration(3);
        arcs.getArc().add(arc);

        arc = new BusNetwork.Arcs.Arc();
        arc.setId(102);
        arc.setNodeLeftId(stations[4].getId());
        arc.setNodeRightId(stations[7].getId());
        arc.setDuration(2);
        arcs.getArc().add(arc);

        arc = new BusNetwork.Arcs.Arc();
        arc.setId(103);
        arc.setNodeLeftId(stations[7].getId());
        arc.setNodeRightId(stations[0].getId());
        arc.setDuration(5);
        arcs.getArc().add(arc);

        arcLink = new BusNetwork.Routes.Route.ArcsLink();
        arcLink.setArcId(101);
        route.getArcsLink().add(arcLink);

        arcLink = new BusNetwork.Routes.Route.ArcsLink();
        arcLink.setArcId(102);
        route.getArcsLink().add(arcLink);

        arcLink = new BusNetwork.Routes.Route.ArcsLink();
        arcLink.setArcId(103);
        route.getArcsLink().add(arcLink);

        route.setId(1);
        route.setFirstNodeId(stations[0].getId());
        route.setNumber("272");
        route.setDirect(Direct.FORWARD.name());
        route.setCost(3);

//        RouteImpl<StationImpl> routeTest = new RouteImpl<>("test", "", Direct.FORWARD, 3, stations[0],
//                new ArcImpl<>(stations[0], stations[4], 3),
//                new ArcImpl<>(stations[4], stations[7], 2),
//                new ArcImpl<>(stations[7], stations[0], 5)
//        );
//
//        DaoUtils.setPrivateField(routeTest, "ID", 1);

        routes.getRoute().add(route);
        busNetwork.setStations(stats);
        busNetwork.setArcs(arcs);
        busNetwork.setRoutes(routes);

        return busNetwork;
    }

    private static void initFromTo() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2015, 11, 1, 7, 0, 0);
        timeFrom = DateTimeHelper.roundByMinutes(calendar.getTime());
        timeTo = DateTimeHelper.incHours(timeFrom, 2);
    }

    private static com.telesens.afanasiev.dao.impl.jaxb.schemes.RunTimetable initTimeTable() {
        com.telesens.afanasiev.dao.impl.jaxb.schemes.RunTimetable xmlRunTimetable = new com.telesens.afanasiev.dao.impl.jaxb.schemes.RunTimetable();
        com.telesens.afanasiev.dao.impl.jaxb.schemes.RunTimetable.Task task;
        int runInterval = 9;
        Date iTime;
        long id = 1;

        for (int m = 0; m < 120; m+= runInterval) {
            task = new com.telesens.afanasiev.dao.impl.jaxb.schemes.RunTimetable.Task();
            iTime = DateTimeHelper.incMinutes(timeFrom, m);

            task.setId(id++);
            task.setIdRoute(1);
            task.setTimeStart(DateTimeHelper.dateToXMLGregorianCalendar(iTime));
            task.setBreakForwardDuration(5);
            task.setBreakBackDuration(0);
            xmlRunTimetable.getTask().add(task);
        }

        return xmlRunTimetable;
    }

    private static PassGenRules initPassGenerationRules () {

        //----------TARGETS-------------
        final int N_TARGETS = 10;

        PassGenRules.Targets.Target[] target = new PassGenRules.Targets.Target[N_TARGETS];
        for (int i = 0; i < N_TARGETS; i++) {
            target[i] = new PassGenRules.Targets.Target();
            target[i].setId(i+1);
        }

        target[0].setStationId(5);
        target[0].setFactor(3);

        target[1].setStationId(8);
        target[1].setFactor(7);

        target[2].setStationId(5);
        target[2].setFactor(1);

        target[3].setStationId(8);
        target[3].setFactor(1);

        target[4].setStationId(1);
        target[4].setFactor(2);

        target[5].setStationId(8);
        target[5].setFactor(8);

        target[6].setStationId(1);
        target[6].setFactor(1);

        target[7].setStationId(8);
        target[7].setFactor(9);

        target[8].setStationId(1);
        target[8].setFactor(1);

        target[9].setStationId(1);
        target[9].setFactor(1);

        //----------TASKS-------------

        final int N_TASK = 6;
        PassGenRules.Tasks.Task.LinkTarget linkTarget;
        PassGenRules.Tasks.Task[] tasks = new PassGenRules.Tasks.Task[N_TASK];

        for (int i = 0; i < tasks.length; i++) {
            tasks[i] = new PassGenRules.Tasks.Task();
            tasks[i].setId(i+1);
        }

        // for "Героев Труда"
        // first hour
        linkTarget = new PassGenRules.Tasks.Task.LinkTarget();
        linkTarget.setTargetId(1);
        tasks[0].getLinkTarget().add(linkTarget);

        linkTarget = new PassGenRules.Tasks.Task.LinkTarget();
        linkTarget.setTargetId(2);
        tasks[0].getLinkTarget().add(linkTarget);

        tasks[0].setStationId(1);
        tasks[0].setTimeFrom(DateTimeHelper.dateToXMLGregorianCalendar(timeFrom));
        tasks[0].setDuration(60);
        tasks[0].setPassCount(200);
        tasks[0].setMinutesLimitWaiting(15);

        // second hour
        linkTarget = new PassGenRules.Tasks.Task.LinkTarget();
        linkTarget.setTargetId(3);
        tasks[1].getLinkTarget().add(linkTarget);

        linkTarget = new PassGenRules.Tasks.Task.LinkTarget();
        linkTarget.setTargetId(4);
        tasks[1].getLinkTarget().add(linkTarget);

        tasks[1].setStationId(1);
        tasks[1].setTimeFrom(DateTimeHelper.dateToXMLGregorianCalendar(DateTimeHelper.incHour(timeFrom)));
        tasks[1].setDuration(60);
        tasks[1].setPassCount(300);
        tasks[1].setMinutesLimitWaiting(10);

        // for "Киевская"
        // first hour
        linkTarget = new PassGenRules.Tasks.Task.LinkTarget();
        linkTarget.setTargetId(5);
        tasks[2].getLinkTarget().add(linkTarget);

        linkTarget = new PassGenRules.Tasks.Task.LinkTarget();
        linkTarget.setTargetId(6);
        tasks[2].getLinkTarget().add(linkTarget);

        tasks[2].setStationId(5);
        tasks[2].setTimeFrom(DateTimeHelper.dateToXMLGregorianCalendar(timeFrom));
        tasks[2].setDuration(60);
        tasks[2].setPassCount(100);
        tasks[2].setMinutesLimitWaiting(10);

        // second hour
        linkTarget = new PassGenRules.Tasks.Task.LinkTarget();
        linkTarget.setTargetId(7);
        tasks[3].getLinkTarget().add(linkTarget);

        linkTarget = new PassGenRules.Tasks.Task.LinkTarget();
        linkTarget.setTargetId(8);
        tasks[3].getLinkTarget().add(linkTarget);

        tasks[3].setStationId(5);
        tasks[3].setTimeFrom(DateTimeHelper.dateToXMLGregorianCalendar(DateTimeHelper.incHour(timeFrom)));
        tasks[3].setDuration(60);
        tasks[3].setPassCount(150);
        tasks[3].setMinutesLimitWaiting(10);

        // for "Советская"
        // first hour
        linkTarget = new PassGenRules.Tasks.Task.LinkTarget();
        linkTarget.setTargetId(9);
        tasks[4].getLinkTarget().add(linkTarget);

        tasks[4].setStationId(8);
        tasks[4].setTimeFrom(DateTimeHelper.dateToXMLGregorianCalendar(timeFrom));
        tasks[4].setDuration(60);
        tasks[4].setPassCount(250);
        tasks[4].setMinutesLimitWaiting(15);

        // second hour
        linkTarget = new PassGenRules.Tasks.Task.LinkTarget();
        linkTarget.setTargetId(10);
        tasks[5].getLinkTarget().add(linkTarget);

        tasks[5].setStationId(8);
        tasks[5].setTimeFrom(DateTimeHelper.dateToXMLGregorianCalendar(DateTimeHelper.incHour(timeFrom)));
        tasks[5].setDuration(60);
        tasks[5].setPassCount(350);
        tasks[5].setMinutesLimitWaiting(10);

        PassGenRules passGenRules = new PassGenRules();
        passGenRules.setTargets(new PassGenRules.Targets());
        passGenRules.setTasks(new PassGenRules.Tasks());

        for (int i = 0; i < target.length; i++) {
            passGenRules.getTargets().getTarget().add(target[i]);
        }

        for (int i = 0; i < tasks.length; i++) {
            passGenRules.getTasks().getTask().add(tasks[i]);
        }

        return passGenRules;
    }

    private static void writeXmlButNetwork(BusNetwork busNetwork) {
        try  {
            JAXBContext jaxbContext = JAXBContext.newInstance(BusNetwork.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(busNetwork, new FileWriter(pathButNetLoadXml));
        } catch (IOException | JAXBException exc) {
            exc.printStackTrace();
        }
    }

    private static void writeXmlTimeTable(com.telesens.afanasiev.dao.impl.jaxb.schemes.RunTimetable xmlRunTimetable) {
        try  {
            JAXBContext jaxbContext = JAXBContext.newInstance(com.telesens.afanasiev.dao.impl.jaxb.schemes.RunTimetable.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(xmlRunTimetable, new FileWriter(pathPassGenXml));

        } catch (IOException | JAXBException exc) {
            exc.printStackTrace();
        }
    }

    private static void writeXmlPassGenRules(PassGenRules passGenRules) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(PassGenRules.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(passGenRules, new FileWriter(pathRunTimetableXml));

        } catch(IOException | JAXBException exc) {
            exc.printStackTrace();
        }
    }
}
