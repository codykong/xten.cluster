package com.xten.cluster.bootstrap;

import com.xten.cluster.common.configuration.ConfigConstants;
import com.xten.cluster.common.configuration.Configuration;
import com.xten.cluster.common.configuration.IllegalConfigurationException;
import com.xten.cluster.agent.AgentBootstrap;
import org.apache.commons.cli.*;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;

/**
 * Description:
 * User: kongqingyu
 * Date: 2017/12/19
 */
public class Bootstrap {

    private static Logger LOG = LoggerFactory.getLogger(Bootstrap.class);

    public static void main(String[] args) throws FileNotFoundException, ParseException {

        NodeCliOptions nodeCliOptions = NodeCliOptions.parseArgs(args);

        loadLogFile(ConfigConstants.LOG_FILENAME,nodeCliOptions.getConfigDir());
        Configuration configuration = loadConfiguration(ConfigConstants.CONFIGURATION_FILENAME,nodeCliOptions.getConfigDir());

        LOG.info("configuration:{}",configuration.keySet());

        AgentBootstrap clusterNode = new AgentBootstrap(configuration);
        clusterNode.start();

    }

    /**
     * 加载日志文件
     * @param logFile 日志文件名称
     * @param configDir 日志文件目录
     * @throws FileNotFoundException
     */
    private static void loadLogFile(String logFile,String configDir) throws FileNotFoundException {

        if (configDir == null){
            return;
        }

        final File confDirFile = new File(configDir);
        if (!(confDirFile.exists())) {
            throw new IllegalConfigurationException(
                    "The given configuration directory name '" + configDir +
                            "' (" + confDirFile.getAbsolutePath() + ") does not describe an existing directory.");
        }

        final File logConfigFile = new File(confDirFile, logFile);

        ConfigurationSource source = new ConfigurationSource(new FileInputStream(logConfigFile),logConfigFile);
        Configurator.initialize(null, source);

        LOG = LoggerFactory.getLogger(Bootstrap.class);
        LOG.info("extra log config start");
    }

    /**
     * 加载配置文件
     * @param configFile 配置文件名称
     * @param configDir 配置文件目录
     * @return
     * @throws FileNotFoundException
     */
    public static Configuration loadConfiguration(String configFile, String configDir) throws FileNotFoundException {

        Yaml yaml = new Yaml();

//        如果未指定配置文件路径，则从classpath中加载
        if (configDir == null){
            InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(configFile);

            if (inputStream !=null){
                Map<String, Object> property = (Map<String, Object>) yaml.load(inputStream);
                return new Configuration(property);
            }
        }

        if (configDir == null) {
            throw new IllegalArgumentException("Given configuration directory is null, cannot load configuration");
        }

        final File confDirFile = new File(configDir);
        if (!(confDirFile.exists())) {
            throw new IllegalConfigurationException(
                    "The given configuration directory name '" + configDir +
                            "' (" + confDirFile.getAbsolutePath() + ") does not describe an existing directory.");
        }

        // get yaml configuration file
        final File yamlConfigFile = new File(confDirFile, configFile);

        if (!yamlConfigFile.exists()) {
            throw new IllegalConfigurationException(
                    "The config file '" + yamlConfigFile +
                            "' (" + confDirFile.getAbsolutePath() + ") does not exist.");
        }


        Map<String, Object>  property = (Map<String, Object>) yaml.load(new FileInputStream(yamlConfigFile));
        return new Configuration(property);
    }




}
