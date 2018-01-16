package com.xten.cluster.common.consul;

import com.xten.cluster.common.inject.AbstractModule;

/**
 * Description:
 * User: kongqingyu
 * Date: 2017/10/25
 */
public class ConsulModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ConsulService.class).asEagerSingleton();

    }


}
