package com.xten.cluster.common.consul.meta;

/**
 * Description:
 * User: kongqingyu
 * Date: 2018/1/16
 */
public interface Metadata {

    /**
     * 序列化为文本
     * @return
     */
    String toContent();

    String key();


}
