package com.telesens.afanasiev.reporter;

import com.telesens.afanasiev.helper.DateTimeHelper;

import java.util.ArrayDeque;
import java.util.Date;
import java.util.Queue;

/**
 * Created by oleg on 12/17/15.
 */
public class LogCollector implements
        ClockReporter, SimulatorReporter, RouteReporter, PassengerReporter, StationReporter, BusReporter, RunReporter {
    private volatile static LogCollector uniqueInstance;
    private volatile Queue<String> queueOfLogMsg;
    private volatile Queue<BusLogUnit> queueOfBusLogs;
    private volatile Queue<StationLogUnit> queueOfStationLogs;
    private volatile Queue<PassengerLogUnit> queueOfPassLogs;
    private volatile Queue<RunLogUnit> queueOfRunLogs;

    private boolean isFinishSimulation;
    private Date actualTime;

    private LogCollector() {
        queueOfLogMsg = new ArrayDeque<>();
        queueOfBusLogs = new ArrayDeque<>();
        queueOfStationLogs = new ArrayDeque<>();
        queueOfPassLogs = new ArrayDeque<>();
        queueOfRunLogs = new ArrayDeque<>();

        isFinishSimulation = false;
    }

    public static LogCollector getInstance() {
        if (uniqueInstance == null) {
            synchronized (LogCollector.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new LogCollector();
                }
            }
        }

        return uniqueInstance;
    }

    @Override
    public void sendLogTick(Date actualTime) {
        this.actualTime = actualTime;

        sendLogMessage("", "");
        sendLogMessage("Clock -> tick", DateTimeHelper.toString(actualTime));
        sendLogMessage("", "");
    }

    @Override
    public void sendSimulatorLogStart(Date startTime) {
        this.actualTime = startTime;
        queueOfLogMsg.add("\n******************************* НАЧАЛО СИМУЛЯЦИИ *******************************\n\n");

        sendLogMessage("Время начала:", DateTimeHelper.toString(startTime));
        sendLogMessage("", "");
    }

    @Override
    public void sendSimulatorLogFinish() {
        isFinishSimulation = true;
        queueOfLogMsg.add("\n******************************* СИМУЛЯЦИЯ ЗАВЕРШЕНА *******************************\n");
    }

    @Override
    public void sendRouteLog(String routeNumber, int busesRunCount, int busesWaitingCount) {
        sendLogMessage(
                String.format("Маршрут(ы) № %s", routeNumber),
                String.format("Кол-во автобусов: в рейсах %d, освободившихся %d", busesRunCount, busesWaitingCount));
    }

    @Override
    public void sendPassLogDelivered(long passengerId, long stationIdFrom, String stationName, String busNumber, Date timeComeInBus, Date timeGoOffBus) {
        sendLogMessage(String.format("Пассажир [ID: %d]", passengerId),
                String.format("Вышел на остановке \"%s\"", stationName)
        );

        queueOfPassLogs.add(new PassengerLogUnit(passengerId, stationIdFrom, busNumber, timeComeInBus, timeGoOffBus));
    }

    @Override
    public void sendPassLogWelcomeToStation(long passengerId, String passengerName,
                                            long stationFromId, String stationFromName,
                                            long stationTargetId, String stationTargetName) {

        sendLogMessage(String.format("Пассажир %s [ID: %d]", passengerName, passengerId),
                String.format("Пришел на остановку \"%s\". Жду автобус на: \"%s\"", stationFromName, stationTargetName));
    }

    @Override
    public void sendPassLogWelcomeToBus(long passengerId, long stationId, String busNumber) {

    }

    @Override
    public void sendStationLogQueueSize(long stationId, String stationName, int queueSize) {
        sendLogMessage(String.format("Остановка \"%s\" [ID: %d]", stationName, stationId),
                String.format("Кол-во в очереди: %d", queueSize));
    }

    @Override
    public void sendStationLogBusArrived(long stationId, String stationName,
                                         long routeId, String routeNumber, String routeDirect, String busNumber,
                                         int freeSeatsCount, int getOffPassCount, int takeInPassCount, int stayPassCount) {
        sendLogMessage(String.format("Остановка \"%s\" [ID: %d]", stationName, stationId),
                String.format("Прибыл автобус № %s, маршрут № %s - %s, вышло %d, свободных мест %d",
                        busNumber, routeNumber, routeDirect, getOffPassCount, freeSeatsCount));

        sendLogMessage(String.format("Остановка \"%s\" [ID: %d]", stationName, stationId),
                String.format("В автобус № %s сели %d пассажиров, остались на остановке %d", busNumber, takeInPassCount, stayPassCount));

        queueOfStationLogs.add(new StationLogUnit(stationId, -1, routeId, actualTime, busNumber, takeInPassCount, getOffPassCount, stayPassCount));
    }

    @Override
    public void sendStationLogPassLeft(long stationId, String stationName, long passId, int queueSize) {
        sendLogMessage(String.format("Остановка \"%s\" [ID: %d]", stationName, stationId),
                String.format("Пассажир [ID = %d] покинул остановку, осталось в очереди: %d", passId, queueSize));
    }

    @Override
    public void sendBusLogArriveStation(String busNumber, long stationId, String stationName, int passInsideCount, int freeSeatsCount) {
        sendLogMessage(String.format("Автобус № %s", busNumber),
                String.format("Прибыл на остановку \"%s\", кол-во пассажиров в салоне %s, свободных мест %d",
                        stationName, passInsideCount, freeSeatsCount));
    }

    @Override
    public void sendBusLogDoStop(String busNumber, long routeId, String routeName, long stationId, String stationName,
                          int getOffPassCount, int takeInPassCount, int passInsideCount, int freeSeatsCount) {
        sendLogMessage(String.format("Автобус № %s (марш %s [ID: %d])", busNumber, routeName, routeId),
                String.format("Высадил %d, принял %d, кол-во пассажиров в салоне %s, осталось свободных мест %d",
                        getOffPassCount, takeInPassCount, passInsideCount, freeSeatsCount));

        queueOfBusLogs.add(new BusLogUnit(actualTime, routeId, stationId, busNumber, freeSeatsCount + passInsideCount,
                getOffPassCount, takeInPassCount, passInsideCount));
    }

    @Override
    public void sendBusLogRunProgress(String busNumber, String msg) {
        sendLogMessage(String.format("Автобус № %s", busNumber), msg);
    }

    @Override
    public void sendRunLog(long routeId, String routeNumber, String busNumber, Date timeStart, Date timeFinish, int passDeliveredCount) {
        sendLogMessage(String.format("Рейс по маршруту %s [ID: %d]", routeNumber, routeId),
                String.format("Рейс пройден автобусом %s за %d минут (с %s до %s). Перевезено %d пассажиров",
                        busNumber, DateTimeHelper.diffMinutes(timeStart, timeFinish),
                        DateTimeHelper.toString(timeStart), DateTimeHelper.toString(timeFinish), passDeliveredCount)
                );

        queueOfRunLogs.add(new RunLogUnit(routeId, routeNumber, busNumber, timeStart, timeFinish, passDeliveredCount));
    }


    public boolean isFinish() {
        return isFinishSimulation;
    }

    public String pollMsg() {
        return queueOfLogMsg.poll();
    }

    public int sizeOfQueueMsg() {
        return queueOfLogMsg.size();
    }

    private void sendLogMessage(String src, String msg) {
        queueOfLogMsg.add(String.format("%-40s | %s", src, msg));
    }
}
