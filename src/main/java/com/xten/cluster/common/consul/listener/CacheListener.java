package com.xten.cluster.common.consul.listener;

/**
 * Description:
 * User: kongqingyu
 * Date: 2017/11/15
 */
public interface CacheListener<ACTION extends CacheChangeAction> {

    /**
     * 注册该Cache对应的Action
     * @param cacheAction
     */
    void registerAction(ACTION cacheAction);

    /**
     * 启动Listener
     */
    void start();
}
