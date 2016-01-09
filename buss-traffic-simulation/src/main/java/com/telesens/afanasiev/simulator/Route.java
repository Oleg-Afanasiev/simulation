package com.telesens.afanasiev.simulator;

import java.io.Serializable;
import java.util.*;

enum TypeOfRoute {
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

enum Direct {
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

/**
 * Created by oleg on 12/7/15.
 */
public class Route<T> implements Iterable<Arc<T>>, Serializable {
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
    private long ID;

    private String number;
    private String description;
    private List<Arc<T>> arcs;
    private Direct direct;
    private double cost;
    private T firstNode;


    public Route() {
        arcs = new ArrayList<>();
    }

    @SafeVarargs
    public Route(String number, String description, Direct direct, double cost, T firstNode, Arc<T>...listArcs) {

        if (number == null || number.equals(""))
            throw new IllegalArgumentException("Incorrect number for 'Route'.");

        if (cost < 0)
            throw new IllegalArgumentException("Incorrect cost for 'Route'.");

        if (listArcs.length == 0)
            throw new IllegalArgumentException("Incorrect listArcs for 'Route'. The route must contain as less one arc.");

        if (!listArcs[0].contains(firstNode))
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

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Arc<T>> getArcs() {
        return arcs;
    }

    public void setArcs(List<Arc<T>> arcs) {
        this.arcs = arcs;
    }

    public Direct getDirect() {
        return direct;
    }

    public void setDirect(Direct direct) {
        this.direct = direct;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public T getFirstNode() {
        return firstNode;
    }

    public void setFirstNode(T firstNode) {
        this.firstNode = firstNode;
    }

    public T getLastNode() {
        return getNode(arcs.size());
    }

    public T getNode(int index) {
        if (index < 0 || index > arcs.size())
            throw new IllegalArgumentException("Incorrect index of node");

        if (index == arcs.size())
            return arcs.get(index - 1).getNodeRight();
        else
            return arcs.get(index).getNodeLeft();
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

        sb.append(String.format("Маршрут \"%s\", %s, [ID: %d]: %n", number, direct, ID));
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
