package com.afanasiev.telesens.simulate;

import org.apache.log4j.Logger;

/**
 * Created by oleg on 12/17/15.
 */
public class ReportCollector {
    private final Logger logger;
    private static ReportCollector uniqueInstance = new ReportCollector();

    private ReportCollector() {
        logger = Logger.getLogger(ReportCollector.class);
    }

    public static ReportCollector getInstance() {
        return uniqueInstance;
    }

    public void sendMessage (String src, String msg) {
        logger.debug(String.format("%-40s | %s", src, msg));
    }
}
