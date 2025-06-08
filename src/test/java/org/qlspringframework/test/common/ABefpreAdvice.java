package org.qlspringframework.test.common;

import org.qlspringframework.aop.MethodBeforeAdvice;
import org.qlspringframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author jixu
 * @title ABefpreAvvice
 * @date 2025/6/6 20:03
 */
@Component
public class ABefpreAdvice implements MethodBeforeAdvice {



    /**
     * 在目标方法执行前执行的方法。
     *
     * @param method 目标方法的反射对象，允许访问方法名称、返回类型、参数类型等信息。
     * @param args   目标方法的参数数组，允许访问和修改方法参数。
     * @param target 目标对象，即执行方法的类的实例，允许访问对象状态。
     * @throws Throwable 如果预处理方法中发生异常，可以抛出此异常。
     */
    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        System.out.println("before");
    }
}
