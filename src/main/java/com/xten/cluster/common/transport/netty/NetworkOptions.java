package com.xten.cluster.common.transport.netty;

import com.xten.cluster.common.configuration.ConfigConstants;
import com.xten.cluster.common.configuration.ConfigOption;
import com.xten.cluster.common.configuration.ConfigOptions;
import com.xten.cluster.common.unit.ByteSizeValue;
import com.xten.cluster.common.util.NetUtils;

/**
 * Description:
 * User: kongqingyu
 * Date: 2018/1/4
 */
public class NetworkOptions {

    public static final ConfigOption<Boolean> TCP_NO_DELAY_OPTION = ConfigOptions
            .key(NetworkConfigConstants.TCP_NO_DELAY_KEY)
            .defaultValue(true);

    public static final ConfigOption<Boolean> TCP_KEEP_ALIVE_OPTION = ConfigOptions
            .key(NetworkConfigConstants.TCP_KEEP_ALIVE_KEY)
            .defaultValue(true);

    public static final ConfigOption<ByteSizeValue> TCP_SEND_BUFFER_SIZE_OPTION = ConfigOptions
            .key(NetworkConfigConstants.TCP_SEND_BUFFER_SIZE_KEY)
            .defaultValue(new ByteSizeValue(-1));




}
