package com.telesens.afanasiev.model.simulation;

import com.telesens.afanasiev.dao.DAOException;
import com.telesens.afanasiev.logs.shemes.*;
import com.telesens.afanasiev.model.identities.Direct;
import com.telesens.afanasiev.model.helper.DateTimeHelper;
import com.telesens.afanasiev.dao.impl.jaxb.schemes.BusNetwork;
import com.telesens.afanasiev.dao.impl.jaxb.schemes.PassGenRules;
import com.telesens.afanasiev.model.loader.DataLoader;
import com.telesens.afanasiev.model.reporter.*;
import com.telesens.afanasiev.model.reporter.unit.TotalStatistic;
import com.telesens.afanasiev.model.rules.PassengerGenerationRules;
import com.telesens.afanasiev.model.rules.RunTimetable;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
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
    private static final String pathStatisticLogXml = "./src/main/java/com/telesens/afanasiev/logs/statistic.xml";

    private static final String pathButNetLoadXml = "./src/main/resources/xml/load/busnetwork.xml";
    private static final String pathPassGenXml = "./src/main/resources/xml/load/runtimetable.xml";
    private static final String pathRunTimetableXml = "./src/main/resources/xml/load/passgeneration.xml";

    private static Date timeFrom;
    private static Date timeTo;

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {

        DataLoader dataLoader = new DataLoader();

        initFromTo();

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

        LogCollector logCollector = LogCollector.getInstance();

        try {
            tSimulator.join();
        } catch(InterruptedException exc ) {}

        TotalStatistic statistic = logCollector.getTotalStatistic();
        writeStatisticToXml(statistic, pathStatisticLogXml);
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

    private static void writeStatisticToXml(TotalStatistic statistic, String dstXML) {
        try {
            FileOutputStream fos = new  FileOutputStream(dstXML);

            JAXBContext jaxbContext = JAXBContext.newInstance(Statistic.class);
            Marshaller marshaller = jaxbContext.createMarshaller();

            Statistic statisticData = new Statistic();
            Statistic.Log log = new Statistic.Log();
            log.setTimeStart(DateTimeHelper.dateToXMLGregorianCalendar(statistic.getStartTime()));
            log.setTimeFinish(DateTimeHelper.dateToXMLGregorianCalendar(statistic.getFinishTime()));
            log.setBussesCount(statistic.getBussesCount());
            log.setFillingBusses(statistic.getFillingBusses());
            log.setRunCount(statistic.getRunCount());
            log.setPassNotTransportedCount(statistic.getPassNotTransportedCount());
            log.setPassTransportedCount(statistic.getPassTransportedCount());

            statisticData.getLog().add(log);

            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(statisticData, fos);

        } catch(JAXBException | IOException  exc) {
            exc.printStackTrace();
        }
    }
}
