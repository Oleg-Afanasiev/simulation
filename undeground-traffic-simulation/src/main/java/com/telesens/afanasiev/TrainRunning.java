package com.telesens.afanasiev;

import java.util.*;

/**
 * Created by oleg on 12/23/15.
 */
public class TrainRunning implements Observer {
    enum Phase  {
        run,
        stopAtStation,
        stopForBreak,
        completed
    }

    private ReportCollector reportCollector;

    private Train train;
    private Route<Station> routeForward;
    private Route<Station> routeBack;
    private Route<Station> currentRoute;
    private boolean isRunForward;

    private int breakDuration;
    private int stopDuration;

    private Phase phase;

    private int remainTime;
    private Iterator<Arc<Station>> arcIterator;
    private Station nextStation;

    public TrainRunning(Route<Station> routeForward, Route<Station> routeBack, Train train, int breakDuration, int stopDuration) {
        this.breakDuration = breakDuration;
        this.stopDuration = stopDuration;
        this.routeForward = routeForward;
        this.routeBack = routeBack;
        this.train = train;

        reportCollector = ReportCollector.getInstance();
    }

    public Train getTrain() {
        return train;
    }

    public boolean isCompleted() {
        return phase == Phase.completed;
    }

    public void start(Date curTime) {
        currentRoute = routeForward;
        isRunForward = true;
        arcIterator = currentRoute.iterator();

        nextStation = currentRoute.getFirstNode();
        reportCollector.sendMessage(train.toString(), "<<<Начал рейс >>>");

        phase = Phase.run;
        remainTime = 1;
    }

    public void tick(Date nextTime) {
        remainTime--;

        if (remainTime == 0) {

            switch (phase) {
                case run:
                    stopAtStation(nextStation);
                    break;

                case stopForBreak:
                    startMovingBack();
                    break;

                case stopAtStation:
                    moveToNextStation();
                    break;
            }
        }
    }

    private void stopAtStation(Station station) {
        phase = Phase.stopAtStation;
        remainTime = stopDuration;

        reportCollector.sendMessage(train.toString(), String.format("Прибыл на остановку \"%s\":", station));

        reportCollector.sendMessage(train.toString(),
                String.format("     кол-во пассажиров на остановке %d, в салоне %d, свободных мест %d.",
                        station.passengersCount(isRunForward), train.getPassengersCount(), train.getFreeSeatsCount()));

        train.debusPassengersOut(station.getID());
        station.arriveTrain(train, isRunForward);

        reportCollector.sendMessage(train.toString(),
                String.format("     кол-во пассажиров на остановке %d, в салоне %d, свободных мест %d.\n",
                        station.passengersCount(isRunForward), train.getPassengersCount(), train.getFreeSeatsCount()));
    }

    private void moveToNextStation() {
        nextStation.arriveTrain(train, isRunForward);

        if (arcIterator.hasNext()) {
            phase = Phase.run;
            Arc<Station> nextArc = arcIterator.next();
            remainTime = nextArc.getDuration();
            nextStation = nextArc.getOppositeNode(nextStation);
            reportCollector.sendMessage(train.toString(),
                    String.format("Начал движение к следующей остановке.\n"));
        } else {
            if (isRunForward)
                stopForBreak();
            else
                finishRun();
        }
    }

    private void stopForBreak() {
        phase = Phase.stopForBreak;
        remainTime = breakDuration;
        currentRoute = routeBack;
        isRunForward = false;

        reportCollector.sendMessage(train.toString(), "<<< Встал на перерыв >>>\n");
    }

    private void startMovingBack() {
        arcIterator = currentRoute.iterator();

        nextStation = currentRoute.getFirstNode();
        reportCollector.sendMessage(train.toString(), "<<<Начал обратный рейс >>>");

        stopAtStation(nextStation);
    }

    private void finishRun() {
        phase = Phase.completed;
        reportCollector.sendMessage(train.toString(),
                String.format("<<< Завершил рейс >>>\n"));
    }
}
