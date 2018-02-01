package com.xten.cluster.metadata;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.xten.cluster.common.consul.ConsulService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;

/**
 * Description:
 * User: kongqingyu
 * Date: 2018/1/3
 */
public class AgentConsulService {
    private final Logger LOG = LoggerFactory.getLogger(AgentConsulService.class);
    private final ConsulService consulService;

    @Inject
    public AgentConsulService(ConsulService consulService){
        this.consulService = consulService;
    }


    @Nullable
    public AgentMeta getAgentMetadata(String name){

        Optional<String> optionalValue = consulService.getOptionalValue(MetaKey.agent(name));
        if (optionalValue.isPresent()){
            return AgentMeta.fromJsonContent(optionalValue.get());
        }else {
            return null;
        }
    }

    public void upset(AgentMeta agentMeta){

        consulService.putValue(agentMeta);
    }



}
