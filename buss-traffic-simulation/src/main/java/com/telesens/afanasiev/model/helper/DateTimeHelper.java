package com.telesens.afanasiev.model.helper;

import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;

import javax.xml.datatype.XMLGregorianCalendar;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by oleg on 12/5/15.
 */
public class DateTimeHelper {

    private static final long ONE_MINUTE_IN_MILLIS = 60000; //milliseconds

    public static String toString(Date dateTime) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy hh:mm");
        return dateFormat.format(dateTime);
    }

    public static Date incMinute(Date dateTime) {
        return  incMinutes(dateTime, 1);
    }

    public static Date incMinutes(Date dateTime, int nMinutes) {
        return  new Date(dateTime.getTime() + nMinutes * ONE_MINUTE_IN_MILLIS);
    }

    public static Date incHour(Date dateTime) {
        return incHours(dateTime, 1);
    }

    public static Date incHours(Date dateTime, int nHours) {
        return  incMinutes(dateTime, 60 * nHours);
    }

    public static Date incDay(Date dateTime) {
        return incDays(dateTime, 1);
    }

    public static Date incDays(Date dateTime, int nDays) {
        return incHours(dateTime, 24 * nDays);
    }

    public static Date incWeek(Date dateTime) {
        return incDays(dateTime, 7);
    }

    public static Date incYears(Date dateTime, int nYears) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateTime);
        calendar.add(Calendar.YEAR, nYears);

        return calendar.getTime();
    }

    public static Date incYear(Date dateTime) {
        return incYears(dateTime, 1);
    }

    public static int diffMinutes(Date dtFrom , Date dtTo) {
        Calendar calendarFrom = Calendar.getInstance();
        calendarFrom.setTime(dtFrom);

        Calendar calendarTo = Calendar.getInstance();
        calendarTo.setTime(dtTo);

        long diff = calendarTo.getTimeInMillis() - calendarFrom.getTimeInMillis();

        return (int)(diff / (1000 * 60));
    }

    public static Date roundByMinutes(Date dateTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateTime);
        calendar.clear(Calendar.SECOND);
        calendar.clear(Calendar.MILLISECOND);

        return calendar.getTime();
    }

    public static XMLGregorianCalendar dateToXMLGregorianCalendar(Date dateTime) {

        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTime(dateTime);

        return new XMLGregorianCalendarImpl(gregorianCalendar);
    }

    public static Date XMLGregorianCalendarToDate(XMLGregorianCalendar xmlGregorianCalendar) {
        return xmlGregorianCalendar.toGregorianCalendar().getTime();
    }
}
