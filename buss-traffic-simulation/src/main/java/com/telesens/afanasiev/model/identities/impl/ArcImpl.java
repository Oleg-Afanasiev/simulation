package com.telesens.afanasiev.model.identities.impl;

import com.telesens.afanasiev.model.identities.Arc;
import com.telesens.afanasiev.model.identities.Identity;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by oleg on 12/5/15.
 */
@Data
@NoArgsConstructor
public class ArcImpl<T extends Identity> extends IdentityImpl implements Arc<T>, Identity {
    private static final long serialVersionUID = 1L;

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

    private T nodeLeft;
    private T nodeRight;
    private int duration;

    public ArcImpl(T nodeLeft, T nodeRight, int duration) throws EqualNodesException {
        if (nodeLeft.equals(nodeRight))
            throw new EqualNodesException("ErrorNodesInit! Two nodes must be differ");

        if (duration <= 0)
            throw new IllegalArgumentException("Incorrect duration for 'Arc'. Duration must be positive");

        this.nodeLeft = nodeLeft;
        this.nodeRight = nodeRight;
        this.duration = duration;
    }

    @Override
    public T getOppositeNode(T node) {
        if (nodeLeft.equals(node))
            return nodeRight;

        if (nodeRight.equals(node))
            return nodeLeft;

        return null;
    }

    @Override
    public boolean contains(T node) {
        return (nodeLeft.equals(node) || nodeRight.equals(node));
    }
}
