package org.qlspringframework.aop;

import org.aopalliance.intercept.MethodInterceptor;
import sun.dc.pr.PRError;

/**
 * AdvisedSupper类是用于配置和管理目标源、方法匹配器和方法拦截器的超级类
 * 它提供了一种机制，用于在满足特定条件时，对目标源的方法调用进行拦截和处理
 *
 * @author jixu
 * @title AdvisedSupper
 * @date 2025/5/21 21:49
 */
public class AdvisedSupper {

    // 目标源，代表了需要被拦截和处理的目标对象
    private TargetSource targetSource;

    // 方法匹配器，用于判断是否需要对某个方法进行拦截
    private MethodMatcher methodMatcher;

    // 方法拦截器，用于在满足条件时对方法进行实际的拦截和处理
    private MethodInterceptor methodInterceptor;

    // 是否开启Cglib代理
    private boolean proxyTargetClass = false;



    /**
     * 获取方法匹配器
     *
     * @return 当前的MethodMatcher实例
     */
    public MethodMatcher getMethodMatcher() {
        return methodMatcher;
    }

    /**
     * 设置方法匹配器
     *
     * @param methodMatcher 要设置的MethodMatcher实例，用于匹配方法
     */
    public void setMethodMatcher(MethodMatcher methodMatcher) {
        this.methodMatcher = methodMatcher;
    }

    /**
     * 获取目标源
     *
     * @return 当前的TargetSource实例
     */
    public TargetSource getTargetSource() {
        return targetSource;
    }

    /**
     * 设置目标源
     *
     * @param targetSource 要设置的TargetSource实例，代表需要被处理的目标对象
     */
    public void setTargetSource(TargetSource targetSource) {
        this.targetSource = targetSource;
    }

    /**
     * 获取方法拦截器
     *
     * @return 当前的MethodInterceptor实例
     */
    public MethodInterceptor getMethodInterceptor() {
        return methodInterceptor;
    }

    /**
     * 设置方法拦截器
     *
     * @param methodInterceptor 要设置的MethodInterceptor实例，用于拦截和处理方法调用
     */
    public void setMethodInterceptor(MethodInterceptor methodInterceptor) {
        this.methodInterceptor = methodInterceptor;
    }

    public boolean isProxyTargetClass() {
        return proxyTargetClass;
    }

    public void setProxyTargetClass(boolean proxyTargetClass) {
        this.proxyTargetClass = proxyTargetClass;
    }
}

