package com.xten.cluster.metadata;

import com.xten.cluster.common.inject.AbstractModule;
import com.xten.cluster.metadata.listener.AgentServiceCheckAction;
import com.xten.cluster.metadata.listener.AgentMetaChangedAction;

/**
 * Description:
 * User: kongqingyu
 * Date: 2017/10/25
 */
public class ClusterMetaModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ClusterMetaService.class).asEagerSingleton();
        bind(AgentConsulService.class).asEagerSingleton();
        bind(AgentMetaChangedAction.class).asEagerSingleton();
        bind(AgentServiceCheckAction.class).asEagerSingleton();
    }


}
