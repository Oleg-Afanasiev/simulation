import com.telesens.afanasiev.model.identities.Passenger;
import com.telesens.afanasiev.model.helper.DateTimeHelper;
import com.telesens.afanasiev.model.identities.PassengerGenerationTask;
import com.telesens.afanasiev.model.rules.PassengerGenerationRules;
import com.telesens.afanasiev.model.rules.PassengerTargetSpreading;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by oleg on 1/6/16.
 */
public class TestPassengerGenerationTask {

    private PassengerGenerationRules passGenerationRules;
    private PassengerTargetSpreading passTargetSpreading;
    private Date timeFrom;

    @Before
    public void init() {
        Calendar calendar = new GregorianCalendar();

        calendar.set(2015, 1, 11, 8, 0);
        timeFrom = calendar.getTime();
    }

    @Test
    public void testIsActual() {
        passGenerationRules = new PassengerGenerationRules();
        passTargetSpreading = new PassengerTargetSpreading();

        passTargetSpreading.addTarget(5, 3);
        passTargetSpreading.addTarget(8, 7);
        passGenerationRules.addRule(1, timeFrom, 60, 0, 5, passTargetSpreading);

        passTargetSpreading = new PassengerTargetSpreading();
        passTargetSpreading.addTarget(5, 1);
        passTargetSpreading.addTarget(8, 1);
        passGenerationRules.addRule(1, 60, 100, 4, passTargetSpreading);

        PassengerGenerationTask passGenerationTask1 = passGenerationRules.pollRuleForStation(1);
        PassengerGenerationTask passGenerationTask2 = passGenerationRules.pollRuleForStation(1);

        Date time;
        for (int m = 0; m < 60; m++) {
            time = DateTimeHelper.incMinutes(timeFrom, m);
            assertEquals(true, passGenerationTask1.isActual(time));
            assertEquals(false, passGenerationTask2.isActual(time));
        }

        for (int m = 60; m < 120; m++) {
            time = DateTimeHelper.incMinutes(timeFrom, m);
            assertEquals(false, passGenerationTask1.isActual(time));
            assertEquals(true, passGenerationTask2.isActual(time));
        }

        for (int m = 120; m < 200; m++) {
            time = DateTimeHelper.incMinutes(timeFrom, m);
            assertEquals(false, passGenerationTask1.isActual(time));
            assertEquals(false, passGenerationTask2.isActual(time));
        }
     }

    @Test
    public void testGetPassengersCount() {
        passGenerationRules = new PassengerGenerationRules();
        passTargetSpreading = new PassengerTargetSpreading();

        passTargetSpreading.addTarget(5, 3);
        passTargetSpreading.addTarget(8, 7);
        passGenerationRules.addRule(1, timeFrom, 60, 100_003, 5, passTargetSpreading);

        passTargetSpreading = new PassengerTargetSpreading();
        passTargetSpreading.addTarget(5, 1);
        passTargetSpreading.addTarget(8, 1);
        passGenerationRules.addRule(1, 45, 170_002, 4, passTargetSpreading);

        passTargetSpreading = new PassengerTargetSpreading();
        passTargetSpreading.addTarget(5, 1);
        passTargetSpreading.addTarget(8, 10);
        passGenerationRules.addRule(1, DateTimeHelper.incMinutes(timeFrom, 105), 30, 50_001, 4, passTargetSpreading);

        int passCount = 0;
        PassengerGenerationTask passGenerationTask = passGenerationRules.pollRuleForStation(1);
        Date time;

        for (int m = 0; m < 60; m++) {
            time = DateTimeHelper.incMinutes(timeFrom, m);
            passCount += passGenerationTask.getPassengers(time).size();
        }

        assertEquals(100_003, passCount);

        passCount = 0;
        passGenerationTask = passGenerationRules.pollRuleForStation(1);

        for (int m = 60; m < 105; m++) {
            time = DateTimeHelper.incMinutes(timeFrom, m);
            passCount += passGenerationTask.getPassengers(time).size();
        }

        assertEquals(170_002, passCount);

        passCount = 0;
        passGenerationTask = passGenerationRules.pollRuleForStation(1);

        for (int m = 105; m < 135; m++) {
            time = DateTimeHelper.incMinutes(timeFrom, m);
            passCount += passGenerationTask.getPassengers(time).size();
        }

        assertEquals(50_001, passCount);
    }

    @Test
    public void testGetPassengersSpreading() {
        passGenerationRules = new PassengerGenerationRules();
        passTargetSpreading = new PassengerTargetSpreading();

        passTargetSpreading.addTarget(5, 3);
        passTargetSpreading.addTarget(8, 7);
        passGenerationRules.addRule(1, timeFrom, 60, 1_000_000, 5, passTargetSpreading);

        passTargetSpreading = new PassengerTargetSpreading();
        passTargetSpreading.addTarget(5, 1);
        passTargetSpreading.addTarget(8, 1);
        passGenerationRules.addRule(1, 45, 1_700_000, 4, passTargetSpreading);

        passTargetSpreading = new PassengerTargetSpreading();
        passTargetSpreading.addTarget(5, 1);
        passTargetSpreading.addTarget(8, 10);
        passTargetSpreading.addTarget(6, 4);
        passGenerationRules.addRule(1, DateTimeHelper.incMinutes(timeFrom, 105), 30, 5_000_000, 4, passTargetSpreading);

        int passCount = 0;
        PassengerGenerationTask passGenerationTask = passGenerationRules.pollRuleForStation(1);
        Date time;
        double passCountTarget5 = 0;
        double passCountTarget8 = 0;

        for (int m = 0; m < 60; m++) {
            time = DateTimeHelper.incMinutes(timeFrom, m);
            for (Passenger passenger : passGenerationTask.getPassengers(time)) {
                if (passenger.getStationTargetId() == 5)
                    passCountTarget5++;
                else if (passenger.getStationTargetId() == 8)
                    passCountTarget8++;
            }
        }

        double diff = Math.abs((double)7 / 3 - passCountTarget8 / passCountTarget5);
        assertEquals(true, diff < 0.1);

        //***
        passGenerationTask = passGenerationRules.pollRuleForStation(1);
        passCountTarget5 = 0;
        passCountTarget8 = 0;

        for (int m = 60; m < 105; m++) {
            time = DateTimeHelper.incMinutes(timeFrom, m);
            for (Passenger passenger : passGenerationTask.getPassengers(time)) {
                if (passenger.getStationTargetId() == 5)
                    passCountTarget5++;
                else if (passenger.getStationTargetId() == 8)
                    passCountTarget8++;
            }
        }

        diff = Math.abs(1 - passCountTarget8 / passCountTarget5);
        assertEquals(true, diff < 0.1);

        //***
        passGenerationTask = passGenerationRules.pollRuleForStation(1);
        passCountTarget5 = 0;
        passCountTarget8 = 0;
        int passCountTarget6 = 0;

        for (int m = 105; m < 135; m++) {
            time = DateTimeHelper.incMinutes(timeFrom, m);
            for (Passenger passenger : passGenerationTask.getPassengers(time)) {
                if (passenger.getStationTargetId() == 5)
                    passCountTarget5++;
                else if (passenger.getStationTargetId() == 8)
                    passCountTarget8++;
                else if (passenger.getStationTargetId() == 6)
                    passCountTarget6++;
            }
        }

        diff = Math.abs(10 - passCountTarget8 / passCountTarget5);
        assertEquals(true, diff < 0.1);

        diff = Math.abs(4 - passCountTarget6 / passCountTarget5);
        assertEquals(true, diff < 0.1);

        diff = Math.abs((double)10/4 - passCountTarget8 / passCountTarget6);
        assertEquals(true, diff < 0.1);
    }

    @Test
    public void testGetPassengersByMinutes() {
        passGenerationRules = new PassengerGenerationRules();
        passTargetSpreading = new PassengerTargetSpreading();

        passTargetSpreading.addTarget(5, 3);
        passTargetSpreading.addTarget(8, 7);

        passGenerationRules.addRule(1, timeFrom, 1, 1, 4, passTargetSpreading);

        for (int i = 2; i <= 100; i++)
            passGenerationRules.addRule(1, 1, i, 4, passTargetSpreading);

        int passCount = 0;
        PassengerGenerationTask passGenerationTask;
        Date time;

        for (int m = 0; m < 100; m++) {
            passGenerationTask = passGenerationRules.pollRuleForStation(1);
            time = DateTimeHelper.incMinutes(timeFrom, m);
            passCount = passGenerationTask.getPassengers(time).size();

            assertEquals(m + 1, passCount);
        }
    }
}
