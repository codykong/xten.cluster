package com.xten.cluster.agent;

import com.xten.cluster.common.lifecycle.LifecycleComponent;

/**
 * Description:
 * User: kongqingyu
 * Date: 2017/11/25
 */
public interface AgentService extends LifecycleComponent {

    /**
     * 被通知节点已停止
     * @param nodeId
     */
    void heardAgentStopped(String nodeId);

    /**
     * 被通知节点已启动
     * @param nodeId
     */
    void heardAgentStarted(String nodeId);

}
