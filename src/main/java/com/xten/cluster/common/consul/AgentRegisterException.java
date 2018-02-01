package com.xten.cluster.common.consul;

/**
 * Description:
 * User: kongqingyu
 * Date: 2018/1/4
 */
public class AgentRegisterException extends RuntimeException{


    public AgentRegisterException(String message){
        super(message);
    }

    public AgentRegisterException(String message, Throwable e){
        super(message,e);
    }

}
