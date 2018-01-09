package com.xten.cluster.common.transport;

import com.google.inject.name.Names;
import com.xten.cluster.common.consul.ConsulService;
import com.xten.cluster.common.inject.AbstractModule;
import com.xten.cluster.common.transport.netty.NettyTransport;

/**
 * Description:
 * User: kongqingyu
 * Date: 2017/10/25
 */
public class TransportModule extends AbstractModule {

    @Override
    protected void configure() {
//        bind(TcpTransport.class).annotatedWith(Names.named("nettyTransport")).to(NettyTransport.class).asEagerSingleton();
        bind(Transport.class).to(NettyTransport.class).asEagerSingleton();
    }


}
