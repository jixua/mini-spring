package org.qlspringframework.beans;

/**
 * @description: Bean异常
 * @author: jixu
 * @create: 2025-03-28 16:57
 **/
public class BeansException extends RuntimeException{

    public BeansException(String msg) {
        super(msg);
    }

    public BeansException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
