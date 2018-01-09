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
    public static final String NODE_NAME_KEY = "node.name";

    //    节点的类型
    public static final String NODE_TYPE_KEY = "node.type";

    //    该节点可以提供的内存大小，固定值 ，优先于node.memory.percent
    public static final String NODE_MEMORY_MB_KEY = "node.memory.mb";

    //    该节点可以提供的内存大小，本机内存占比
    public static final String NODE_MEMORY_PERCENT_KEY = "node.memory.percent";

    //    节点的端口
    public static final String NODE_RPC_PORT_KEY = "node.rpc.port";
    // 通信host
    public static final String NODE_RPC_HOST_KEY = "node.rpc.host";
    // 通信ip
    public static final String NODE_RPC_ADDRESS_KEY = "node.rpc.address";





}
