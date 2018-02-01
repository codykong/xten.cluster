package com.xten.cluster.metadata;

import com.google.inject.Inject;
import com.xten.cluster.common.configuration.*;
import com.xten.cluster.common.consul.AgentHealthService;
import com.xten.cluster.common.lifecycle.Lifecycle;
import com.xten.cluster.common.util.AgentConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Description:
 * User: kongqingyu
 * Date: 2018/1/2
 */
public class ClusterMetaService {

    private static final Logger LOG = LoggerFactory.getLogger(ClusterMetaService.class);

    private final Configuration configuration;
    private final AgentConsulService agentConsulService;
    private final AgentHealthService agentHealthService;
    private AgentMeta currentAgent;
    private boolean isLeader;

    @Inject
    public ClusterMetaService(Configuration configuration,
                              AgentConsulService agentConsulService,
                              AgentHealthService agentHealthService){
        this.configuration = configuration;
        this.agentConsulService = agentConsulService;
        this.agentHealthService = agentHealthService;
        // 初始化当前node
        currentAgent();
    }

    /**
     * 获取远程FetchAgent 信息
     * @return
     */
    public synchronized AgentMeta currentAgent(){
        if (currentAgent == null){
            AgentMeta configAgentMeta = buildFromConfiguration(configuration);

            AgentMeta agentMeta = agentConsulService.getAgentMetadata(configAgentMeta.getName());
            if (agentMeta == null){
                agentMeta = configAgentMeta;
            }else {
                mergeFromConfig(agentMeta,configAgentMeta);
            }

            agentMeta.setStatus(Lifecycle.State.INITIALIZED);
            agentConsulService.upset(agentMeta);
            currentAgent = agentMeta;
        }

        return currentAgent;

    }

    /**
     * 注册本节点
     */
    public synchronized void registerAgent(){
        agentHealthService.registerAgent(currentAgent);
    }

    /**
     * 注销本节点
     */
    public synchronized void deregisterAgent(){
        agentHealthService.deregisterAgent(currentAgent);
    }

    /**
     * 释放leader
     */
    public synchronized void releaseLeader(){
        agentHealthService.releaseLeader(currentAgent);
    }


    /**
     * 选举主节点
     * @return
     */
    public synchronized void electLeader(){
        isLeader = agentHealthService.electLeader(currentAgent);
        if (isLeader){
            // do some thing that leader must do
        }
    }


    /**
     * 从配置文件中合并最新的节点配置
     * @param agentMeta
     * @param configMeta
     */
    private void mergeFromConfig(AgentMeta agentMeta, AgentMeta configMeta){
        agentMeta.setIp(configMeta.getIp());
        agentMeta.setHost(configMeta.getHost());
        agentMeta.setPort(configMeta.getPort());
        agentMeta.setType(configMeta.getType());

    }

    /**
     * 从配置文件中构造节点元数据
     * @param configuration
     * @return
     */
    private AgentMeta buildFromConfiguration(Configuration configuration){


        String agentName = configuration.getString(ConfigConstants.AGENT_NAME_KEY);
        // 以机器host进行划分
        if (agentName == null){
            agentName = configuration.getString(ConfigConstants.CONSUL_RPC_HOST_KEY);
        }

        int port = ClusterConfigValue.getAgentRpcPort(configuration);
        String host = configuration.getString(ClusterOptions.AGENT_RPC_HOST_OPTION);
        String ip = configuration.getString(ClusterOptions.AGENT_RPC_ADDRESS_OPTION);

        AgentConstant.AgentType agentType = AgentConstant.AgentType.valueOf(
                configuration.getString(ConfigConstants.AGENT_TYPE_KEY).toUpperCase());

        return new AgentMeta(agentName,ip,host,port,Lifecycle.State.INITIALIZED,agentType);

    }

    /**
     * 设置本节点的状态
     * @param agentStatus
     */
    public void currentAgentStatus(Lifecycle.State agentStatus){
        currentAgent.setStatus(agentStatus);
        LOG.info("currentAgentStatus:{}",agentStatus);
        agentConsulService.upset(currentAgent);
    }

    /**
     * 查看本节点是否是主节点
     * @return
     */
    public boolean isLeader(){
        return isLeader;
    }
}
