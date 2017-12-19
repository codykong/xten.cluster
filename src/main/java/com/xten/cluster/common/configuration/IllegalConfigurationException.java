package com.xten.cluster.common.configuration;


/**
 * Description:
 * User: kongqingyu
 * Date: 2017/9/12
 */
public class IllegalConfigurationException extends RuntimeException {

    public IllegalConfigurationException(String msg) {
        super(msg);
    }

    public IllegalConfigurationException(String msg, Throwable e) {
        super(msg,e);
    }


}
