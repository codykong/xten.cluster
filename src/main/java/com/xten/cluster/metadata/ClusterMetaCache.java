package com.xten.cluster.metadata;

import com.google.inject.Inject;
import com.xten.cluster.common.consul.meta.Metadata;

import java.util.HashMap;
import java.util.Map;

/**
 * Description:
 * User: kongqingyu
 * Date: 2018/1/16
 */
public class ClusterMetaCache {

    private final Map<String,Metadata> nodeMetaMap = new HashMap<>();

    @Inject
    public ClusterMetaCache(){ }

    public void addNodeMeta(Metadata nodeMeta){
        nodeMetaMap.put(nodeMeta.key(),nodeMeta);
    }

    public AgentMeta getNodeMeta(String key){
        return (AgentMeta)nodeMetaMap.get(key);
    }

    public void delNodeMeta(String key){
        nodeMetaMap.remove(key);
    }

    public Map<String,Metadata> nodeMetaMap(){
        return nodeMetaMap;
    }
}
