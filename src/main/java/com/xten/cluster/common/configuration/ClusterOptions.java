package com.xten.cluster.common.configuration;

import com.xten.cluster.common.util.NetUtils;

/**
 * Description:
 * User: kongqingyu
 * Date: 2018/1/4
 */
public class ClusterOptions {

    public static final ConfigOption<String> AGENT_RPC_HOST_OPTION = ConfigOptions
            .key(ConfigConstants.AGENT_RPC_HOST_KEY)
            .defaultValue(NetUtils.getNetAddress().getHostName());

    public static final ConfigOption<String> AGENT_RPC_ADDRESS_OPTION = ConfigOptions
            .key(ConfigConstants.AGENT_RPC_HOST_KEY)
            .defaultValue(NetUtils.getHostIPSafe());




}
