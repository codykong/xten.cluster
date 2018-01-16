package com.xten.cluster.common.consul.listener.kv;

import com.google.common.base.Optional;
import com.orbitz.consul.model.kv.Value;

/**
 * Description:
 * User: kongqingyu
 * Date: 2017/11/28
 */
public class CachedKV {

    private String key;
    private long createIndex;
    private long modifyIndex;
    private Optional<String> session = Optional.absent();
    private Optional<String> oldSession = Optional.absent();
    private Optional<String> valueAsString = Optional.absent();
    private Optional<String> oldValueAsString = Optional.absent();
    private KVCacheCode kvCacheCode;


    public void setOldKV(CachedKV oldKV){
        if (oldKV != null){
            oldSession = oldKV.getSession();
            oldValueAsString = oldKV.getValueAsString();
        }
    }

    public void deletedKV(){
        this.oldSession = session;
        this.oldValueAsString = valueAsString;
        this.session = Optional.absent();
        this.valueAsString = Optional.absent();
        this.kvCacheCode = CachedKV.KVCacheCode.REMOVED;
    }

    public CachedKV(Value value, CachedKV oldKV){
        this.key = value.getKey();
        this.createIndex = value.getCreateIndex();
        this.modifyIndex = value.getModifyIndex();
        this.session = value.getSession();
        this.valueAsString = value.getValueAsString();

        if (oldKV != null){
            oldSession = oldKV.getSession();
            oldValueAsString = oldKV.getValueAsString();
        }
    }


    public Optional<String> getOldSession() {
        return oldSession;
    }

    public void setOldSession(Optional<String> oldSession) {
        this.oldSession = oldSession;
    }

    public Optional<String> getOldValueAsString() {
        return oldValueAsString;
    }

    public void setOldValueAsString(Optional<String> oldValueAsString) {
        this.oldValueAsString = oldValueAsString;
    }

    public KVCacheCode getKvCacheCode() {
        return kvCacheCode;
    }

    public void setKvCacheCode(KVCacheCode kvCacheCode) {
        this.kvCacheCode = kvCacheCode;
    }

    public enum KVCacheCode{
        INIT,NEW,REMOVED,CHANGED
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public long getCreateIndex() {
        return createIndex;
    }

    public void setCreateIndex(long createIndex) {
        this.createIndex = createIndex;
    }

    public long getModifyIndex() {
        return modifyIndex;
    }

    public void setModifyIndex(long modifyIndex) {
        this.modifyIndex = modifyIndex;
    }

    public Optional<String> getSession() {
        return session;
    }

    public void setSession(Optional<String> session) {
        this.session = session;
    }

    public Optional<String> getValueAsString() {
        return valueAsString;
    }

    public void setValueAsString(Optional<String> valueAsString) {
        this.valueAsString = valueAsString;
    }



}
