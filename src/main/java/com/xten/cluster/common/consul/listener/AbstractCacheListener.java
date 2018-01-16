package com.xten.cluster.common.consul.listener;

import com.orbitz.consul.cache.ConsulCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

/**
 * Description:
 * User: kongqingyu
 * Date: 2017/11/15
 */
public abstract class AbstractCacheListener<T extends ConsulCache,A extends CacheChangeAction> implements CacheListener<A> {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractCacheListener.class);

    private Set<T> caches = new HashSet<>();

    protected static final int DEFAULT_WATCH_SECONDS = 3;

    /**
     * 添加对应的cache
     * @param cache
     */
    protected void addCache(T cache){
        caches.add(cache);
    }

    @Override
    public void start() {

        caches.stream().forEach(p -> {
            try {
                p.start();
            } catch (Exception e) {
                LOG.info("ConsulCache start error,cache is:"+p);
                throw new RuntimeException("ConsulCache start error,cache is:"+p);
            }
        });


    }
}
