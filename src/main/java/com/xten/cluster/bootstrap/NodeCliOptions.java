package com.xten.cluster.bootstrap;

import org.apache.commons.cli.*;

/**
 * Description:
 * User: kongqingyu
 * Date: 2017/12/19
 */
public class NodeCliOptions {

    private String configDir;

    public final static String CONFIG_DIR = "configDir";

    public String getConfigDir() {
        return configDir;
    }

    public void setConfigDir(String configDir) {
        this.configDir = configDir;
    }


    /**
     * 解析启动参数
     * @param args
     * @return
     * @throws ParseException
     */
    public static NodeCliOptions parseArgs(String[] args) throws ParseException {

        Options options = new Options();
        options.addOption(optionalOption(NodeCliOptions.CONFIG_DIR,"配置文件目录"));

        CommandLineParser parser = new DefaultParser();

        CommandLine commandLine = parser.parse(options,args);

        NodeCliOptions nodeCliOptions = new NodeCliOptions();
        nodeCliOptions.setConfigDir(commandLine.getOptionValue(NodeCliOptions.CONFIG_DIR));

        return nodeCliOptions;
    }

    private static Option optionalOption(String opt,String description){

        Option option = new Option(opt,true,description);
        option.setOptionalArg(true);

        return option;
    }

}
