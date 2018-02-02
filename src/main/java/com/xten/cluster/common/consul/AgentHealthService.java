package com.xten.cluster.common.consul;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.xten.cluster.metadata.AgentMeta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Description:
 * User: kongqingyu
 * Date: 2018/1/4
 */
public class AgentHealthService {

    private static final Logger LOG = LoggerFactory.getLogger(AgentHealthService.class);
    private final ConsulService consulService;

    private ServiceCheck currentServiceCheck;

    @Inject
    public AgentHealthService(ConsulService consulService){
        this.consulService = consulService;
    }

    /**
     * 注册节点
     * @param agentMeta
     */
    public void registerAgent(AgentMeta agentMeta){

        try {
            consulService.registerService(agentMeta.getIp(),agentMeta.getPort(),agentMeta.serviceId(),agentMeta.getAgentTypeName());


        } catch (Exception e) {
            throw new AgentRegisterException(e.getMessage(),e);
        }
    }

    /**
     * 竞选Leader
     * @param nodeMeta
     * @throws Exception
     */
    public boolean electLeader(AgentMeta nodeMeta) {

        currentServiceCheck = consulService.registerSession(nodeMeta.getIp(),nodeMeta.getPort(),
                nodeMeta.serviceId(),nodeMeta.getAgentTypeName());

        Optional<String> leaderInfo = consulService.electLeaderForService(nodeMeta.getType().name(),nodeMeta.getName(),
                currentServiceCheck.getSessionId());

        // 如果当前服务是Leader，则进行标记
        return leaderInfo.isPresent() && leaderInfo.get().equals(currentServiceCheck.getServiceId());

    }

    /**
     * 释放Leader权限
     * @return
     */
    public void releaseLeader(AgentMeta nodeMeta){


        Preconditions.checkNotNull(currentServiceCheck,"currentServiceCheck is null,name is:"+nodeMeta.checkId());

        consulService.releaseLock(currentServiceCheck.getServiceId(),currentServiceCheck.getSessionId());
        consulService.destroySession(currentServiceCheck.getSessionId());
        consulService.deregisterCheck(currentServiceCheck.getSessionCheckId());

    }

    public void deregisterAgent(AgentMeta nodeMeta){
        consulService.deregisterService(nodeMeta.serviceId());
    }



}
