package com.xten.cluster.common.configuration;

/**
 * Description:
 * User: kongqingyu
 * Date: 2018/1/4
 */
public class ClusterConfigValue {

    /**
     * 获取通信的端口
     * @param configuration
     * @return
     */
    public static int getNodeRpcPort (Configuration configuration){
        if (!configuration.containsKey(ConfigConstants.NODE_RPC_PORT_KEY)) {
            throw new IllegalConfigurationException(ConfigConstants.NODE_RPC_PORT_KEY + "must be set");
        }
        return configuration.getInteger(ConfigConstants.NODE_RPC_PORT_KEY, 0);
    }
}
