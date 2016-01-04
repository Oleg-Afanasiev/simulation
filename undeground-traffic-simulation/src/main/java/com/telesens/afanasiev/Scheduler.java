package com.telesens.afanasiev;

import java.util.Random;

/**
 * Created by oleg on 12/26/15.
 */
public class Scheduler implements Runnable {

    class NextCommingPass {
        private int remainTime;
        private int passCount;
        private long targetStationId;

        public NextCommingPass(long targetStationId, int remainTime, int passCount) {
            this.targetStationId = targetStationId;
            this.remainTime = remainTime;
            this.passCount = passCount;
        }

        public long getTargetStationId() {
            return targetStationId;
        }

        public void setTargetStationId(long targetStationId) {
            this.targetStationId = targetStationId;
        }

        public int getRemainTime() {
            return remainTime;
        }

        public void setRemainTime(int remainTime) {
            this.remainTime = remainTime;
        }

        public int getPassCount() {
            return passCount;
        }

        public void setPassCount(int passCount) {
            this.passCount = passCount;
        }

        public void tick() {
            remainTime--;

            if (remainTime < 0)
                throw new IllegalArgumentException("Incorrect 'remainTime' into 'NextCommingPass' class");
        }
    }

    private ReportCollector reportCollector;
    private final int DELTA_TIME = 100; // 1 minute
    private Station[] listStations;
    private boolean isFinish = false;
    private NextCommingPass[] nextCommingPassengers;

    public Scheduler(Station... stations) {
        reportCollector = ReportCollector.getInstance();
        listStations = new Station[stations.length];
        nextCommingPassengers = new NextCommingPass[stations.length];

        for (int i = 0; i < stations.length; i++) {
            listStations[i] = stations[i];
            nextCommingPassengers[i] = new NextCommingPass(0L, 1, 0);
        }
    }

    public void finish() {
        isFinish = true;
    }

    @Override
    public void run() {
        reportCollector.sendMessage("Scheduler of passengers", "Планировщик начал генерировать поток пассажиров");
        while(!isFinish) {
            try {
                sentTick();
                generatePassengers();
                Thread.sleep(DELTA_TIME);
            } catch (InterruptedException exc) {
                exc.printStackTrace();
            }

        }

        reportCollector.sendMessage("Scheduler of passengers", "Планировщик завершил работу");
    }

    private void sentTick() {
        for (int i = 0; i < nextCommingPassengers.length; i++)
            nextCommingPassengers[i].tick();
    }

    private void generatePassengers() {
        Random random = new Random();
        int passCount;
        int nextTime;
        long targetStationId = 1;

        for (int i = 0; i < listStations.length; i++) {
            if (nextCommingPassengers[i].getRemainTime() == 0) {
                if (nextCommingPassengers[i].getPassCount() > 0) {
                    listStations[i].arrivePassengers(targetStationId, nextCommingPassengers[i].getPassCount());
                    reportCollector.sendMessage("Sheduler",
                            String.format("На станцию \"%s\" прибыло %d пассажиров, едут на \"%s\"",
                                    listStations[i].getName(), nextCommingPassengers[i].getPassCount(),
                                    getStationById(nextCommingPassengers[i].getTargetStationId())));
                }

                nextTime = 1 + random.nextInt(5);
                passCount = nextTime * listStations[i].getPassengersCountInHour() / 60;
                targetStationId = generateTargetId(listStations[i].getID());

                nextCommingPassengers[i].setRemainTime(nextTime);
                nextCommingPassengers[i].setPassCount(passCount);
                nextCommingPassengers[i].setTargetStationId(targetStationId);
            }
        }
    }

    private long generateTargetId(long idStationFrom) {
        Random random = new Random();
        int i;

        do {
            i = random.nextInt(listStations.length - 1);
        } while(listStations[i].getID() == idStationFrom);

        return listStations[i].getID();
    }

    private Station getStationById(long id) {
        for (int i = 0; i < listStations.length; i++)
            if (listStations[i].getID() == id)
                return listStations[i];

        return null;
    }
}
