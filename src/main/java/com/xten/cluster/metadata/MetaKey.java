package com.xten.cluster.metadata;


import org.apache.commons.lang3.StringUtils;

/**
 * Description:
 * User: kongqingyu
 * Date: 2018/1/3
 */
public class MetaKey {

    private static final String SEPARATOR = "/";
    public static final String PREFIX_NODE = "node";
    public static final String PREFIX_ROOT = "cluster";


    /**
     * node元数据对应的key
     * @param name
     * @return
     */
    public static String node(String name){
        return generate(PREFIX_NODE,name);
    }

    /**
     * 选主所在的节点
     * @param serviceName
     * @return
     */
    public static String getServiceLeaderKey(String serviceName) {
        return SEPARATOR + PREFIX_ROOT +"/service/" + serviceName + "/leader";
    }

    /**
     * 根据多个参数生成
     * @param args
     * @return
     */
    public static String generate(String... args){

        return  SEPARATOR + PREFIX_ROOT + SEPARATOR + StringUtils.join(args,SEPARATOR);
    }

}