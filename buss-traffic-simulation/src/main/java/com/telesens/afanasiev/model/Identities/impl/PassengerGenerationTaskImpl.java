package com.telesens.afanasiev.model.Identities.impl;

import com.telesens.afanasiev.model.helper.DateTimeHelper;
import com.telesens.afanasiev.model.Identities.PassengerGenerationTask;
import com.telesens.afanasiev.model.Identities.Identity;
import com.telesens.afanasiev.model.Identities.Passenger;
import com.telesens.afanasiev.model.rules.PassengerTargetSpreading;
import lombok.*;

import java.util.*;

/**
 * Created by oleg on 1/5/16.
 */
@NoArgsConstructor
public class PassengerGenerationTaskImpl extends IdentityImpl implements PassengerGenerationTask, Identity {
    private static final long serialVersionUID = 1L;

    @Getter
    @Setter
    private long stationId;
    @Getter
    @Setter
    private Date timeFrom;  // 'timeFrom' - inclusive
    @Getter
    @Setter
    private int duration; // 'timeFrom' + 'duration' - exclusive
    @Getter
    @Setter
    private int passCount;
    @Getter
    @Setter
    private int minutesLimitWaiting; //how much minutes
    @Getter
    @Setter
    private PassengerTargetSpreading passTargetSpreading;

    private transient final int TIME_GEN_MAX_STEP = 3; // minutes
    private transient Map<Integer, Collection<Passenger>> passTimeSpreading;

    public PassengerGenerationTaskImpl(long stationId, Date timeFrom, int duration, int passCount, int minutesLimitWaiting, PassengerTargetSpreading passTargetSpreading) {
        this.stationId = stationId;
        this.timeFrom = timeFrom;
        this.duration = duration;
        this.passCount = passCount;
        this.minutesLimitWaiting = minutesLimitWaiting;
        this.passTargetSpreading = passTargetSpreading;
    }

    public boolean isActual(Date curTime) {
        int diff = DateTimeHelper.diffMinutes(timeFrom, curTime);

        return (diff < duration) && (diff >= 0);
    }

    public Collection<Passenger> getPassengers(Date curTime) {
        Collection<Passenger> passengers;

        if (passTimeSpreading == null) {
            passTimeSpreading = new HashMap<>();
            generatePassengers();
        }

        int diffMinutes = DateTimeHelper.diffMinutes(timeFrom, curTime);

        if (!passTimeSpreading.containsKey(diffMinutes))
            passengers = new ArrayList<>();
        else {
            passengers = passTimeSpreading.get(diffMinutes);
            /*  we can query to get passengers more than one time for same station (in case of 'circular' route)
                because first and last stations can be same. So we need to clear list of passengers after getting them.
             */
            passTimeSpreading.put(diffMinutes, new ArrayList<>()); // clearing
        }

        return passengers;
    }

    private void generatePassengers() {
        Random random = new Random();

        int nextTime;
        long targetId;
        for (int i = passCount; i > 0; i--) {
            nextTime = random.nextInt(duration);
            targetId = passTargetSpreading.generateTargetId();
            if (passTimeSpreading.get(nextTime) == null)
                passTimeSpreading.put(nextTime, new ArrayList<>());

            passTimeSpreading.get(nextTime).add(new Passenger(stationId, targetId, minutesLimitWaiting));
        }
    }
}
