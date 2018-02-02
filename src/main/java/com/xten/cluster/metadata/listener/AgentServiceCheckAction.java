package com.xten.cluster.metadata.listener;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.xten.cluster.agent.AgentBootstrap;
import com.xten.cluster.agent.AgentService;
import com.xten.cluster.common.consul.listener.service.AbstractServiceCheckAction;
import com.xten.cluster.common.consul.listener.service.CachedService;
import com.xten.cluster.common.consul.listener.service.ServiceCacheListener;
import com.xten.cluster.common.util.AgentConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Description:
 * User: kongqingyu
 * Date: 2018/1/17
 */
public class AgentServiceCheckAction extends AbstractServiceCheckAction {

    private static final Logger LOG = LoggerFactory.getLogger(AgentServiceCheckAction.class);

    private final AgentService agentService;

    @Inject
    public AgentServiceCheckAction(ServiceCacheListener serviceCacheListener,
                                   AgentService agentService){
        this.agentService = agentService;
        serviceCacheListener.registerAction(this);
    }


    @Override
    public String getKey() {
        return AgentConstant.AgentType.AGENT.toTypeName();
    }

    @Override
    public void serviceInit(CachedService service) {
        agentService.heardAgentStarted(service.getId());
    }

    @Override
    public void serviceUp(CachedService service) {
        agentService.heardAgentStarted(service.getService());
    }

    @Override
    public void serviceDown(CachedService service) {
        agentService.heardAgentStopped(service.getService());
    }
}
