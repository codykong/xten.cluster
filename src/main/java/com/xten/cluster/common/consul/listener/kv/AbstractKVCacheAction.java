package com.xten.cluster.common.consul.listener.kv;

import com.orbitz.consul.model.kv.Value;
import com.xten.cluster.common.consul.listener.CacheChangeAction;

import java.util.*;

/**
 * Description:
 * User: kongqingyu
 * Date: 2017/11/28
 */
public abstract class AbstractKVCacheAction implements CacheChangeAction<String,Value> {

    private Map<String,CachedKV> concernedKV = null;

    @Override
    public void notify(Map<String, Value> newValues) {

        List<CachedKV> changedKVS = new ArrayList<>();
        Set<String> valueKeys = new HashSet<>();
        // 如果之前不存在，则说明为初始化
        if (concernedKV == null){
            initConcernedKV(newValues);
            concernedKV.values().stream().forEach(p -> {
                p.setKvCacheCode(CachedKV.KVCacheCode.INIT);
                changedKVS.add(p);
            });

        }else {
            // 先找出已存在的kv
            for (Map.Entry<String, Value> entry : newValues.entrySet()){
                CachedKV cacheKV = concernedKV.get(entry.getValue().getKey());
                // 如果不存在，则是新的KV
                if (cacheKV == null){
                    CachedKV newKV = new CachedKV(entry.getValue(),null);
                    newKV.setKvCacheCode(CachedKV.KVCacheCode.NEW);
                    changedKVS.add(newKV);
                    concernedKV.put(newKV.getKey(),newKV);

                }else {
                    if (!entry.getValue().getSession().equals(cacheKV.getSession()) ||
                            !entry.getValue().getValueAsString().equals(cacheKV.getValueAsString())){
                        cacheKV = new CachedKV(entry.getValue(),cacheKV);
                        cacheKV.setKvCacheCode(CachedKV.KVCacheCode.CHANGED);
                        concernedKV.put(cacheKV.getKey(),cacheKV);
                        changedKVS.add(cacheKV);
                    }

                }

                valueKeys.add(entry.getValue().getKey());

            }

            Set<String> deletedKeys = new HashSet<>();

            // 找出被删除的KV
            for (Map.Entry<String, CachedKV> entry : concernedKV.entrySet()){
                if (!valueKeys.contains(entry.getKey())){
                    entry.getValue().deletedKV();
                    changedKVS.add(entry.getValue());
                    deletedKeys.add(entry.getKey());
                }
            }

            // 本地缓存中删除已经被删除的key
            deletedKeys.stream().forEach(p -> {
                concernedKV.remove(p);
            });

        }

        changedKVNotify(changedKVS);
    }

    /**
     * 变更的kv提醒
     * @param changedKVs
     */
    public abstract void changedKVNotify(List<CachedKV> changedKVs);

    public void initConcernedKV(Map<String, Value> map){
        concernedKV = new HashMap<>();
        for (Map.Entry<String, Value> entry : map.entrySet()){
            concernedKV.put(entry.getValue().getKey(),
                    new CachedKV(entry.getValue(),null));
        }
    }
}

