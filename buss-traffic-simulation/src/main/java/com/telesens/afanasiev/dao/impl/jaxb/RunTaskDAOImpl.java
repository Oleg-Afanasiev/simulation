package com.telesens.afanasiev.dao.impl.jaxb;

import com.telesens.afanasiev.dao.DAOException;
import com.telesens.afanasiev.dao.RunTaskDAO;
import com.telesens.afanasiev.dao.impl.jaxb.schemes.RunTimetable;
import com.telesens.afanasiev.helper.DaoUtils;
import com.telesens.afanasiev.helper.DateTimeHelper;
import com.telesens.afanasiev.rules.RunTask;
import com.telesens.afanasiev.rules.impl.RunTaskImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * Created by oleg on 1/13/16.
 */
public class RunTaskDAOImpl implements RunTaskDAO {
    private DAOLoaderData loader;

    public RunTaskDAOImpl() {
        loader = DAOLoaderData.getInstance();
    }

    @Override
    public RunTask getById(long id) {
        RunTimetable data = loader.getRunTimetableData();
        RunTimetable.Task taskData = parseById(data, id);

        if (taskData == null)
            throw new DAOException("Entity with specified ID wasn't found. Id = " + id);

        RunTask task = createTask(taskData);
        return task;
    }

    @Override
    public Collection<RunTask> getAll() {
        RunTimetable data = loader.getRunTimetableData();
        Collection<RunTimetable.Task> tasksData = parseAll(data);
        Collection<RunTask> tasks = new ArrayList<>();
        RunTask task;

        for (RunTimetable.Task taskData : tasksData) {
            task = createTask(taskData);
            tasks.add(task);
        }
        return tasks;
    }

    private RunTimetable.Task parseById(RunTimetable data, long id) {
        for (RunTimetable.Task taskData : data.getTask())
            if (taskData.getId() == id)
                return taskData;

        return null;
    }

    private Collection<RunTimetable.Task> parseAll(RunTimetable data) {
        Collection<RunTimetable.Task> tasksData = new ArrayList<>();
        for (RunTimetable.Task taskData : data.getTask())
            tasksData.add(taskData);

        return tasksData;
    }

    private RunTask createTask(RunTimetable.Task taskData) {
        long id = taskData.getId();
        long routeId = taskData.getRouteId();
        Date timeStart = DateTimeHelper.XMLGregorianCalendarToDate(taskData.getTimeStart());
        int breakForwardDuration = taskData.getBreakForwardDuration();
        int breakBackDuration = taskData.getBreakBackDuration();

        RunTask task = new RunTaskImpl(routeId, timeStart, breakForwardDuration, breakBackDuration);

        try {
            DaoUtils.setPrivateField(task, "id", id);
        } catch(IllegalAccessException | NoSuchFieldException exc) {
            exc.printStackTrace();;
        }

        return task;
    }
}
