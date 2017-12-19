package com.xten.cluster.common;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Description:
 * User: kongqingyu
 * Date: 2017/12/19
 */
public class Log4j2Test {

    private static final Logger LOG = LoggerFactory.getLogger(Log4j2Test.class);

    @Test
    public void log(){
        LOG.info("hello log world");
    }
}
