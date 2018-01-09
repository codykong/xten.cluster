package com.xten.cluster.common.configuration;

import com.xten.cluster.common.inject.AbstractModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Description:
 * User: kongqingyu
 * Date: 2017/10/27
 */
public class ConfigurationModule extends AbstractModule {

    private static final Logger LOG = LoggerFactory.getLogger(ConfigurationModule.class);

    private final Configuration configuration;

    public ConfigurationModule(Configuration configuration){
        this.configuration = configuration;
    }

    @Override
    protected void configure() {
        bind(Configuration.class).toInstance(configuration);
    }

    public Configuration getConfiguration() {
        return configuration;
    }
}
