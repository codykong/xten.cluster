package com.xten.cluster.metadata.listener;

import com.google.gson.Gson;
import com.google.inject.Inject;
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

    @Inject
    public AgentServiceCheckAction(ServiceCacheListener serviceCacheListener){

        serviceCacheListener.registerAction(this);
    }

    @Override
    public void changedServiceNotify(List<CachedService> changedServices) {

        LOG.info("changedService:{}",new Gson().toJson(changedServices));
    }


    @Override
    public String getKey() {
        return AgentConstant.AgentType.AGENT.toTypeName();
    }
}
