package com.xten.cluster.common.consul.listener.kv;

import com.google.inject.Inject;
import com.xten.cluster.metadata.ClusterMetaCache;
import com.xten.cluster.metadata.MetaKey;
import com.xten.cluster.metadata.NodeMeta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Description:
 * User: kongqingyu
 * Date: 2018/1/16
 */
public class NodeKVChangedAction extends AbstractKVCacheAction{

    private static final Logger LOG = LoggerFactory.getLogger(NodeKVChangedAction.class);

    private final ClusterMetaCache clusterMetaCache;

    @Inject
    public NodeKVChangedAction(KVCacheListener kvCacheListener,
                               ClusterMetaCache clusterMetaCache){
        this.clusterMetaCache = clusterMetaCache;
        kvCacheListener.registerAction(this);
    }

    @Override
    public void changedKVNotify(List<CachedKV> changedKVs) {

        changedKVs.stream().forEach(p -> {
            if (p.getKvCacheCode() == CachedKV.KVCacheCode.REMOVED){
                clusterMetaCache.delNodeMeta(p.getKey());
                LOG.info("NodeKVChanged,status:{},key:{},value:{}",p.getKvCacheCode(),p.getKey(),p.getOldValueAsString());
            }else if (p.getValueAsString().isPresent()){
                clusterMetaCache.addNodeMeta(NodeMeta.fromJsonContent(p.getValueAsString().get()));
                LOG.info("NodeKVChanged,status:{},key:{},value:{}",p.getKvCacheCode(),p.getKey(),p.getValueAsString());
            }
        });
    }

    @Override
    public String getKey() {
        return MetaKey.node("localhost");
    }

}
