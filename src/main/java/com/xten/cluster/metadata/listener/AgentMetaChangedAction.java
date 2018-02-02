package com.xten.cluster.metadata.listener;

import com.google.inject.Inject;
import com.xten.cluster.common.consul.listener.kv.AbstractKVCacheAction;
import com.xten.cluster.common.consul.listener.kv.CachedKV;
import com.xten.cluster.common.consul.listener.kv.KVCacheListener;
import com.xten.cluster.metadata.ClusterMetaCache;
import com.xten.cluster.metadata.MetaKey;
import com.xten.cluster.metadata.AgentMeta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Description:
 * User: kongqingyu
 * Date: 2018/1/16
 */
public class AgentMetaChangedAction extends AbstractKVCacheAction {

    private static final Logger LOG = LoggerFactory.getLogger(AgentMetaChangedAction.class);

    private final ClusterMetaCache clusterMetaCache;

    @Inject
    public AgentMetaChangedAction(KVCacheListener kvCacheListener,
                                  ClusterMetaCache clusterMetaCache){
        this.clusterMetaCache = clusterMetaCache;
        kvCacheListener.registerAction(this);
    }

    @Override
    public void changedKVNotify(List<CachedKV> changedKVs) {

        changedKVs.stream().forEach(p -> {
            if (p.getKvCacheCode() == CachedKV.KVCacheCode.REMOVED){
                clusterMetaCache.delNodeMeta(p.getKey());
            }else if (p.getValueAsString().isPresent()){
                clusterMetaCache.addNodeMeta(AgentMeta.fromJsonContent(p.getValueAsString().get()));
            }
        });
    }

    @Override
    public String getKey() {
        return MetaKey.agents();
    }

}
