package org.qlspringframework.aop.framework.adapter;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.qlspringframework.aop.MethodBeforeAdvice;

/**
 * AOP 前置通知拦截器类，用于在方法调用前执行额外的逻辑
 * 主要功能是封装了如何在目标方法调用前，执行前置通知（Advice）
 *
 * @author jixu
 * @title MethodBeforeAdviceInterceptor
 * @date 2025/5/24 16:15
 */
public class MethodBeforeAdviceInterceptor implements MethodInterceptor {

    // 前置通知对象，定义了在目标方法调用前需要执行的逻辑
    private MethodBeforeAdvice advice;

    /**
     * 构造函数，用于注入前置通知
     *
     * @param advice 前置通知对象，用于在目标方法调用前执行逻辑
     */
    public MethodBeforeAdviceInterceptor(MethodBeforeAdvice advice) {
        this.advice = advice;
    }

    /**
     * AOP 前置拦截器
     * 在目标方法调用前，执行前置通知的逻辑
     *
     * @param methodInvocation 方法调用对象，包含方法信息、参数和目标对象
     * @return 目标方法的执行结果
     * @throws Throwable 目标方法或前置通知可能抛出的异常
     */
    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        // 执行前置拦截方法
        advice.before(methodInvocation.getMethod(), methodInvocation.getArguments(), methodInvocation.getThis());
        // 继续执行下一个拦截器或目标方法本身
        return methodInvocation.proceed();
    }
}

