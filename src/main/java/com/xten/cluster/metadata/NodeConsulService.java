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
public class NodeConsulService {
    private final Logger LOG = LoggerFactory.getLogger(NodeConsulService.class);
    private final ConsulService consulService;

    @Inject
    public NodeConsulService(ConsulService consulService){
        this.consulService = consulService;
    }


    @Nullable
    public NodeMeta getNodeMetadata(String name){

        Optional<String> optionalValue = consulService.getOptionalValue(MetaKey.node(name));
        if (optionalValue.isPresent()){
            return NodeMeta.fromJsonContent(optionalValue.get());
        }else {
            return null;
        }
    }

    public void upset(NodeMeta nodeMetadata){

        consulService.putValue(nodeMetadata);
    }



}
