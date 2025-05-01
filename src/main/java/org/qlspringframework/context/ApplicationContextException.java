package org.qlspringframework.context;

import org.qlspringframework.beans.BeanException;

/**
 * 当应用上下文(ApplicationContext)中发生异常时抛出此异常
 * 它继承自BeanException，用于更具体的表示在处理Bean时遇到的问题
 *
 * @author: jixu
 * @create: 2025-04-19 14:33
 **/
public class ApplicationContextException extends BeanException {

    /**
     * 构造一个应用上下文异常，包含一个简单的消息
     *
     * @param msg 异常的详细消息
     */
    public ApplicationContextException(String msg) {
        super(msg);
    }

    /**
     * 构造一个应用上下文异常，包含一个简单的消息和一个原因
     *
     * @param msg   异常的详细消息
     * @param cause 异常的原因，通常是一个下层抛出的异常
     */
    public ApplicationContextException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
