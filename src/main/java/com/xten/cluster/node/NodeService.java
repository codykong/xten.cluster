package com.xten.cluster.node;

import com.xten.cluster.common.lifecycle.LifecycleComponent;

/**
 * Description:
 * User: kongqingyu
 * Date: 2017/11/25
 */
public interface NodeService extends LifecycleComponent {

    /**
     * 被通知节点已停止
     * @param nodeId
     */
    void heardNodeStopped(String nodeId);

    /**
     * 被通知节点已启动
     * @param nodeId
     */
    void heardNodeStarted(String nodeId);

}
