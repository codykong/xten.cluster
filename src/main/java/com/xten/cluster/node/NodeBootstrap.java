package com.xten.cluster.node;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.xten.cluster.common.configuration.Configuration;
import com.xten.cluster.common.configuration.ConfigurationModule;
import com.xten.cluster.common.consul.ConsulModule;
import com.xten.cluster.common.inject.ModulesBuilder;
import com.xten.cluster.common.transport.TransportModule;
import com.xten.cluster.metadata.ClusterMetaModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Description:
 * User: kongqingyu
 * Date: 2017/12/27
 */
public class NodeBootstrap {

    private static final Logger LOG = LoggerFactory.getLogger(NodeBootstrap.class);

    private final Configuration configuration;
    private final NodeService clusterNodeService;


    @Inject
    public NodeBootstrap(Configuration configuration){
        this.configuration = configuration;

        ModulesBuilder modules = new ModulesBuilder();
        modules.add(new ConfigurationModule(configuration));
        modules.add(new ConsulModule());
        modules.add(new TransportModule());
        modules.add(new ClusterMetaModule());

        modules.add(b -> {
            b.bind(NodeService.class).to(NodeServiceImpl.class).asEagerSingleton();
        });

        Injector injector = modules.createInjector();

        clusterNodeService = injector.getInstance(NodeService.class);

        addShutdownHook();
    }

    /**
     * 启动本节点
     */
    public void start(){
        try {
            clusterNodeService.start();
        } catch (Exception e) {
            LOG.error("start clusterNodeService error, stop begin:"+e.getMessage(),e);
            clusterNodeService.stop();
            LOG.error("start clusterNodeService error, stop end:");
        }
    }


    /**
     * 增加停止的钩子
     */
    private void addShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread() {

            @Override
            public void run() {
                clusterNodeService.stop();
            }

        });
    }


}
