package com.telesens.afanasiev.model.identities;

/**
 * Created by oleg on 1/11/16.
 */
public enum Direct {
    FORWARD ("прямой"),
    BACK ("обратный");

    private String name;

    Direct(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
