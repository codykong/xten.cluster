package com.xten.cluster.common.consul.listener;

import com.orbitz.consul.cache.ConsulCache;

/**
 * Description:
 * User: kongqingyu
 * Date: 2017/11/1
 */
public interface CacheChangeAction<K,V> extends ConsulCache.Listener<K,V> {

    /**
     * 获取该元数据对应的key
     * @return
     */
    String getKey();


}
