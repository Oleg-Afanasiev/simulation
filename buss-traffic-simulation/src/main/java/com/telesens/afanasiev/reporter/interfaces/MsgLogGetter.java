package com.telesens.afanasiev.reporter.interfaces;

/**
 * Created by oleg on 1/10/16.
 */
public interface MsgLogGetter {
    boolean isFinishCollect();
    boolean isEmptyMsgLogsQueue();
    String pollMsgLog();
}
