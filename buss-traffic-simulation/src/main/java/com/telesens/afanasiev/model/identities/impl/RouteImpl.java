package com.telesens.afanasiev.model.identities.impl;

import com.telesens.afanasiev.model.identities.*;
import com.telesens.afanasiev.model.helper.DaoUtils;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

/**
 * Created by oleg on 12/7/15.
 */
@Data
@NoArgsConstructor
public class RouteImpl<T extends Identity> extends IdentityImpl implements Route<T>, Identity, Iterable<Arc<T>> {
    class RouteIterator implements Iterator<Arc<T>> {
        private int iteratorCursor = -1;

        @Override
        public boolean hasNext() {
            return iteratorCursor < arcs.size() - 1;
        }

        @Override
        public Arc<T> next() {
            iteratorCursor++;
            return arcs.get(iteratorCursor);
        }
    }

    private static final long serialVersionUID = 1;

    private String number;
    private String description;
    private List<Arc<T>> arcs;
    private Direct direct;
    private double cost;
    private T firstNode;
    private long pairRouteId; // it means routes: 'FORWARD' <-> 'BACK'

    @SafeVarargs
    public RouteImpl(String number, String description, Direct direct, double cost, T firstNode, Arc<T>... listArcs) {
        this(number, description, direct, cost, firstNode, Arrays.asList(listArcs));
    }

    public RouteImpl(String number, String description, Direct direct, double cost, T firstNode, List<Arc<T>> listArcs)
        throws IllegalArgumentException {
        if (number == null || number.equals(""))
            throw new IllegalArgumentException("Incorrect number for 'Route'.");

        if (cost < 0)
            throw new IllegalArgumentException("Incorrect cost for 'Route'.");

        if (listArcs.size() == 0)
            throw new IllegalArgumentException("Incorrect listArcs for 'Route'. The route must contain as less one arc.");

        if (!listArcs.get(0).contains(firstNode))
            throw new IllegalArgumentException("Incorrect sequence of 'listArgs' for 'Route'. The first arc must contain 'firstNode'.");

        this.number = number;
        this.description = description;
        this.direct = direct;
        this.firstNode = firstNode;

        T prevNode = firstNode;
        arcs = new ArrayList<>();

        for (Arc<T> nextArc : listArcs)
            if (nextArc.contains(prevNode)) {
                this.arcs.add(nextArc);
                prevNode = nextArc.getOppositeNode(prevNode);
            }
            else
                throw new IllegalArgumentException("Incorrect sequence of 'listArgs' for 'Route'. ");
    }

    @Override
    public T getLastNode() {
        return getNode(arcs.size());
    }

    @Override
    public T getNode(int index) {
        if (index < 0 || index > arcs.size())
            throw new IllegalArgumentException("Incorrect index of node");

        if (index == arcs.size())
            return arcs.get(index - 1).getNodeRight();
        else
            return arcs.get(index).getNodeLeft();
    }

    @Override
    public void createLastArc(long arcID, T rightNode, int duration) {

        T leftNode = getLastNode();
        Arc<T> arc = new ArcImpl<>(leftNode, rightNode, duration);
        DaoUtils.setPrivateId(arc, arcID);

        arcs.add(arc);
    }

    public void addFirstNode(T node) {
        throw new UnsupportedOperationException("The node can't be added");
    }

    public void addLastNode(T node) {
        throw new UnsupportedOperationException("The node can't be added");
    }

    public void insertNode(T node, int index) {
        throw new UnsupportedOperationException("The node can't be inserted");
    }

    public void removeNode(T node) {
        throw new UnsupportedOperationException("The node can't be removed");
    }

    @Override
    public TypeOfRoute getType() {
        if (getFirstNode().equals(getLastNode()))
            return TypeOfRoute.CIRCULAR;
        else
            return TypeOfRoute.SIMPLE;
    }

    @Override
    public Iterator<Arc<T>> iterator() {
        return new RouteIterator();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(String.format("Маршрут \"%s\", %s, [ID: %d]: %n", number, direct, getId()));
        sb.append(String.format("%s - %s%n",
                ((Station) getFirstNode()).getName().toUpperCase(),
                ((Station) getLastNode()).getName().toUpperCase()));

        if (!description.equals(""))
            sb.append(String.format("%s %n", description));

        T prevNode = firstNode;

        sb.append(String.format("%3s%s%n", " ", firstNode));

        for (Arc<T> arc : arcs) {
            sb.append(String.format("%3s%s%n", " ", arc.getOppositeNode(prevNode)));
            prevNode = arc.getOppositeNode(prevNode);
        }

        return sb.toString();
    }

    public String testShow() {
        StringBuilder sb = new StringBuilder();
        String type = getType() == TypeOfRoute.CIRCULAR ? "(" + TypeOfRoute.CIRCULAR + ")" : "";

        sb.append(String.format("Route \"%s\" %s, %s: %n", number, type, direct));

        for (Arc<T> arc : arcs)
            sb.append(String.format("-[%s, %s]-", arc.getNodeLeft(), arc.getNodeRight()));

        return sb.toString();
    }
}
