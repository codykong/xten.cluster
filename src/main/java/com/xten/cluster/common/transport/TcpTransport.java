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

        boolean success = false;
        try {
            if (!lifecycleState.canMoveToStart()){
                return;
            }

            lifecycleState.moveToStarted();
            doStart();
            lifecycleState.started();
            success = true;
        } finally {
            if (!success){
                stop();
            }
        }
    }

    public abstract void doStart();

    @Override
    public void stop() {

    }

    @Override
    public void close() {

    }
}
