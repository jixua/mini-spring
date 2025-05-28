package org.qlspringframework.aop;

import org.aopalliance.aop.Advice;

import java.lang.reflect.Method;

/**
 * @author jixu
 * @title AfterReturningAdvice
 * @date 2025/5/24 16:41
 */
public interface AfterReturningAdvice extends AfterAdvice {

    void after(Method method, Object[] args, Object target);
}
