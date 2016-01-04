package com.telesens.afanasiev.simulator;

import com.telesens.afanasiev.helper.DateTimeHelper;
import com.telesens.afanasiev.loader.TimeTable;

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
    private static TimeTable timeTable;
    private static Date timeFrom;
    private static Date timeTo;

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {

        initFromTo();

        initTimeTable();
        serializationTimeTable(timeTable);
        timeTable = deserializationTimeTable();
        simulator = new BusTrafficSimulator();
        simulator.loadData(timeTable);
        simulator.start(timeFrom, timeTo);
        //System.out.println(simulator.allRoutesToString());
    }

    private static void initFromTo() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2015, 11, 1, 7, 0, 0);
        timeFrom = DateTimeHelper.roundByMinutes(calendar.getTime());
        timeTo = DateTimeHelper.incHours(timeFrom, 2);
    }

    private static void initTimeTable() {
        timeTable = new TimeTable();
        int runInterval = 7;
        Date iTime;

        for (int m = 0; m < 120; m+= runInterval) {
            iTime = DateTimeHelper.incMinutes(timeFrom, m);
            timeTable.addTask(1L, iTime, 5, 0);
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

    private  static TimeTable deserializationTimeTable() {
        TimeTable timeTable = null;
        try (XMLDecoder xmlDecoder = new XMLDecoder(new FileInputStream("./src/main/resources/xml/timetable.xml"))) {
            timeTable = (TimeTable)xmlDecoder.readObject();
        } catch(FileNotFoundException exc) {
            exc.printStackTrace();
        }

        return timeTable;
    }
}
