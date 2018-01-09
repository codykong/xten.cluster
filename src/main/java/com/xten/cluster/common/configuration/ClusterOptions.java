package com.xten.cluster.common.configuration;

import com.xten.cluster.common.util.NetUtils;

/**
 * Description:
 * User: kongqingyu
 * Date: 2018/1/4
 */
public class ClusterOptions {

    public static final ConfigOption<String> NODE_RPC_HOST_OPTION = ConfigOptions
            .key(ConfigConstants.NODE_RPC_HOST_KEY)
            .defaultValue(NetUtils.getNetAddress().getHostName());

    public static final ConfigOption<String> NODE_RPC_ADDRESS_OPTION = ConfigOptions
            .key(ConfigConstants.NODE_RPC_ADDRESS_KEY)
            .defaultValue(NetUtils.getHostIPSafe());




}
