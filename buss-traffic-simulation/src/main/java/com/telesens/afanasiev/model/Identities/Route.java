package com.telesens.afanasiev.model.Identities;

import java.util.Iterator;

/**
 * Created by oleg on 1/14/16.
 */
public interface Route<T> extends Identity, Iterable<Arc<T>> {
    String getNumber();
    String getDescription();
    Direct getDirect();
    double getCost();
    T getFirstNode();

    void setNumber(String number);
    void setDescription(String description);
    void setCost(double cost);

    T getLastNode();
    T getNode(int index);
    void createLastArc(long arcID, T rightNode, int duration) throws NoSuchFieldException, IllegalAccessException;
    TypeOfRoute getType();
    Iterator<Arc<T>> iterator();
}
