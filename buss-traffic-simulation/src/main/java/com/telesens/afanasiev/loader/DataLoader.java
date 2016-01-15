package com.telesens.afanasiev.loader;

import com.telesens.afanasiev.rules.PassengerGenerationRules;
import com.telesens.afanasiev.rules.impl.PassengerGenerationRulesImpl;
import com.telesens.afanasiev.rules.impl.RunTimetable;
import com.telesens.afanasiev.simulation.*;

/**
 * Created by oleg on 1/11/16.
 */
public class DataLoader {
    public static class NoValidLoadDataException extends RuntimeException {
        private String msg;
        private String baseMsg = "Incorrect load data. ";
        public NoValidLoadDataException() {
            msg = "";
        }

        public NoValidLoadDataException(String msg) {
            this.msg = msg;
        }

        @Override
        public String toString() {
            if (msg.equals(""))
                return baseMsg;
            else
                return msg;
        }
    }

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
