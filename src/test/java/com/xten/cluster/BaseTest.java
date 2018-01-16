package com.xten.cluster;

import com.google.inject.Injector;
import com.xten.cluster.bootstrap.Bootstrap;
import com.xten.cluster.common.configuration.ConfigConstants;
import com.xten.cluster.common.configuration.Configuration;
import com.xten.cluster.common.configuration.ConfigurationModule;
import com.xten.cluster.common.consul.ConsulModule;
import com.xten.cluster.common.inject.ModulesBuilder;
import org.junit.BeforeClass;

/**
 * Description:
 * User: kongqingyu
 * Date: 2018/1/16
 */
public class BaseTest {

    private static Injector injector;

    @BeforeClass
    public static void setUp() throws Exception {

        Configuration configuration = Bootstrap.loadConfiguration(ConfigConstants.CONFIGURATION_FILENAME,null);
        ModulesBuilder modules = new ModulesBuilder();

        modules.add(new ConfigurationModule(configuration));
        modules.add(new ConsulModule());

        injector = modules.createInjector();


    }

    protected Injector getInjector(){
        return injector;
    }
}
