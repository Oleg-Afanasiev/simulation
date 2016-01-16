package com.telesens.afanasiev.dao.impl.jaxb;

import com.telesens.afanasiev.dao.DAOException;
import com.telesens.afanasiev.dao.PassGenTaskDAO;
import com.telesens.afanasiev.dao.impl.jaxb.schemes.PassGenRules;
import com.telesens.afanasiev.model.helper.DateTimeHelper;
import com.telesens.afanasiev.model.identities.PassengerGenerationTask;
import com.telesens.afanasiev.model.identities.impl.PassengerGenerationTaskImpl;
import com.telesens.afanasiev.model.rules.PassengerTargetSpreading;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * Created by oleg on 1/13/16.
 */
public class PassGenTaskDAOImpl implements PassGenTaskDAO {
    private DAOLoaderData loader;

    public PassGenTaskDAOImpl() {
        loader = DAOLoaderData.getInstance();
    }

    @Override
    public PassengerGenerationTask getById(long id) {
        PassGenRules data = loader.getPassGenRulesData();
        PassGenRules.Tasks.Task taskData = parseTaskById(data, id);

        if (taskData == null)
            throw new DAOException("Entity with specified ID wasn't found. Id = " + id);

        PassengerGenerationTask task = createTask(data, taskData);

        return task;
    }

    @Override
    public Collection<PassengerGenerationTask> getAll() {
        PassGenRules data = loader.getPassGenRulesData();
        Collection<PassGenRules.Tasks.Task> tasksData = parseAllTask(data);

        PassengerGenerationTask task;
        Collection<PassengerGenerationTask> tasks = new ArrayList<>();

        for (PassGenRules.Tasks.Task taskData : tasksData) {
            task = createTask(data, taskData);
            tasks.add(task);
        }

        return tasks;
    }

    private PassGenRules.Tasks.Task parseTaskById(PassGenRules data, long id) {
        for (PassGenRules.Tasks.Task taskData : data.getTasks().getTask()) {
            if (taskData.getId() == id)
                return taskData;
        }
        return null;
    }

    private PassGenRules.Targets.Target parseTargetById(PassGenRules data, long id) {
        for (PassGenRules.Targets.Target targetData : data.getTargets().getTarget()) {
            if (targetData.getId() == id) {
                return targetData;
            }
        }
        return null;
    }

    private Collection<PassGenRules.Tasks.Task> parseAllTask(PassGenRules data) {
        Collection<PassGenRules.Tasks.Task> tasksData = new ArrayList<>();
        for (PassGenRules.Tasks.Task taskData : data.getTasks().getTask()) {
            tasksData.add(taskData);
        }

        return tasksData;
    }

    private PassengerGenerationTask createTask(PassGenRules data, PassGenRules.Tasks.Task taskData) {

        long stationId = taskData.getStationId();
        Date timeFrom = DateTimeHelper.XMLGregorianCalendarToDate(taskData.getTimeFrom());
        int duration = taskData.getDuration();
        int passCount = taskData.getPassCount();
        int minutesLimitWaiting = taskData.getMinutesLimitWaiting();

        PassengerTargetSpreading spreading = new PassengerTargetSpreading();
        PassGenRules.Targets.Target targetData;

        for (PassGenRules.Tasks.Task.LinkTarget linkTargetData : taskData.getLinkTarget()) {
            targetData = parseTargetById(data, linkTargetData.getTargetId());

            if (targetData == null)
                throw new DAOException("Entity with specified ID wasn't found. Id = " + linkTargetData.getTargetId());

            spreading.addTarget(targetData.getStationId(), targetData.getFactor());
        }

        PassengerGenerationTask task = new PassengerGenerationTaskImpl(stationId, timeFrom, duration, passCount, minutesLimitWaiting, spreading);

        return task;
    }
}
