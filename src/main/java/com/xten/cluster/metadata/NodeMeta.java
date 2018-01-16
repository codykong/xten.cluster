package com.xten.cluster.metadata;

import com.google.gson.Gson;
import com.xten.cluster.common.lifecycle.Lifecycle;

/**
 * Description:
 * User: kongqingyu
 * Date: 2018/1/2
 */
public class NodeMeta extends JsonMeta {
    private String name;
    private String ip;
    private String host;
    private Integer port;
    private Lifecycle.State status;

    public NodeMeta(String name, String ip, String host, Integer port, Lifecycle.State status){
        this.name = name;
        this.ip = ip;
        this.host = host;
        this.port = port;
        this.status = status;
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
        return MetaKey.node(name);
    }

    public static NodeMeta fromJsonContent(String jsonContent) {
        return new Gson().fromJson(jsonContent,NodeMeta.class);
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
