package com.telesens.afanasiev;

import java.io.Serializable;
import java.util.Date;
import java.util.PriorityQueue;

/**
 * Created by oleg on 12/23/15.
 */
public class TimeTable implements Serializable{
    private static final long serialVersionUID = -1L;
    private PriorityQueue<TrainTask> queueTasks;

    public TimeTable() {
        queueTasks = new PriorityQueue<>();
    }

    public PriorityQueue<TrainTask> getQueueTasks() {
        return queueTasks;
    }

    public void setQueueTasks(PriorityQueue<TrainTask> queueTasks) {
        this.queueTasks = queueTasks;
    }

    public void addTask(TrainTask task) {
        queueTasks.add(task);
    }

    public boolean isExistNextTask() {
        return queueTasks.size() > 0;
    }

    public Date getTimeForNextTask() {
        return queueTasks.peek().getTimeStart();
    }

    public TrainTask poolTask() {
        return queueTasks.poll();
    }
}
