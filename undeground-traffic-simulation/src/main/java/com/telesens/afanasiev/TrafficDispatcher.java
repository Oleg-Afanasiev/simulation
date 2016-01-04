package com.telesens.afanasiev;

import java.util.*;

/**
 * Created by oleg on 12/23/15.
 */
public class TrafficDispatcher implements Observer {

    private ReportCollector reportCollector;
    private TimeTable timeTable;
    private Route routeForward;
    private Route routeBack;
    private int breakDuration = 5;
    private int stopDuration = 1;

    private long trainID = 1;

    private List<TrainRunning> trainsInRunning;

    public TrafficDispatcher(TimeTable timeTable, Route routeForward, Route routeBack) {
        this.timeTable = timeTable;
        this.routeForward = routeForward;
        this.routeBack = routeBack;

        reportCollector = ReportCollector.getInstance();
        trainsInRunning = new ArrayList<>();
    }

    public void tick(Date nextTime) {
        checkNextTaskByTimeTable(nextTime);
        informTickTrains(nextTime);
    }

    private void checkNextTaskByTimeTable(Date nextTime) {
        if (timeTable.isExistNextTask())
            if (Math.abs(timeTable.getTimeForNextTask().getTime() - nextTime.getTime()) < 10000)
                initNewRun(nextTime);
    }

    private void informTickTrains(Date nextTime) {
        Iterator<TrainRunning> iterator = trainsInRunning.iterator();
        TrainRunning trainRunning;
        while (iterator.hasNext()) {
            trainRunning = iterator.next();
            trainRunning.tick(nextTime);

            if (trainRunning.isCompleted())
                iterator.remove();
        }
    }

    private void initNewRun(Date nextTime) {
        TrainTask task = timeTable.poolTask();
        int trainNumber = new Random().nextInt(8999) + 1000;
        TrainRunning trainRunning = new TrainRunning(routeForward, routeBack,
                        new Train(trainID++, trainNumber), breakDuration, stopDuration);

        trainsInRunning.add(trainRunning);
        trainRunning.start(nextTime);
        reportCollector.sendMessage("Dispatcher", String.format("Кол-во поездов в рейсах: %d", trainsInRunning.size()));
    }
}
