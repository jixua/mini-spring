package org.qlspringframework.aop;

import java.lang.reflect.Method;

/**
 * @author jixu
 * @title MethodBeforeAdvice
 * @date 2025/5/24 16:03
 */
public interface MethodBeforeAdvice extends BeforeAdvice {

    void before(Method method, Object[] args, Object target);
}
