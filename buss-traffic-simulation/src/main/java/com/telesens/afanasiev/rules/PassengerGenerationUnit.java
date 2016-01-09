package com.telesens.afanasiev.rules;

import com.telesens.afanasiev.helper.DateTimeHelper;
import com.telesens.afanasiev.simulator.Passenger;

import java.io.Serializable;
import java.util.*;

/**
 * Created by oleg on 1/5/16.
 */
public class PassengerGenerationUnit implements Serializable{
    private static final long serialVersionUID = 1L;

    private long stationId;
    private Date timeFrom;  // 'timeFrom' - inclusive
    private int duration; // 'timeFrom' + 'duration' - exclusive
    private int passCount;
    private int minutesLimitWaiting; //how much minutes
    private PassengerTargetSpreading passTargetSpreading;

    private transient final int TIME_GEN_MAX_STEP = 3; // minutes
    private transient Map<Integer, Collection<Passenger>> passTimeSpreading;

    public PassengerGenerationUnit() {

    }

    public PassengerGenerationUnit(long stationId, Date timeFrom, int duration, int passCount, int minutesLimitWaiting, PassengerTargetSpreading passTargetSpreading) {
        this.stationId = stationId;
        this.timeFrom = timeFrom;
        this.duration = duration;
        this.passCount = passCount;
        this.minutesLimitWaiting = minutesLimitWaiting;
        this.passTargetSpreading = passTargetSpreading;
    }

    public Date getTimeFrom() {
        return timeFrom;
    }

    public void setTimeFrom(Date timeFrom) {
        this.timeFrom = timeFrom;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getPassCount() {
        return passCount;
    }

    public void setPassCount(int passCount) {
        this.passCount = passCount;
    }

    public int getMinutesLimitWaiting() {
        return minutesLimitWaiting;
    }

    public void setMinutesLimitWaiting(int minutesLimitWaiting) {
        this.minutesLimitWaiting = minutesLimitWaiting;
    }

    public long getStationId() {
        return stationId;
    }

    public void setStationId(long stationId) {
        this.stationId = stationId;
    }

    public PassengerTargetSpreading getPassTargetStations() {
        return passTargetSpreading;
    }

    public void setPassTargetStations(PassengerTargetSpreading passTargetStations) {
        this.passTargetSpreading = passTargetStations;
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
            /*  we can query to get passengers more than one time for same station (in case of 'circular' route),
                because first and last stations can be same. We need to clear list of passengers after getting.
             */
            passTimeSpreading.put(diffMinutes, new ArrayList<>());
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
