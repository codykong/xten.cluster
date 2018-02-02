package com.xten.cluster.metadata;


import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;

/**
 * Description:
 * User: kongqingyu
 * Date: 2018/1/3
 */
public class MetaKey {

    private static final String SEPARATOR = "/";
    private static final String SERVICE_SEPARATOR = ":";
    public static final String PREFIX_AGENT = "agent";
    public static final String PREFIX_ROOT = "cluster";

    public static String agentServiceId(String id){
        Preconditions.checkNotNull(id,"serviceId can not be null");
        return PREFIX_ROOT +  SERVICE_SEPARATOR + "service" + SERVICE_SEPARATOR + id;
    }

    public static String agentCheckId(String id){
        Preconditions.checkNotNull(id,"check can not be null");
        return PREFIX_ROOT +  SERVICE_SEPARATOR + "check" + SERVICE_SEPARATOR + id;
    }

    /**
     * node元数据对应的key
     * @param name 当为null时获取所有的node
     * @return
     */
    public static String agent(@Nullable String name){
        return generate(PREFIX_AGENT,name);
    }

    /**
     * 所有node对应的key
     * @return
     */
    public static String agents(){
        return agent(null);
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
