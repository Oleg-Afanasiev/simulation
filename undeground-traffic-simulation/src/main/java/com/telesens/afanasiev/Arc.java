package com.telesens.afanasiev;

/**
 * Created by oleg on 12/23/15.
 */
public class Arc<T> {
    private T nodeFrom;
    private T nodeTo;
    private int duration; // how much min needs to reach from "nodeFrom" to "nodeTo"

    public Arc (T nodeFrom, T nodeTo, int duration) {
        this.nodeFrom = nodeFrom;
        this.nodeTo = nodeTo;
        this.duration = duration;
    }

    public T getNodeFrom() {
        return nodeFrom;
    }

    public T getNodeTo() {
        return nodeTo;
    }

    public T getOppositeNode(T node) {
        return node.equals(nodeFrom) ? nodeTo : nodeFrom;
    }

    public int getDuration() {
        return duration;
    }
}
