package com.xten.cluster.common.consul.listener.kv;

import com.google.inject.Inject;
import com.xten.cluster.common.util.AgentConstant;
import com.xten.cluster.metadata.ClusterMetaService;
import com.xten.cluster.metadata.MetaKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
/**
 * Description:
 * User: kongqingyu
 * Date: 2018/1/17
 */
public class LeaderChangedAction extends AbstractKVCacheAction {

    private static final Logger LOG = LoggerFactory.getLogger(LeaderChangedAction.class);

    private final ClusterMetaService clusterMetaService;

    @Inject
    public LeaderChangedAction(KVCacheListener kvCacheListener,
                               ClusterMetaService clusterMetaService){
        this.clusterMetaService = clusterMetaService;
        kvCacheListener.registerAction(this);
    }

    @Override
    public void changedKVNotify(List<CachedKV> changedKVs) {
        // 如果Leader不存在，则重新选主
        changedKVs.forEach(p -> {
            if (!p.getSession().isPresent()){
                clusterMetaService.electLeader();
            }
        });

    }

    @Override
    public String getKey() {
        return MetaKey.getServiceLeaderKey(AgentConstant.AgentType.AGENT.name());
    }
}
