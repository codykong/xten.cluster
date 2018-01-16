package com.xten.cluster.common.exception;

/**
 * Description:
 * User: kongqingyu
 * Date: 2018/1/12
 */
public class ParseException extends ClusterException {

    public ParseException(String msg, Object... args) {
        super(msg, args);
    }

    public ParseException(String msg) {
        super(msg);
    }

    public ParseException(String msg, Throwable e) {
        super(msg,e);
    }
}
