package com.telesens.afanasiev.model.loader;

import com.telesens.afanasiev.dao.RunTaskDAO;
import com.telesens.afanasiev.dao.impl.jaxb.RunTaskDAOImpl;
import com.telesens.afanasiev.model.Identities.RunTask;
import com.telesens.afanasiev.model.rules.RunTimetable;

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
