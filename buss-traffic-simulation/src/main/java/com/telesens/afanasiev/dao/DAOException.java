package com.telesens.afanasiev.dao;

/**
 * Created by oleg on 1/14/16.
 */
public class DAOException extends RuntimeException {
    public DAOException(String msg, Throwable e) {
        super(msg, e);
    }

    public DAOException(String msg) {
        super(msg);
    }
 }
