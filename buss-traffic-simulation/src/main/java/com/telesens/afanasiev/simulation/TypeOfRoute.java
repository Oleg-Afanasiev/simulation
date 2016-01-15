package com.telesens.afanasiev.simulation;

/**
 * Created by oleg on 1/14/16.
 */
public enum TypeOfRoute {
    CIRCULAR ("кольцевой"),
    SIMPLE ("");

    private String name;

    TypeOfRoute(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}