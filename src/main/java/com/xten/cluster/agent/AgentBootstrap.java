package com.xten.cluster.agent;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.xten.cluster.common.configuration.Configuration;
import com.xten.cluster.common.configuration.ConfigurationModule;
import com.xten.cluster.common.consul.ConsulModule;
import com.xten.cluster.common.consul.listener.ListenerModule;
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
public class AgentBootstrap {

    private static final Logger LOG = LoggerFactory.getLogger(AgentBootstrap.class);

    private final Configuration configuration;
    private final AgentService clusterAgentService;


    @Inject
    public AgentBootstrap(Configuration configuration){
        this.configuration = configuration;

        ModulesBuilder modules = new ModulesBuilder();
        modules.add(new ConfigurationModule(configuration));
        modules.add(new ConsulModule());
        modules.add(new ListenerModule());
        modules.add(new TransportModule());
        modules.add(new ClusterMetaModule());

        modules.add(b -> {
            b.bind(AgentService.class).to(AgentServiceImpl.class).asEagerSingleton();
        });

        Injector injector = modules.createInjector();

        clusterAgentService = injector.getInstance(AgentService.class);

        addShutdownHook();
    }

    /**
     * 启动本节点
     */
    public void start(){
        try {
            clusterAgentService.start();
        } catch (Exception e) {
            LOG.error("start clusterNodeService error, stop begin:"+e.getMessage(),e);
            clusterAgentService.stop();
            LOG.error("start clusterNodeService error, stop end:");
            System.exit(-1);
        }
    }


    /**
     * 增加停止的钩子
     */
    private void addShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread() {

            @Override
            public void run() {
                clusterAgentService.stop();
            }

        });
    }


}
