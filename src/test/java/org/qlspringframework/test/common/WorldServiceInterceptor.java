package org.qlspringframework.test.common;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @author jixu
 * @title WorldServiceInterceptor
 * @date 2025/5/22 20:05
 */
public class WorldServiceInterceptor implements MethodInterceptor {
    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        System.out.println("before");
        Object result = methodInvocation.proceed();
        System.out.println("after");
        return result;
    }
}
