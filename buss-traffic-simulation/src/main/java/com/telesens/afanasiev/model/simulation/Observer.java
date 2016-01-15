package com.telesens.afanasiev.model.simulation;

import java.util.Date;

/**
 * Created by oleg on 12/10/15.
 */
public interface Observer {
    void tick(Date date);
}
