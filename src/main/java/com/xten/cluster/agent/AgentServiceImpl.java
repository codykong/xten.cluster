package com.xten.cluster.agent;

import com.google.inject.Inject;
import com.xten.cluster.common.configuration.Configuration;
import com.xten.cluster.common.consul.listener.CacheListenerService;
import com.xten.cluster.common.lifecycle.Lifecycle;
import com.xten.cluster.common.transport.Transport;
import com.xten.cluster.metadata.ClusterMetaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Description:
 * User: kongqingyu
 * Date: 2017/12/27
 */
public class AgentServiceImpl implements AgentService {

    private static final Logger LOG = LoggerFactory.getLogger(AgentServiceImpl.class);

    private final Configuration configuration;
    private final ClusterMetaService clusterMetaService;
    private final Transport transport;
    private final CacheListenerService cacheListenerService;

    @Inject
    public AgentServiceImpl(Configuration configuration,
                            ClusterMetaService clusterMetaService,
                            Transport transport,
                            CacheListenerService cacheListenerService){
        this.configuration = configuration;
        this.clusterMetaService = clusterMetaService;
        this.transport = transport;
        this.cacheListenerService = cacheListenerService;
    }

    @Override
    public void start() {

        clusterMetaService.currentAgentStatus(Lifecycle.State.STARTING);

        //do something such as start transport
        startRPC();

        //do something that you need to initialize your service

        clusterMetaService.registerAgent();

        clusterMetaService.currentAgentStatus(Lifecycle.State.RUNNING);

        clusterMetaService.electLeader();

        cacheListenerService.start();
    }

    @Override
    public void stop() {

        clusterMetaService.currentAgentStatus(Lifecycle.State.STOPPING);

        clusterMetaService.releaseLeader();

        //do something that you need to do before stop your service

        clusterMetaService.deregisterAgent();

        clusterMetaService.currentAgentStatus(Lifecycle.State.STOPPED);

    }

    /**
     * 启动远程通信
     */
    private void startRPC(){

        transport.start();
    }

    @Override
    public void close() {

    }

    @Override
    public void heardAgentStopped(String nodeId) {

    }

    @Override
    public void heardAgentStarted(String nodeId) {

    }
}
