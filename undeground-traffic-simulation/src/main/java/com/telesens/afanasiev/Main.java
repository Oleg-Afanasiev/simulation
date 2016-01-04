package com.telesens.afanasiev;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by oleg on 12/23/15.
 */
public class Main {
    private static final int STOP_DURATION = 1; // min

    private static Station[] listStations;
    private static Arc<Station>[] listArcs;

    private static TimeTable timeTable;

    private static final int YEAR = 2015;
    private static final int MONTH = 11;
    private static final int DAY = 24;

    private static int hourFrom = 10;
    private static int houtTo = 12;
    private static int movingInterval = 4;

    private static Route<Station> routeForward;
    private static Route<Station> routeBack;
    private static TrafficDispatcher trafficDispatcher;
    private static Simulator simulator;
    private static Scheduler scheduler;

    public static void main(String[] args) {
        System.out.println("**********Иммитация движения метрополитена************\n");
        initStations();
        initArcs();
        initRoutes();
        //initTimeTable();
        deserializeTimeTable();
        initTrafficDispatcher();
        initSimulator();
        initSheduler();

        startSimulation();
    }

    private static void startSimulation() {
        Thread tTrainTraffic = new Thread(simulator);
        Thread tScheduler = new Thread(scheduler);

        tTrainTraffic.start();
        tScheduler.start();

        try {
            tTrainTraffic.join();
        } catch(InterruptedException exc) {
            exc.printStackTrace();
        }

        scheduler.finish();
    }

    private static void initStations() {
        listStations = new Station[]{
                new Station(1, "Героев Труда", 1500),
                new Station(2, "Студенческая", 900),
                new Station(3, "Академика Павлова", 750),
                new Station(4, "Барабашова", 1200),
                new Station(5, "Киевская", 450),
                new Station(6, "Пушкинская", 600),
                new Station(7, "Университет", 1350),
                new Station(8, "Исторический музей", 1500),
        };
    }

    private static void initTimeTable() {
        Calendar calendar = Calendar.getInstance();
        Date time;

        timeTable = new TimeTable();
        TrainTask trainTask;

        for (int m = 0; m <= (houtTo - hourFrom) * 60; m += movingInterval) {
            calendar.set(YEAR, MONTH, DAY, hourFrom + m / 60, m % 60, 0);
            time = calendar.getTime();
            trainTask = new TrainTask(time,  listStations[3]);
            timeTable.addTask(trainTask);
        }

        serializeTimeTable();
    }

    private static void initArcs() {

        listArcs = new Arc[]{
                new Arc<>(listStations[0], listStations[1], 2),
                new Arc<>(listStations[1], listStations[2], 2),
                new Arc<>(listStations[2], listStations[3], 3),
                new Arc<>(listStations[3], listStations[4], 4),
                new Arc<>(listStations[4], listStations[5], 3),
                new Arc<>(listStations[5], listStations[6], 2),
                new Arc<>(listStations[6], listStations[7], 2)
        };

        //System.out.println(listArcs[3].getNodeFrom().getName());
    }

    private static void initRoutes() {
        routeForward = new Route<>(listStations[0], "Forward", listArcs);
        routeBack = new Route<>(listStations[7], "Back", listArcs[6], listArcs[5], listArcs[4], listArcs[3], listArcs[2], listArcs[1], listArcs[0]);

        for (int i = 0; i < listStations.length; i++) {
            for (int j = i + 1; j < listStations.length; j++)
                listStations[i].initTargetStations(true, listStations[j].getID());

            for (int j = 0; j < i; j++)
                listStations[i].initTargetStations(false, listStations[j].getID());
        }

        System.out.println(routeForward);
        System.out.println(routeBack);
    }

    private static void serializeTimeTable() {
        try (XMLEncoder xmlEncoder = new XMLEncoder(new FileOutputStream("./src/main/resources/timetable.xml"))) {
            xmlEncoder.writeObject(timeTable);
            xmlEncoder.flush();
        } catch (IOException exc) {
            exc.printStackTrace();
        }
    }

    private static void deserializeTimeTable() {
        try (XMLDecoder xmlDecoder = new XMLDecoder(new FileInputStream("./src/main/resources/timetable.xml"))) {
            timeTable = (TimeTable)xmlDecoder.readObject();
        } catch(IOException exc) {
            exc.printStackTrace();
        }
    }

    private static void initTrafficDispatcher() {
        trafficDispatcher = new TrafficDispatcher(timeTable, routeForward, routeBack);
    }

    private static void initSimulator() {
        Calendar calendar = Calendar.getInstance();

        calendar.set(YEAR, MONTH, DAY, hourFrom, 0, 0);
        Date timeFrom = calendar.getTime();

        calendar.set(YEAR, MONTH, DAY, houtTo, 0, 0);
        Date timeTo = calendar.getTime();

        simulator = new Simulator(timeFrom, timeTo, trafficDispatcher);
    }

    private static void initSheduler() {
        scheduler = new Scheduler(listStations);
    }
}
