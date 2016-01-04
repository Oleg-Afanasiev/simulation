package com.telesens.afanasiev;

import org.apache.log4j.Logger;

/**
 * Created by oleg on 12/25/15.
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
        System.out.println(String.format("%-40s | %s", src, msg));
    }
}
