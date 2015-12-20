package com.afanasiev.telesens.simulate;

import java.util.Date;


/**
 * Created by oleg on 12/4/15.
 */
public class Main {

    private static BusTrafficSimulator simulator;

    public static void main(String[] args) {

        simulator = new BusTrafficSimulator();
        //System.out.println(simulator.allRoutesToString());

        Date dtNow = new Date();

        simulator.start(dtNow, DateTimeHelper.incHours(dtNow, 2));
    }
}
