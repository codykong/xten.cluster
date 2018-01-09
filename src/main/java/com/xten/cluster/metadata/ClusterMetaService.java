package com.xten.cluster.metadata;

import com.google.inject.Inject;
import com.xten.cluster.common.configuration.*;
import com.xten.cluster.common.consul.NodeHealthService;
import com.xten.cluster.common.lifecycle.Lifecycle;
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
    private final NodeConsulService nodeConsulService;
    private final NodeHealthService nodeHealthService;
    private NodeMeta currentNode;
    private boolean isLeader;

    @Inject
    public ClusterMetaService(Configuration configuration,
                              NodeConsulService nodeConsulService,
                              NodeHealthService nodeHealthService){
        this.configuration = configuration;
        this.nodeConsulService = nodeConsulService;
        this.nodeHealthService = nodeHealthService;
        // 初始化当前node
        currentNode();
    }

    /**
     * 获取远程FetchNode 信息
     * @return
     */
    public synchronized NodeMeta currentNode(){
        if (currentNode == null){
            NodeMeta configNodeMeta = buildFromConfiguration(configuration);

            NodeMeta nodeMeta = nodeConsulService.getNodeMetadata(configNodeMeta.getName());
            if (nodeMeta == null){
                nodeMeta = configNodeMeta;
            }else {
                mergeFromConfig(nodeMeta,configNodeMeta);
            }

            nodeMeta.setStatus(Lifecycle.State.INITIALIZED);
            nodeConsulService.upset(nodeMeta);
            currentNode = nodeMeta;
        }

        return currentNode;

    }

    /**
     * 注册本节点
     */
    public synchronized void registerNode(){
        nodeHealthService.registerNode(currentNode);
    }

    /**
     * 注销本节点
     */
    public synchronized void deregisterNode(){
        nodeHealthService.deregisterNode(currentNode);
    }

    /**
     * 释放leader
     */
    public synchronized void releaseLeader(){
        nodeHealthService.releaseLeader(currentNode);
    }


    /**
     * 选举主节点
     * @return
     */
    public synchronized void electLeader(){
        isLeader = nodeHealthService.electLeader(currentNode);
        if (isLeader){
            // do some thing that leader must do
        }
    }


    /**
     * 从配置文件中合并最新的节点配置
     * @param nodeMeta
     * @param configMeta
     */
    private void mergeFromConfig(NodeMeta nodeMeta, NodeMeta configMeta){
        nodeMeta.setIp(configMeta.getIp());
        nodeMeta.setHost(configMeta.getHost());
        nodeMeta.setPort(configMeta.getPort());
    }

    /**
     * 从配置文件中构造节点元数据
     * @param configuration
     * @return
     */
    private NodeMeta buildFromConfiguration(Configuration configuration){


        String nodeName = configuration.getString(ConfigConstants.NODE_NAME_KEY);
        // 以机器host进行划分
        if (nodeName == null){
            nodeName = configuration.getString(ConfigConstants.CONSUL_RPC_HOST_KEY);
        }

        int port = ClusterConfigValue.getNodeRpcPort(configuration);
        String host = configuration.getString(ClusterOptions.NODE_RPC_HOST_OPTION);
        String ip = configuration.getString(ClusterOptions.NODE_RPC_ADDRESS_OPTION);

        return new NodeMeta(nodeName,ip,host,port,Lifecycle.State.INITIALIZED);

    }

    /**
     * 设置本节点的状态
     * @param nodeStatus
     */
    public void currentNodeStatus(Lifecycle.State nodeStatus){
        currentNode.setStatus(nodeStatus);
        LOG.info("currentNodeStatus:{}",nodeStatus);
        nodeConsulService.upset(currentNode);
    }

    /**
     * 查看本节点是否是主节点
     * @return
     */
    public boolean isLeader(){
        return isLeader;
    }
}
