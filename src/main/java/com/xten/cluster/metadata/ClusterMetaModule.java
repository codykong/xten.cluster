package com.xten.cluster.metadata;

import com.xten.cluster.common.consul.ConsulService;
import com.xten.cluster.common.inject.AbstractModule;

/**
 * Description:
 * User: kongqingyu
 * Date: 2017/10/25
 */
public class ClusterMetaModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ClusterMetaService.class).asEagerSingleton();
        bind(NodeConsulService.class).asEagerSingleton();
    }


}
