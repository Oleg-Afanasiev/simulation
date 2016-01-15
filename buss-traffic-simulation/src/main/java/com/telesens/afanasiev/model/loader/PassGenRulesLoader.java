package com.telesens.afanasiev.model.loader;

import com.telesens.afanasiev.dao.PassGenTaskDAO;
import com.telesens.afanasiev.dao.impl.jaxb.PassGenTaskDAOImpl;
import com.telesens.afanasiev.model.rules.PassengerGenerationRules;
import com.telesens.afanasiev.model.Identities.PassengerGenerationTask;

import java.util.Collection;

/**
 * Created by oleg on 1/13/16.
 */
public class PassGenRulesLoader {

    public PassengerGenerationRules loadPassGenRules() {
        PassengerGenerationRules passengerGenerationRules = new PassengerGenerationRules();
        PassGenTaskDAO taskDAO = new PassGenTaskDAOImpl();
        Collection<PassengerGenerationTask> tasks = taskDAO.getAll();

        for (PassengerGenerationTask task : tasks)
                passengerGenerationRules.addRule(task);

        return passengerGenerationRules;
    }
}
