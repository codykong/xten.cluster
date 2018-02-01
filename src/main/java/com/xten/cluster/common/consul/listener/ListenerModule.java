package com.xten.cluster.common.consul.listener;

import com.xten.cluster.common.consul.listener.kv.KVCacheListener;
import com.xten.cluster.common.consul.listener.kv.LeaderChangedAction;
import com.xten.cluster.common.consul.listener.service.ServiceCacheListener;
import com.xten.cluster.common.inject.AbstractModule;

/**
 * Description:
 * User: kongqingyu
 * Date: 2018/1/16
 */
public class ListenerModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(KVCacheListener.class).asEagerSingleton();
        bind(ServiceCacheListener.class).asEagerSingleton();

        bind(CacheListenerService.class).asEagerSingleton();
        bind(LeaderChangedAction.class).asEagerSingleton();
    }
}
