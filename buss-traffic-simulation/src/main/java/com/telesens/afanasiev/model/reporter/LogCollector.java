package com.telesens.afanasiev.model.reporter;

import com.telesens.afanasiev.model.helper.DateTimeHelper;
import com.telesens.afanasiev.model.reporter.interfaces.*;
import com.telesens.afanasiev.model.reporter.unit.BusLogUnit;
import com.telesens.afanasiev.model.reporter.unit.PassengerLogUnit;
import com.telesens.afanasiev.model.reporter.unit.RunLogUnit;
import com.telesens.afanasiev.model.reporter.unit.StationLogUnit;

import java.util.ArrayDeque;
import java.util.Date;
import java.util.Queue;

/**
 * Created by oleg on 12/17/15.
 */
public class LogCollector implements
        ClockReporter, SimulatorReporter, RouteReporter, PassengerReporter, StationReporter, BusReporter, RunReporter,
        MsgLogGetter, BusLogGetter, RunLogGetter, PassengerLogGetter, StationLogGetter {

    private volatile static LogCollector uniqueInstance;
    private volatile Queue<String> queueOfMsgLogs;
    private volatile Queue<BusLogUnit> queueOfBusLogs;
    private volatile Queue<StationLogUnit> queueOfStationLogs;
    private volatile Queue<PassengerLogUnit> queueOfPassLogs;
    private volatile Queue<RunLogUnit> queueOfRunLogs;

    private boolean isFinishSimulation;
    private Date actualTime;

    private LogCollector() {
        queueOfMsgLogs = new ArrayDeque<>();
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

        saveLogMessage("", "");
        saveLogMessage("Clock -> tick", DateTimeHelper.toString(actualTime));
        saveLogMessage("", "");
    }

    @Override
    public void sendSimulatorLogStart(Date startTime) {
        this.actualTime = startTime;
        queueOfMsgLogs.add("\n******************************* НАЧАЛО СИМУЛЯЦИИ *******************************\n\n");

        saveLogMessage("Время начала:", DateTimeHelper.toString(startTime));
        saveLogMessage("", "");
    }

    @Override
    public void sendSimulatorLogFinish() {
        isFinishSimulation = true;
        queueOfMsgLogs.add("\n******************************* СИМУЛЯЦИЯ ЗАВЕРШЕНА *******************************\n");
    }

    @Override
    public void sendRouteLog(String routeNumber, int busesRunCount, int busesWaitingCount) {
        saveLogMessage(
                String.format("Маршрут(ы) № %s", routeNumber),
                String.format("Кол-во автобусов: в рейсах %d, освободившихся %d", busesRunCount, busesWaitingCount));
    }

    @Override
    public void sendPassLogDelivered(long passengerId, long stationIdFrom, String stationName, String busNumber, Date timeComeInBus, Date timeGoOffBus) {
        saveLogMessage(String.format("Пассажир [ID: %d]", passengerId),
                String.format("Вышел на остановке \"%s\"", stationName)
        );

        queueOfPassLogs.add(new PassengerLogUnit(passengerId, stationIdFrom, busNumber, timeComeInBus, timeGoOffBus));
    }

    @Override
    public void sendPassLogWelcomeToStation(long passengerId, String passengerName,
                                            long stationFromId, String stationFromName,
                                            long stationTargetId, String stationTargetName) {

        saveLogMessage(String.format("Пассажир %s [ID: %d]", passengerName, passengerId),
                String.format("Пришел на остановку \"%s\". Жду автобус на: \"%s\"", stationFromName, stationTargetName));
    }

    @Override
    public void sendPassLogWelcomeToBus(long passengerId, long stationId, String busNumber) {

    }

    @Override
    public void sendStationLogQueueSize(long stationId, String stationName, int queueSize) {
        saveLogMessage(String.format("Остановка \"%s\" [ID: %d]", stationName, stationId),
                String.format("Кол-во в очереди: %d", queueSize));
    }

    @Override
    public void sendStationLogBusArrived(long stationId, String stationName,
                                         long routeId, String routeNumber, String routeDirect, String busNumber,
                                         int freeSeatsCount, int getOffPassCount, int takeInPassCount, int stayPassCount) {
        saveLogMessage(String.format("Остановка \"%s\" [ID: %d]", stationName, stationId),
                String.format("Прибыл автобус № %s, маршрут № %s - %s, вышло %d, свободных мест %d",
                        busNumber, routeNumber, routeDirect, getOffPassCount, freeSeatsCount));

        saveLogMessage(String.format("Остановка \"%s\" [ID: %d]", stationName, stationId),
                String.format("В автобус № %s сели %d пассажиров, остались на остановке %d", busNumber, takeInPassCount, stayPassCount));

        queueOfStationLogs.add(new StationLogUnit(stationId, -1, routeId, actualTime, busNumber, takeInPassCount, getOffPassCount, stayPassCount));
    }

    @Override
    public void sendStationLogPassLeft(long stationId, String stationName, long passId, int queueSize) {
        saveLogMessage(String.format("Остановка \"%s\" [ID: %d]", stationName, stationId),
                String.format("Пассажир [ID = %d] покинул остановку, осталось в очереди: %d", passId, queueSize));
    }

    @Override
    public void sendBusLogArriveStation(String busNumber, long stationId, String stationName, int passInsideCount, int freeSeatsCount) {
        saveLogMessage(String.format("Автобус № %s", busNumber),
                String.format("Прибыл на остановку \"%s\", кол-во пассажиров в салоне %s, свободных мест %d",
                        stationName, passInsideCount, freeSeatsCount));
    }

    @Override
    public void sendBusLogDoStop(String busNumber, long routeId, String routeName, long stationId, String stationName,
                          int getOffPassCount, int takeInPassCount, int passInsideCount, int freeSeatsCount) {
        saveLogMessage(String.format("Автобус № %s (марш %s [ID: %d])", busNumber, routeName, routeId),
                String.format("Высадил %d, принял %d, кол-во пассажиров в салоне %s, осталось свободных мест %d",
                        getOffPassCount, takeInPassCount, passInsideCount, freeSeatsCount));

        queueOfBusLogs.add(new BusLogUnit(actualTime, routeId, stationId, busNumber, freeSeatsCount + passInsideCount,
                getOffPassCount, takeInPassCount, passInsideCount));
    }

    @Override
    public void sendBusLogRunProgress(String busNumber, String msg) {
        saveLogMessage(String.format("Автобус № %s", busNumber), msg);
    }

    @Override
    public void sendRunLog(long routeId, String routeNumber, String busNumber, Date timeStart, Date timeFinish, int passDeliveredCount) {
        saveLogMessage(String.format("Рейс по маршруту %s [ID: %d]", routeNumber, routeId),
                String.format("Рейс пройден автобусом %s за %d минут (с %s до %s). Перевезено %d пассажиров",
                        busNumber, DateTimeHelper.diffMinutes(timeStart, timeFinish),
                        DateTimeHelper.toString(timeStart), DateTimeHelper.toString(timeFinish), passDeliveredCount)
        );

        queueOfRunLogs.add(new RunLogUnit(routeId, routeNumber, busNumber, timeStart, timeFinish, passDeliveredCount));
    }

    @Override
    public boolean isFinishCollect() {
        return isFinishSimulation;
    }

    @Override
    public String pollMsgLog() {
        return queueOfMsgLogs.poll();
    }

    @Override
    public boolean isEmptyMsgLogsQueue() {
        return queueOfMsgLogs.isEmpty();
    }

    @Override
    public boolean isEmptyBusLogsQueue() {
        return queueOfBusLogs.isEmpty();
    }

    @Override
    public BusLogUnit pollBusLog() {
        return queueOfBusLogs.poll();
    }

    @Override
    public boolean isEmptyRunLogsQueue() {
        return queueOfRunLogs.isEmpty();
    }

    @Override
    public RunLogUnit pollRunLog() {
        return queueOfRunLogs.poll();
    }

    @Override
    public boolean isEmptyPassLogsQueue() {
        return queueOfPassLogs.isEmpty();
    }

    @Override
    public PassengerLogUnit pollPassLog() {
        return queueOfPassLogs.poll();
    }

    @Override
    public boolean isEmptyStationLogsQueue() {
        return queueOfStationLogs.isEmpty();
    }

    @Override
    public StationLogUnit pollStationLog() {
        return queueOfStationLogs.poll();
    }

    private void saveLogMessage(String src, String msg) {
            queueOfMsgLogs.add(String.format("%-40s | %s", src, msg));
    }
}
