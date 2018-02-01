package com.xten.cluster.common.util;

/**
 * Description:
 * User: kongqingyu
 * Date: 2018/2/1
 */
public class AgentConstant {

    public enum  AgentType {

        AGENT(0);

        private final int code;

        AgentType(int code){
            this.code = code;
        }

        public boolean equal(int code){
            return this.code == code;
        }

        public int getCode(){
            return this.code;
        }

        public String toTypeName(){
            return this.name().toLowerCase();
        }
    }

}
