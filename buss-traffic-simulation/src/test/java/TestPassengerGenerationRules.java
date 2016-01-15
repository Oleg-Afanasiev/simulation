import com.telesens.afanasiev.model.helper.DateTimeHelper;
import com.telesens.afanasiev.model.rules.PassengerGenerationRules;
import com.telesens.afanasiev.model.rules.PassengerTargetSpreading;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by oleg on 1/6/16.
 */
public class TestPassengerGenerationRules {

    private PassengerGenerationRules passGenerationRules;
    private Date timeFrom;

    @Before
    public void init() {
        passGenerationRules = new PassengerGenerationRules();
        Calendar calendar = new GregorianCalendar();
        calendar.set(2015, 4, 7, 8, 15);
        timeFrom = calendar.getTime();
    }

    @Test (expected = UnsupportedOperationException.class)
    public void testAddRule1() {
        long stationId = 1;
        int duration = 1;
        int passCount = 1;
        int limitWaiting = 1;
        PassengerTargetSpreading passTargetSpreading = new PassengerTargetSpreading();

        passGenerationRules.addRule(stationId, duration, passCount, limitWaiting, passTargetSpreading);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testAddRule2() {
        long stationId = 1;
        int duration = 1;
        int passCount = 1;
        int limitWaiting = 1;
        PassengerTargetSpreading passTargetSpreading = new PassengerTargetSpreading();

        passGenerationRules.addRule(stationId, timeFrom, duration, passCount, limitWaiting, passTargetSpreading);
        passGenerationRules.addRule(stationId, DateTimeHelper.incMinutes(timeFrom, duration + 1), duration, passCount, limitWaiting, passTargetSpreading);
    }

    @Test
    public void testAddRule() {
        long stationId = 1;
        int duration = 1;
        int passCount = 1;
        int limitWaiting = 1;
        PassengerTargetSpreading passTargetSpreading = new PassengerTargetSpreading();

        passGenerationRules.addRule(stationId, timeFrom, duration, passCount, limitWaiting, passTargetSpreading);
        passGenerationRules.addRule(stationId, DateTimeHelper.incMinutes(timeFrom, duration), duration, passCount, limitWaiting, passTargetSpreading);
        passGenerationRules.addRule(stationId, duration, passCount, limitWaiting, passTargetSpreading);
        passGenerationRules.addRule(stationId, DateTimeHelper.incMinutes(timeFrom, 3 * duration), duration, passCount, limitWaiting, passTargetSpreading);
        passGenerationRules.addRule(stationId, duration, passCount, limitWaiting, passTargetSpreading);
        passGenerationRules.addRule(stationId, DateTimeHelper.incMinutes(timeFrom, 5 * duration), duration, passCount, limitWaiting, passTargetSpreading);
    }
}
