package com.xten.cluster.common.consul.listener.service;

import com.google.common.collect.ImmutableList;
import com.orbitz.consul.model.health.Service;

/**
 * Description:
 * User: kongqingyu
 * Date: 2018/1/17
 */
public class CachedService {

    public enum CheckStatus{
        PASSING,CRITICAL
    }

    public enum ServiceStatus{
        INIT,UP,DOWN
    }

    private String id;
    private String service;
    private String address;
    private int port;
    private ImmutableList tags;
    private CheckStatus checkStatus;
    private ServiceStatus serviceStatus;


    public CachedService(Service service, CheckStatus status){
        this.id = service.getId();
        this.service = service.getService();
        this.address = service.getAddress();
        this.port = service.getPort();
        this.tags = ImmutableList.copyOf(service.getTags());
        this.checkStatus = status;

    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CachedService) || obj == null || !this.id.equals(((CachedService) obj).getId())){
            return false;
        }else {
            return true;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public ImmutableList getTags() {
        return tags;
    }

    public void setTags(ImmutableList tags) {
        this.tags = tags;
    }

    public CheckStatus getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(CheckStatus checkStatus) {
        this.checkStatus = checkStatus;
    }

    public ServiceStatus getServiceStatus() {
        return serviceStatus;
    }

    public void setServiceStatus(ServiceStatus serviceStatus) {
        this.serviceStatus = serviceStatus;
    }



}
