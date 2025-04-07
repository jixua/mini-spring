package org.qlspringframework.beans;

/**
 * @description: Bean异常
 * @author: jixu
 * @create: 2025-03-28 16:57
 **/
public class BeanException extends RuntimeException{

    public BeanException(String msg) {
        super(msg);
    }

    public BeanException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
