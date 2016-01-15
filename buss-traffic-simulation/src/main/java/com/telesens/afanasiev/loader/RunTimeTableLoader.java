package com.telesens.afanasiev.loader;

import com.telesens.afanasiev.dao.RunTaskDAO;
import com.telesens.afanasiev.dao.impl.jaxb.RunTaskDAOImpl;
import com.telesens.afanasiev.rules.RunTask;
import com.telesens.afanasiev.rules.impl.RunTimetable;

import java.util.Collection;

/**
 * Created by oleg on 1/13/16.
 */
public class RunTimeTableLoader {

    public RunTimetable loadRunTimetable() {
        RunTimetable runTimetable = new RunTimetable();
        RunTaskDAO runTaskDAO = new RunTaskDAOImpl();
        Collection<RunTask> tasks = runTaskDAO.getAll();

        for (RunTask task : tasks)
            runTimetable.addTask(task.getRouteId(), task.getTimeStart(), task.getBreakForwardDuration(), task.getBreakBackDuration());

        return runTimetable;
    }
}
