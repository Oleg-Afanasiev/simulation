import com.telesens.afanasiev.helper.DateTimeHelper;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import static junit.framework.Assert.*;

/**
 * Created by oleg on 1/1/16.
 */
public class TestDateTimeHelper {
    private Random random;

    @Before
    public void init() {
        random = new Random();
    }

    @Test
    public void testIncMinute(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(2015, 10, 30, 10, 1);
        Date dateTime = calendar.getTime();
        calendar.set(2015, 10, 30, 10, 2);
        assertEquals(calendar.getTime(), DateTimeHelper.incMinute(dateTime));

        calendar.set(2015, 11, 31, 23, 59);
        dateTime = calendar.getTime();
        calendar.set(2016, 0, 1, 0, 0);
        assertEquals(calendar.getTime(), DateTimeHelper.incMinute(dateTime));

        calendar.set(2015, 11, 31, 23, 59);
        dateTime = calendar.getTime();
        calendar.set(2015, 11, 31, 23, 58);
        assertEquals(calendar.getTime(), DateTimeHelper.incMinutes(dateTime, -1));

        calendar.set(2016, 0, 1, 0, 0);
        dateTime = calendar.getTime();
        calendar.set(2015, 11, 31, 23, 59);
        assertEquals(calendar.getTime(), DateTimeHelper.incMinutes(dateTime, -1));
    }

    @Test
    public void testIncMinutes() {
        final int N = 10000;
        int increment;

        Calendar calendar = Calendar.getInstance();
        Date dateTime;

        for (int i = 0; i < N; i++) {
            calendar.set(2015, 10, 30, 10, 1);
            dateTime = calendar.getTime();

            increment = random.nextInt(60);
            calendar.set(2015, 10, 30, 10, 1 + increment);
            assertEquals(calendar.getTime(), DateTimeHelper.incMinutes(dateTime, increment));

            calendar.set(2015, 11, 31, 23, 59);
            dateTime = calendar.getTime();

            calendar.set(2015, 11, 31, 23, 59 + increment);
            assertEquals(calendar.getTime(), DateTimeHelper.incMinutes(dateTime, increment));
        }
    }

    @Test
    public void testIncHour() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2015, 10, 30, 10, 0);
        Date dateTime = calendar.getTime();
        calendar.set(2015, 10, 30, 11, 0);
        assertEquals(calendar.getTime(), DateTimeHelper.incHour(dateTime));

        calendar.set(2015, 11, 31, 23, 59);
        dateTime = calendar.getTime();
        calendar.set(2016, 0, 1, 0, 59);
        assertEquals(calendar.getTime(), DateTimeHelper.incHour(dateTime));
    }

    @Test
    public void testIncHours() {
        final int N = 10000;
        int increment;

        Calendar calendar = Calendar.getInstance();
        Date dateTime;

        for (int i = 0; i < N; i++) {
            calendar.set(2015, 10, 30, 10, 1);
            dateTime = calendar.getTime();

            increment = random.nextInt(24);
            calendar.set(2015, 10, 30, 10 + increment, 1);
            assertEquals(calendar.getTime(), DateTimeHelper.incHours(dateTime, increment));

            calendar.set(2015, 11, 31, 23, 59);
            dateTime = calendar.getTime();

            calendar.set(2015, 11, 31, 23 + increment, 59);
            assertEquals(calendar.getTime(), DateTimeHelper.incHours(dateTime, increment));
        }
    }

    @Test
    public void testIncDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2015, 10, 29, 10, 0);
        Date dateTime = calendar.getTime();
        calendar.set(2015, 10, 30, 10, 0);
        assertEquals(calendar.getTime(), DateTimeHelper.incDay(dateTime));

        calendar.set(2015, 11, 31, 23, 59);
        dateTime = calendar.getTime();
        calendar.set(2016, 0, 1, 23, 59);
        assertEquals(calendar.getTime(), DateTimeHelper.incDay(dateTime));
    }

    @Test
    public void testIncDays() {
        final int N = 10000;
        int increment;

        Calendar calendar = Calendar.getInstance();
        Date dateTime;

        for (int i = 0; i < N; i++) {
            calendar.set(2015, 10, 29, 10, 1);
            dateTime = calendar.getTime();

            increment = random.nextInt(60);
            calendar.set(2015, 10, 29 + increment, 10, 1);
            assertEquals(calendar.getTime(), DateTimeHelper.incDays(dateTime, increment));

            calendar.set(2015, 11, 31, 23, 59);
            dateTime = calendar.getTime();

            calendar.set(2015, 11, 31 + increment, 23, 59);
            assertEquals(calendar.getTime(), DateTimeHelper.incDays(dateTime, increment));
        }
    }

    @Test
    public void testIncWeek() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2015, 10, 22, 10, 0);
        Date dateTime = calendar.getTime();
        calendar.set(2015, 10, 29, 10, 0);
        assertEquals(calendar.getTime(), DateTimeHelper.incWeek(dateTime));

        calendar.set(2015, 11, 31, 23, 59);
        dateTime = calendar.getTime();
        calendar.set(2016, 0, 7, 23, 59);
        assertEquals(calendar.getTime(), DateTimeHelper.incWeek(dateTime));
    }

    @Test
    public void testIncYear() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2015, 10, 29, 10, 0);
        Date dateTime = calendar.getTime();
        calendar.set(2016, 10, 29, 10, 0);
        assertEquals(calendar.getTime(), DateTimeHelper.incYear(dateTime));

        calendar.set(2015, 11, 31, 23, 59);
        dateTime = calendar.getTime();
        calendar.set(2016, 11, 31, 23, 59);
        assertEquals(calendar.getTime(), DateTimeHelper.incYear(dateTime));
    }

    @Test
    public void testIncYears() {
        final int N = 10000;
        int increment;

        Calendar calendar = Calendar.getInstance();
        Date dateTime;

        for (int i = 0; i < N; i++) {
            calendar.set(2015, 10, 29, 10, 1);
            dateTime = calendar.getTime();

            increment = random.nextInt(500);
            calendar.set(2015 + increment, 10, 29, 10, 1);
            assertEquals(calendar.getTime(), DateTimeHelper.incYears(dateTime, increment));

            calendar.set(2015, 11, 31, 23, 59);
            dateTime = calendar.getTime();

            calendar.set(2015 + increment, 11, 31, 23, 59);
            assertEquals(calendar.getTime(), DateTimeHelper.incYears(dateTime, increment));
        }
    }

    @Test
    public void testDiffMinutes() {
        Calendar calendar = Calendar.getInstance();
        Date timeFrom;
        Date timeTo;

        calendar.set(2015, 6, 20, 4, 25);
        timeFrom = calendar.getTime();

        calendar.set(2015, 6, 20, 4, 30);
        timeTo = calendar.getTime();

        assertEquals(5, DateTimeHelper.diffMinutes(timeFrom, timeTo));

        calendar.set(2015, 6, 20, 4, 30);
        timeFrom = calendar.getTime();

        calendar.set(2015, 6, 20, 5, 30);
        timeTo = calendar.getTime();

        assertEquals(60, DateTimeHelper.diffMinutes(timeFrom, timeTo));

        calendar.set(2015, 6, 20, 4, 30);
        timeFrom = calendar.getTime();

        calendar.set(2015, 6, 21, 4, 30);
        timeTo = calendar.getTime();

        assertEquals(60 * 24, DateTimeHelper.diffMinutes(timeFrom, timeTo));

        calendar.set(2015, 6, 20, 4, 30, 5);
        timeFrom = calendar.getTime();

        calendar.set(2015, 6, 20, 4, 30, 25);
        timeTo = calendar.getTime();

        assertEquals(0, DateTimeHelper.diffMinutes(timeFrom, timeTo));
        assertEquals(0, DateTimeHelper.diffMinutes(timeTo, timeFrom));
    }

    @Test
    public void testRoundByMinutes() {
        final int N = 10000;
        int delta;

        Calendar calendar = Calendar.getInstance();
        Date time;

        for (int i = 0; i < N; i++) {
            delta = random.nextInt(59);
            calendar.set(2015, 1, 11, 13, 23, delta);
            time = calendar.getTime();

            calendar.set(2015, 1, 11, 13, 23, 0);
            calendar.clear(Calendar.MILLISECOND);
            assertEquals(calendar.getTime(), DateTimeHelper.roundByMinutes(time));
        }
    }
}
