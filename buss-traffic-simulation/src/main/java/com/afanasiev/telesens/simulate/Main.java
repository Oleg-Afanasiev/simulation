package com.afanasiev.telesens.simulate;
import org.apache.log4j.Logger;

/**
 * Created by oleg on 12/4/15.
 */
public class Main {
    private static Logger logger;
    public static void main(String[] args) {
        logger = Logger.getLogger(Main.class.getName());
        logger.info("****************START SIMULATION****************");
        logger.debug("****************FINISH SIMULATION****************");
    }
}
