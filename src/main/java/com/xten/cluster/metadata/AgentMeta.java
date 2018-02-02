package com.xten.cluster.metadata;

import com.google.gson.Gson;
import com.xten.cluster.common.consul.meta.JsonMeta;
import com.xten.cluster.common.lifecycle.Lifecycle;
import com.xten.cluster.common.util.AgentConstant;

/**
 * Description:
 * User: kongqingyu
 * Date: 2018/1/2
 */
public class AgentMeta extends JsonMeta {
    private String name;
    private String ip;
    private String host;
    private Integer port;
    private Lifecycle.State status;
    private AgentConstant.AgentType type;

    public AgentMeta(String name, String ip, String host, Integer port, Lifecycle.State status){
        this(name,ip,host,port,status, AgentConstant.AgentType.AGENT);
    }

    public AgentMeta(String name, String ip, String host, Integer port, Lifecycle.State status,
                     AgentConstant.AgentType agentType){
        this.name = name;
        this.ip = ip;
        this.host = host;
        this.port = port;
        this.status = status;
        this.type = agentType;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Lifecycle.State getStatus() {
        return status;
    }

    public void setStatus(Lifecycle.State status) {
        this.status = status;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    @Override
    public String toJsonContent() {
        return new Gson().toJson(this);
    }

    @Override
    public String key() {
        return MetaKey.agent(name);
    }

    public String serviceId(){
        return MetaKey.agentServiceId(name);
    }

    public String checkId(){
        return MetaKey.agentCheckId(name);
    }

    public static AgentMeta fromJsonContent(String jsonContent) {
        return new Gson().fromJson(jsonContent,AgentMeta.class);
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public AgentConstant.AgentType getType() {
        return type;
    }

    public String getAgentTypeName() {
        return type.toTypeName();
    }

    public void setType(AgentConstant.AgentType type) {
        this.type = type;
    }
}
