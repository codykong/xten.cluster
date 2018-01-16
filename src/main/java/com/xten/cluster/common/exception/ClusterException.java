package com.xten.cluster.common.exception;

import com.xten.cluster.common.log.LoggerMessageFormat;

/**
 * Description:
 * User: kongqingyu
 * Date: 2018/1/12
 */
public class ClusterException extends RuntimeException {

    /**
     * Construct a <code>ClusterException</code> with the specified detail message.
     *
     * The message can be parameterized using <code>{}</code> as placeholders for the given
     * arguments
     *
     * @param msg  the detail message
     * @param args the arguments for the message
     */
    public ClusterException(String msg, Object... args) {
        super(LoggerMessageFormat.format(msg, args));
    }


}
