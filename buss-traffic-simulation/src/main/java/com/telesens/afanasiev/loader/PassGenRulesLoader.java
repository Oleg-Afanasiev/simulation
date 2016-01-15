package com.telesens.afanasiev.loader;

import com.telesens.afanasiev.dao.PassGenTaskDAO;
import com.telesens.afanasiev.dao.impl.jaxb.PassGenTaskDAOImpl;
import com.telesens.afanasiev.rules.PassengerGenerationRules;
import com.telesens.afanasiev.rules.PassengerGenerationTask;
import com.telesens.afanasiev.rules.impl.PassengerGenerationRulesImpl;

import java.util.Collection;

/**
 * Created by oleg on 1/13/16.
 */
public class PassGenRulesLoader {

    public PassengerGenerationRules loadPassGenRules() {
        PassengerGenerationRules passengerGenerationRules = new PassengerGenerationRulesImpl();
        PassGenTaskDAO taskDAO = new PassGenTaskDAOImpl();
        Collection<PassengerGenerationTask> tasks = taskDAO.getAll();

        for (PassengerGenerationTask task : tasks)
                passengerGenerationRules.addRule(task);

        return passengerGenerationRules;
    }
}
