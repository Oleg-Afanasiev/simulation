package com.telesens.afanasiev.model.identities.impl;

import com.telesens.afanasiev.model.identities.Passenger;
import com.telesens.afanasiev.model.helper.DateTimeHelper;
import com.telesens.afanasiev.model.identities.PassengerGenerationTask;
import com.telesens.afanasiev.model.identities.Identity;
import com.telesens.afanasiev.model.rules.PassengerTargetSpreading;
import lombok.*;

import java.util.*;

/**
 * Created by oleg on 1/5/16.
 */
@Data
@NoArgsConstructor
public class PassengerGenerationTaskImpl extends IdentityImpl implements PassengerGenerationTask, Identity {
    private static final long serialVersionUID = 1L;

    private long stationId;
    private Date timeFrom;  // 'timeFrom' - inclusive
    private int duration; // 'timeFrom' + 'duration' - exclusive
    private int passCount;
    private int minutesLimitWaiting; //how much minutes
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

    @Override
    public boolean isActual(Date actualTime) {
        int diff = DateTimeHelper.diffMinutes(timeFrom, actualTime);

        return (diff < duration) && (diff >= 0);
    }

    @Override
    public Collection<Passenger> getPassengers(Date actualTime) {
        Collection<Passenger> passengers;

        if (passTimeSpreading == null) {
            passTimeSpreading = new HashMap<>();
            generatePassengers();
        }

        int diffMinutes = DateTimeHelper.diffMinutes(timeFrom, actualTime);

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

            passTimeSpreading.get(nextTime).add(new PassengerImpl(stationId, targetId, minutesLimitWaiting));
        }
    }
}
