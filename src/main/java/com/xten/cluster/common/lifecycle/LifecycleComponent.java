package com.xten.cluster.common.lifecycle;

import com.xten.cluster.common.release.Releasable;

/**
 * Description:
 * User: kongqingyu
 * Date: 2018/1/9
 */
public interface LifecycleComponent extends Releasable {

    void start();

    void stop();
}
