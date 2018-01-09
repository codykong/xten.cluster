package com.xten.cluster.common.consul;

/**
 * Description:
 * User: kongqingyu
 * Date: 2018/1/4
 */
public class NodeRegisterException extends RuntimeException{


    public NodeRegisterException(String message){
        super(message);
    }

    public NodeRegisterException(String message,Throwable e){
        super(message,e);
    }

}
