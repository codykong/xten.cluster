package com.xten.cluster.common.transport;

import com.xten.cluster.common.lifecycle.Lifecycle;

/**
 * Description:
 * User: kongqingyu
 * Date: 2018/1/9
 */
public abstract class TcpTransport implements Transport {

    protected final Lifecycle lifecycleState = new Lifecycle();

    @Override
    public void start() {

        if (!lifecycleState.canMoveToStart()){
            return;
        }

        lifecycleState.moveToStarted();
        doStart();
        lifecycleState.started();
    }

    public abstract void doStart();

    @Override
    public void stop() {

    }

    @Override
    public void close() {

    }
}
