package com.xten.cluster.common.consul;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.xten.cluster.metadata.NodeMeta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Description:
 * User: kongqingyu
 * Date: 2018/1/4
 */
public class NodeHealthService {

    private static final Logger LOG = LoggerFactory.getLogger(NodeHealthService.class);
    private static final String SERVICE_NODE = "node";
    private final ConsulService consulService;

    private ServiceCheck currentServiceCheck;

    @Inject
    public NodeHealthService(ConsulService consulService){
        this.consulService = consulService;
    }

    /**
     * 注册节点
     * @param nodeMeta
     */
    public void registerNode(NodeMeta nodeMeta){

        try {
            String serviceId = nodeMeta.key();
            consulService.registerService(nodeMeta.getIp(),nodeMeta.getPort(),serviceId,SERVICE_NODE);


        } catch (Exception e) {
            throw new NodeRegisterException(e.getMessage(),e);
        }
    }

    /**
     * 竞选Leader
     * @param nodeMeta
     * @throws Exception
     */
    public boolean electLeader(NodeMeta nodeMeta) {

        ServiceCheck serviceCheck = consulService.registerSession(nodeMeta.getIp(),nodeMeta.getPort(),
                nodeMeta.key(),SERVICE_NODE);

        currentServiceCheck = serviceCheck;

        Optional<String> leaderInfo = consulService.electLeaderForService(SERVICE_NODE,nodeMeta.key(),
                currentServiceCheck.getSessionId());

        // 如果当前服务是Leader，则进行标记
        return leaderInfo.isPresent() && leaderInfo.get().equals(currentServiceCheck.getServiceId());

    }

    /**
     * 释放Leader权限
     * @return
     */
    public void releaseLeader(NodeMeta nodeMeta){


        Preconditions.checkNotNull(currentServiceCheck,"currentServiceCheck is null,name is:"+nodeMeta.key());

        consulService.releaseLock(currentServiceCheck.getServiceId(),currentServiceCheck.getSessionId());
        consulService.destroySession(currentServiceCheck.getSessionId());
        consulService.deregisterCheck(currentServiceCheck.getSessionCheckId());

    }

    public void deregisterNode(NodeMeta nodeMeta){
        consulService.deregisterService(nodeMeta.key());
    }



}
