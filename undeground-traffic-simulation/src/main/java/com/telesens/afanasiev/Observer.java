package com.telesens.afanasiev;

import java.util.Date;

/**
 * Created by oleg on 12/23/15.
 */
public interface Observer  {
    void tick(Date nextTime);
}
