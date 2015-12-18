package com.afanasiev.telesens.simulate;

/**
 * Created by oleg on 12/5/15.
 */
public class Arc<T> {
    public static class EqualNodesException extends RuntimeException {
        private String msg;
        private String baseMsg = "ErrorNodesInit! Two nodes must be differ";
        public EqualNodesException() {
            msg = "";
        }

        public EqualNodesException(String msg) {
            this.msg = msg;
        }

        @Override
        public String toString() {
            if (msg.equals(""))
                return baseMsg;
            else
                return msg;
        }
    }

    private T nodeFirst;
    private T nodeLast;
    private int duration;

    public Arc(T nodeFirst, T nodeLast, int duration) throws EqualNodesException {
        if (nodeFirst.equals(nodeLast))
            throw new EqualNodesException("ErrorNodesInit! Two nodes must be differ");

        if (duration <= 0)
            throw new IllegalArgumentException("Incorrect duration for 'Arc'. Duration must be positive");

        this.nodeFirst = nodeFirst;
        this.nodeLast = nodeLast;
        this.duration = duration;
    }

    public T getNodeFirst() {
        return nodeFirst;
    }

    public T getNodeLast() {
        return nodeLast;
    }

    public T getOppositeNode(T node) {
        if (nodeFirst.equals(node))
            return nodeLast;

        if (nodeLast.equals(node))
            return nodeFirst;

        return null;
    }

    public int getDuration() {
        return duration;
    }

    public boolean contains(T node) {
        return (nodeFirst.equals(node) || nodeLast.equals(node));
    }
}
