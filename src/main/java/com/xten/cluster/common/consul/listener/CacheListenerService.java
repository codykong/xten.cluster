package com.xten.cluster.common.consul.listener;

import com.google.inject.Inject;
import com.xten.cluster.common.consul.listener.kv.KVCacheListener;
import com.xten.cluster.common.consul.listener.service.ServiceCacheListener;

/**
 * Description:
 * User: kongqingyu
 * Date: 2017/11/15
 */
public class CacheListenerService {

    private final KVCacheListener kvCacheListener;
    private final ServiceCacheListener serviceCacheListener;

    @Inject
    public CacheListenerService(KVCacheListener kvCacheListener,
                                ServiceCacheListener serviceCacheListener){

        this.kvCacheListener = kvCacheListener;
        this.serviceCacheListener = serviceCacheListener;
    }


    public void start(){
        serviceCacheListener.start();
        kvCacheListener.start();
    }
}

