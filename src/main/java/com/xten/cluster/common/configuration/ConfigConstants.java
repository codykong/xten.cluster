package com.xten.cluster.common.configuration;

/**
 * Description:
 * User: kongqingyu
 * Date: 2017/10/26
 */
public class ConfigConstants {

    public static final String LOG_FILENAME = "log4j2.xml";
    public static final String CONFIGURATION_FILENAME = "cluster-conf.yaml";

    public static final String CONSUL_RPC_HOST_KEY = "consul.rpc.host";
    public static final String CONSUL_RPC_PORT_KEY = "consul.rpc.port";


    public static final String CLUSTER_NAME_KEY = "cluster.name";
    public static final String CLUSTER_ENVIRONMENT = "cluster.environment";

    //    节点的类型名称
    public static final String AGENT_NAME_KEY = "agent.name";

    //    节点的类型
    public static final String AGENT_TYPE_KEY = "agent.type";

    //    该节点可以提供的内存大小，固定值 ，优先于node.memory.percent
    public static final String AGENT_MEMORY_MB_KEY = "agent.memory.mb";

    //    该节点可以提供的内存大小，本机内存占比
    public static final String AGENT_MEMORY_PERCENT_KEY = "agent.memory.percent";

    //    节点的端口
    public static final String AGENT_RPC_PORT_KEY = "agent.rpc.port";
    // 通信host
    public static final String AGENT_RPC_HOST_KEY = "agent.rpc.host";
    // 通信ip
    public static final String AGENT_RPC_ADDRESS_KEY = "agent.rpc.address";





}
