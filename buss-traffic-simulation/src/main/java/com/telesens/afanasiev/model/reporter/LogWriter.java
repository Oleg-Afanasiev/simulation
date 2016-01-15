package com.telesens.afanasiev.model.reporter;

import lombok.extern.log4j.Log4j;

/**
 * Created by oleg on 1/6/16.
 */
@Log4j
public class LogWriter implements Runnable{
    private LogCollector logCollector;

    public LogWriter() {
        logCollector = LogCollector.getInstance();
    }

    @Override
    public void run() {

        while (!logCollector.isFinishCollect() || !logCollector.isEmptyMsgLogsQueue()) {
            if (!logCollector.isEmptyMsgLogsQueue())
                log.info(logCollector.pollMsgLog());
        }
    }
}
