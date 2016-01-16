package com.telesens.afanasiev.model.loader;

import com.telesens.afanasiev.model.simulation.TransportNetwork;
import com.telesens.afanasiev.model.rules.PassengerGenerationRules;
import com.telesens.afanasiev.model.rules.RunTimetable;

/**
 * Created by oleg on 1/11/16.
 */
public class DataLoader {

    private TransportNetworkLoader transportNetworkLoader;
    private RunTimeTableLoader runTimeTableLoader;
    private PassGenRulesLoader passGenRulesLoader;

    public DataLoader() {
        transportNetworkLoader = new TransportNetworkLoader();
        runTimeTableLoader = new RunTimeTableLoader();
        passGenRulesLoader = new PassGenRulesLoader();
    }

    public RunTimetable loadRunTimetable() {
        return runTimeTableLoader.loadRunTimetable();
    }

    public PassengerGenerationRules loadPassGenRules() {
        return passGenRulesLoader.loadPassGenRules();
    }

    public TransportNetwork loadBusNetwork()  {
        return transportNetworkLoader.loadBusNetwork();
    }
}
