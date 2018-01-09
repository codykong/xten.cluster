package com.xten.cluster.common.consul;

/**
 * Description:
 * User: kongqingyu
 * Date: 2017/11/27
 */
public class ServiceCheck {

    private String serviceId;

    private String sessionId;
    private String sessionCheckId;

    public ServiceCheck(String sessionId, String checkId,String serviceId){
        this.sessionId = sessionId;
        this.sessionCheckId = checkId;
        this.serviceId = serviceId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }


    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getSessionCheckId() {
        return sessionCheckId;
    }

    public void setSessionCheckId(String sessionCheckId) {
        this.sessionCheckId = sessionCheckId;
    }
}
