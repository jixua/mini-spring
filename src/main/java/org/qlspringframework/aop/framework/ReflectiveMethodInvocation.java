package org.qlspringframework.aop.framework;

import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;

/**
 * ReflectiveMethodInvocation类实现了MethodInvocation接口，用于处理通过反射调用的方法
 * 它封装了方法调用的相关信息，如方法本身、目标对象以及方法参数
 *
 * @author jixu
 * @title ReflectiveMethodInvocation
 * @date 2025/5/21 21:52
 */
public class ReflectiveMethodInvocation implements MethodInvocation {

    // 方法对象，表示要调用的方法
    protected final Method method;

    // 目标对象，即要调用方法的实例
    protected final Object target;

    // 方法参数数组，存储调用方法时传递的参数
    protected final Object[] argument;

    /**
     * 构造方法，初始化ReflectiveMethodInvocation对象
     *
     * @param method 方法对象，表示要调用的方法
     * @param target 目标对象，即要调用方法的实例
     * @param argument 方法参数数组，存储调用方法时传递的参数
     */
    public ReflectiveMethodInvocation(Method method, Object target, Object[] argument) {
        this.method = method;
        this.target = target;
        this.argument = argument;
    }

    /**
     * 获取要调用的方法
     *
     * @return Method对象，表示要调用的方法
     */
    @Override
    public Method getMethod() {
        return this.method;
    }

    /**
     * 获取调用方法时传递的参数
     *
     * @return Object数组，包含调用方法时传递的参数
     */
    @Override
    public Object[] getArguments() {
        return this.argument;
    }

    /**
     * 执行调用的方法
     * 通过反射机制，使用目标对象和参数调用方法
     *
     * @return 方法调用的结果
     * @throws Throwable 如果方法调用过程中发生异常
     */
    @Override
    public Object proceed() throws Throwable {
        return method.invoke(target, argument);
    }

    /**
     * 获取当前调用方法的目标对象
     *
     * @return Object，表示当前调用方法的目标对象
     */
    @Override
    public Object getThis() {
        return target;
    }

    /**
     * 获取当前调用方法的静态部分，即Method对象本身
     *
     * @return AccessibleObject，表示当前调用方法的静态部分
     */
    @Override
    public AccessibleObject getStaticPart() {
        return method;
    }
}

