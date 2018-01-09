package com.xten.cluster.common.util;

/**
 * Description:
 * User: kongqingyu
 * Date: 2018/1/9
 */
public class Executors {

    /**
     * Returns the number of processors available but at most <tt>32</tt>.
     */
    public static int boundedNumberOfProcessors() {
        /* This relates to issues where machines with large number of cores
         * ie. >= 48 create too many threads and run into OOM see #3478
         * We just use an 32 core upper-bound here to not stress the system
         * too much with too many created threads */
        return Math.min(32, Runtime.getRuntime().availableProcessors());
    }

}
