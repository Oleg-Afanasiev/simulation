package com.telesens.afanasiev.dao.impl.jaxb;

import com.telesens.afanasiev.dao.DAOException;
import com.telesens.afanasiev.dao.PassengerTargetDAO;
import com.telesens.afanasiev.dao.impl.jaxb.schemes.PassGenRules;
import com.telesens.afanasiev.helper.DaoUtils;
import com.telesens.afanasiev.rules.PassengerTarget;
import com.telesens.afanasiev.rules.impl.PassengerTargetImpl;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by oleg on 1/13/16.
 */

public class PassengerTargetDAOImpl implements PassengerTargetDAO {

    private DAOLoaderData loader;
    private PassengerTarget passengerTarget;

    public PassengerTargetDAOImpl() {
        loader = DAOLoaderData.getInstance();
    }

    @Override
    public PassengerTarget getById(long id) {
        PassGenRules data = loader.getPassGenRulesData();
        PassGenRules.Targets.Target targetData = parseTargetById(data, id);

        if (targetData == null)
            throw new DAOException("Entity with specified ID wasn't found. Id = " + id);

        PassengerTarget passTarget = createPassengerTarget(targetData);

        return passTarget;
    }

    @Override
    public Collection<PassengerTarget> getAll() {
        PassGenRules data = loader.getPassGenRulesData();
        Collection<PassGenRules.Targets.Target> targetsData = parseTargetsAll(data);

        Collection<PassengerTarget> targets = new ArrayList<>();
        PassengerTarget target;

        for (PassGenRules.Targets.Target targetData : targetsData) {
            target = createPassengerTarget(targetData);
            targets.add(target);
        }

        return targets;
    }

    private PassGenRules.Targets.Target parseTargetById(PassGenRules data, long id) {
        for (PassGenRules.Targets.Target targetData : data.getTargets().getTarget()) {
            if (targetData.getId() == id) {
                return targetData;
            }
        }
        return null;
    }

    private Collection<PassGenRules.Targets.Target> parseTargetsAll(PassGenRules data) {
        Collection<PassGenRules.Targets.Target> targetsData = new ArrayList<>();

        for (PassGenRules.Targets.Target targetData : data.getTargets().getTarget()) {
            targetsData.add(targetData);
        }

        return targetsData;
    }

    private PassengerTarget createPassengerTarget(PassGenRules.Targets.Target targetData) {

        long id = targetData.getId();
        long stationId = targetData.getStationId();
        int factor = targetData.getFactor();

        PassengerTarget target = new PassengerTargetImpl(stationId, factor);

        try {
            DaoUtils.setPrivateField(target, "id", id);
        } catch (NoSuchFieldException | IllegalAccessException exc) {
            exc.printStackTrace();
        }

        return target;
    }
}
