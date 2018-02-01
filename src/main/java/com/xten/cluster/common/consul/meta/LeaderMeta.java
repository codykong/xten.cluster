package com.xten.cluster.common.consul.meta;

import com.google.gson.Gson;

/**
 * Description:
 * User: kongqingyu
 * Date: 2018/1/17
 */
public class LeaderMeta extends JsonMeta{

    private String info;
    private String session;
    private String service;

    public LeaderMeta(String info,String session){
        this.info = info;
        this.session = session;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }


    @Override
    public String toJsonContent() {
        return new Gson().toJson(this);
    }

    public static LeaderMeta fromJsonContent(String jsonContent) {
        return new Gson().fromJson(jsonContent,LeaderMeta.class);
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public String key() {
        return service;
    }
}
