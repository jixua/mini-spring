package org.qlspringframework.beans.common;

import org.qlspringframework.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;

/**
 * @author jixu
 * @title WorldServiceBeforeAdvice
 * @date 2025/5/24 16:49
 */
public class WorldServiceBeforeAdvice implements MethodBeforeAdvice {
    @Override
    public void before(Method method, Object[] args, Object target) {
        System.out.println("Before");
    }
}
