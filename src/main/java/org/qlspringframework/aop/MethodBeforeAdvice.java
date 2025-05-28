package org.qlspringframework.aop;

import java.lang.reflect.Method;

/**
 * MethodBeforeAdvice接口继承自BeforeAdvice，用于在方法执行前提供额外的处理逻辑。
 * 它允许用户在方法执行前访问方法信息、参数和目标对象，以便进行预处理操作。
 *
 * @author jixu
 * @title MethodBeforeAdvice
 * @date 2025/5/24 16:03
 */
public interface MethodBeforeAdvice extends BeforeAdvice {

    /**
     * 在目标方法执行前执行的方法。
     *
     * @param method 目标方法的反射对象，允许访问方法名称、返回类型、参数类型等信息。
     * @param args 目标方法的参数数组，允许访问和修改方法参数。
     * @param target 目标对象，即执行方法的类的实例，允许访问对象状态。
     * @throws Throwable 如果预处理方法中发生异常，可以抛出此异常。
     */
    void before(Method method, Object[] args, Object target) throws Throwable;
}

