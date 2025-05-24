package org.qlspringframework.aop.framework.adapter;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.qlspringframework.aop.MethodBeforeAdvice;

/**
 * @author jixu
 * @title MethodBeforeAdviceInterceptor
 * @date 2025/5/24 16:15
 */
public class MethodBeforeAdviceInterceptor implements MethodInterceptor {

    private MethodBeforeAdvice advice;

    public MethodBeforeAdviceInterceptor(MethodBeforeAdvice advice) {
        this.advice = advice;
    }

    /**
     * AOP 前置拦截器
     * @param methodInvocation
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        // 执行前置拦截方法
        advice.before(methodInvocation.getMethod(), methodInvocation.getArguments(), methodInvocation.getThis());
        // 继续执行下一个拦截器或目标方法本身
        return methodInvocation.proceed();
    }
}
