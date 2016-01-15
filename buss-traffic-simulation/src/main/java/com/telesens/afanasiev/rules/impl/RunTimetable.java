package com.telesens.afanasiev.rules.impl;

import com.telesens.afanasiev.rules.RunTask;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.*;

/**
 * Created by oleg on 12/30/15.
 */
public class RunTimetable implements Serializable {

    private static final long serialVersionUID = 1L;

    @Getter
    @Setter
    private Map<Long, Queue<RunTask>> queueMapOfTasks;

    public RunTimetable() {
        queueMapOfTasks = new HashMap<>();
    }

    public void addTask(long routeId, Date timeStart, int breakForwardDuration, int breakBackDuration) {
        Queue<RunTask> queueForRoute;
        RunTask task = new RunTaskImpl(routeId, timeStart, breakForwardDuration, breakBackDuration);

        if (queueMapOfTasks.containsKey(routeId)) {
            queueForRoute = queueMapOfTasks.get(routeId);
            queueForRoute.add(task);
        }
        else { //queue must be ordered by time
            queueForRoute = new PriorityQueue<>(
                    //(task1, task2)->(Long.compare(task1.getTimeStart().getTime(), (task2.getTimeStart().getTime())))
            );
            queueForRoute.add(task);
            queueMapOfTasks.put(routeId, queueForRoute);
        }
    }

    public boolean isExistTask(long routeId) {
        return (queueMapOfTasks.containsKey(routeId) && queueMapOfTasks.get(routeId).size() > 0);
    }

    public RunTask peekTask(long routeId) {
        if (queueMapOfTasks.containsKey(routeId))
            return queueMapOfTasks.get(routeId).peek();
        else
            return null;
    }

    public RunTask pollTask(long routeId) {
        if (queueMapOfTasks.containsKey(routeId))
            return queueMapOfTasks.get(routeId).poll();
        else
            return null;
    }


}
