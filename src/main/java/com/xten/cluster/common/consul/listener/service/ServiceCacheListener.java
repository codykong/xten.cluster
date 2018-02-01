package com.xten.cluster.common.consul.listener.service;

import com.google.inject.Inject;
import com.orbitz.consul.HealthClient;
import com.orbitz.consul.cache.ServiceHealthCache;
import com.orbitz.consul.option.QueryOptions;
import com.xten.cluster.common.consul.ConsulService;
import com.xten.cluster.common.consul.listener.AbstractCacheListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Description:
 * User: kongqingyu
 * Date: 2018/01/17
 */
public class ServiceCacheListener extends AbstractCacheListener<ServiceHealthCache,ServiceCheckAction> {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceCacheListener.class);
    private final HealthClient healthClient;


    @Inject
    public ServiceCacheListener(ConsulService consulService){
        this.healthClient = consulService.getConsul().healthClient();
    }

    @Override
    public void registerAction(ServiceCheckAction cacheAction) {
        ServiceHealthCache serviceHealthCache = ServiceHealthCache.newCache(healthClient,cacheAction.getKey(),
                false, QueryOptions.BLANK,DEFAULT_WATCH_SECONDS);

        serviceHealthCache.addListener(cacheAction);
        addCache(serviceHealthCache);
    }
}
