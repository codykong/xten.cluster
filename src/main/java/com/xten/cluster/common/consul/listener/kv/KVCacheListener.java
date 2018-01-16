package com.xten.cluster.common.consul.listener.kv;

import com.google.inject.Inject;
import com.orbitz.consul.KeyValueClient;
import com.orbitz.consul.cache.KVCache;
import com.xten.cluster.common.consul.ConsulService;
import com.xten.cluster.common.consul.listener.AbstractCacheListener;
import com.xten.cluster.common.consul.listener.CacheChangeAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Description:
 * User: kongqingyu
 * Date: 2017/11/3
 */
public class KVCacheListener extends AbstractCacheListener<KVCache,CacheChangeAction> {

    private static final Logger LOG = LoggerFactory.getLogger(KVCacheListener.class);
    private final KeyValueClient keyValueClient;


    @Inject
    public KVCacheListener(ConsulService consulService){

        this.keyValueClient = consulService.getConsul().keyValueClient();
    }

    @Override
    public void registerAction(CacheChangeAction cacheAction) {
        String key = cacheAction.getKey();
        KVCache kvCache = KVCache.newCache(keyValueClient, key,DEFAULT_WATCH_SECONDS);
        kvCache.addListener(cacheAction);
        addCache(kvCache);
    }
}
