package com.telesens.afanasiev;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Created by oleg on 12/23/15.
 */
public class Route<T> implements Iterable<Arc<T>>  {
    class RouteIterator implements Iterator<Arc<T>> {
        private int iteratorCursor = -1;

        @Override
        public boolean hasNext() {
            return iteratorCursor < listArcs.size() - 1;
        }

        @Override
        public Arc<T> next() {
            iteratorCursor++;
            return listArcs.get(iteratorCursor);
        }
    }
    private T nodeFirst;
    private List<Arc<T>> listArcs;
    private String name;

    public Route(T nodeFirst, String name,  Arc<T>...listArcs) {
        this.nodeFirst = nodeFirst;
        this.name = name;
        this.listArcs = new ArrayList<>();
        this.listArcs.addAll(Arrays.asList(listArcs));
    }

    public String getName() {
        return name;
    }

    public T getFirstNode() {
        return nodeFirst;
    }

    @Override
    public Iterator<Arc<T>> iterator() {
        return new RouteIterator();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        T prevNode = nodeFirst;
        sb.append(String.format("%s : %s", name, nodeFirst));

        for (Arc<T> arc : listArcs) {
            sb.append(String.format(" -[%d мин]-> %s", arc.getDuration(), arc.getOppositeNode(prevNode)));
            prevNode = arc.getOppositeNode(prevNode);
        }

        return sb.toString();
    }
}
