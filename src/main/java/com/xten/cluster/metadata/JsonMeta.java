package com.xten.cluster.metadata;

/**
 * Description:
 * User: kongqingyu
 * Date: 2018/1/3
 */
public interface JsonMeta {

    /**
     * 序列化为json格式
     * @return
     */
    String toJsonContent();

    String key();

}
