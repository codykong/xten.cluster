package com.xten.cluster.common.consul;

import com.xten.cluster.BaseTest;
import com.xten.cluster.metadata.MetaKey;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Description:
 * User: kongqingyu
 * Date: 2018/1/16
 */
public class ConsulServiceTest extends BaseTest{

    private static final Logger LOG = LoggerFactory.getLogger(ConsulServiceTest.class);
    private ConsulService consulService;

    @Before
    public void before(){
        consulService= getInjector().getInstance(ConsulService.class);
    }

    @Test
    public void getConsul() throws Exception {
    }

    @Test
    public void releaseLock() throws Exception {
    }

    @Test
    public void registerService() throws Exception {
    }

    @Test
    public void deregisterService() throws Exception {
    }

    @Test
    public void electLeaderForService() throws Exception {
    }

    @Test
    public void getLeaderInfoForService() throws Exception {
    }

    @Test
    public void registerSession() throws Exception {
    }

    @Test
    public void destroySession() throws Exception {
    }

    @Test
    public void deregisterCheck() throws Exception {
    }

    @Test
    public void putValue() throws Exception {
    }

    @Test
    public void putValue1() throws Exception {
    }

    @Test
    public void getOptionalValue() throws Exception {
    }

    @Test
    public void getValue() throws Exception {
    }

    @Test
    public void getValuesAsString() throws Exception {

        List<String> nodes = consulService.getValuesAsString(MetaKey.nodes());
        LOG.info("nodes:{}",nodes);

    }

    @Test
    public void deleteKey() throws Exception {
    }

    @Test
    public void deleteKeys() throws Exception {
    }

    @Test
    public void getKeys() throws Exception {
    }

}