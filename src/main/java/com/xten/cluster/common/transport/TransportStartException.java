package com.xten.cluster.common.transport;

/**
 * Description:
 * User: kongqingyu
 * Date: 2018/1/4
 */
public class TransportStartException extends RuntimeException{


    public TransportStartException(String message){
        super(message);
    }

    public TransportStartException(String message, Throwable e){
        super(message,e);
    }

}
