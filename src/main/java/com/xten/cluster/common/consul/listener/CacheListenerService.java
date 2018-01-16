package com.xten.cluster.common.consul.listener;

import com.google.inject.Inject;
import com.xten.cluster.common.consul.listener.kv.KVCacheListener;

/**
 * Description:
 * User: kongqingyu
 * Date: 2017/11/15
 */
public class CacheListenerService {

    private final KVCacheListener kvCacheListener;

    @Inject
    public CacheListenerService(KVCacheListener kvCacheListener){

        this.kvCacheListener = kvCacheListener;
    }


    public void start(){
        kvCacheListener.start();
    }
}

