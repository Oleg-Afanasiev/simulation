package com.telesens.afanasiev.simulation;

/**
 * Created by oleg on 1/14/16.
 */
public interface Arc<T> extends Identity {

    T getNodeLeft();
    T getNodeRight();
    int getDuration();
    void setDuration(int duration);

    T getOppositeNode(T node);
    boolean contains(T node);
}
