package com.telesens.afanasiev.reporter;

import org.apache.log4j.Logger;

/**
 * Created by oleg on 1/6/16.
 */
public class LogWriter implements Runnable{
    private LogCollector logCollector;
    private final Logger logger;

    public LogWriter() {
        logCollector = LogCollector.getInstance();
        logger = Logger.getLogger(LogWriter.class);
    }

    @Override
    public void run() {

        while (!logCollector.isFinish() || logCollector.sizeOfQueueMsg() != 0) {
            if (logCollector.sizeOfQueueMsg() > 0)
                logger.info(logCollector.pollMsg());
        }
    }
}
