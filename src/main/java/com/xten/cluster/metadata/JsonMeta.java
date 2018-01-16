package com.xten.cluster.metadata;

/**
 * Description:
 * User: kongqingyu
 * Date: 2018/1/3
 */
public abstract class JsonMeta implements Metadata{

    public final String toContent(){
        return toJsonContent();
    }

    /**
     * 序列化为json格式
     * @return
     */
    protected abstract String toJsonContent();


}
