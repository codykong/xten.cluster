package com.xten.cluster.common.consul.listener.service;

import com.xten.cluster.common.consul.listener.CacheChangeAction;

/**
 * Description:
 * User: kongqingyu
 * Date: 2018/01/17
 */
public interface ServiceCheckAction<K,V> extends CacheChangeAction<K,V> {

    boolean isPassing();

}
