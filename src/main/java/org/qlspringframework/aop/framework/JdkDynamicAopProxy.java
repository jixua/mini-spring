package org.qlspringframework.aop.framework;

import org.aopalliance.intercept.MethodInterceptor;
import org.qlspringframework.aop.AdvisedSupper;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * JdkDynamicAopProxy类实现了AopProxy和InvocationHandler接口，
 * 用于通过Java动态代理机制创建代理对象并处理方法调用。
 *
 * @author jixu
 * @title JdkDynamicAopProxy
 * @date 2025/5/21 21:52
 */
public class JdkDynamicAopProxy implements AopProxy , InvocationHandler {

    // AdvisedSupper对象包含了代理所需的配置信息，如目标源、方法匹配器和方法拦截器。
    private final AdvisedSupper advisedSupper;

    /**
     * 构造函数，接收一个AdvisedSupper对象来初始化JdkDynamicAopProxy。
     *
     * @param advisedSupper 包含代理配置信息的对象
     */
    public JdkDynamicAopProxy(AdvisedSupper advisedSupper) {
        this.advisedSupper = advisedSupper;
    }

    /**
     * 根据当前类加载器和目标源的接口生成一个代理对象。
     *
     * @return 生成的代理对象
     */
    @Override
    public Object getProxy() {
        // Proxy.newProxyInstance() 是jdk提供的静态方法，用于动态创建代理对象
        // 通过该方法会生成一个实现了目标类接口的代理对象，当调用代理对象的目标方法时，JVM 将调用转发到自定义的InvocationHandler 的 invoke 方法。
        return Proxy.newProxyInstance(getClass().getClassLoader(),advisedSupper.getTargetSource().getTargetInterfaceClass(), this);
    }

    /**
     * 处理代理实例上的方法调用并返回结果。
     * 当与代理实例关联的方法被调用时，将调用此方法。
     *
     * @param proxy 代理类实例
     * @param method 被调用的方法对象
     * @param args 方法参数
     * @return 方法调用的结果
     * @throws Throwable 方法调用中可能抛出的异常
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        // 获取到方法匹配器，通过AspectJExpressionPointcut解析传入的切点表达式，调用对应的matches方法判断是否为被代理类
        if (advisedSupper.getMethodMatcher().matches(method,advisedSupper.getTargetSource().getTarget().getClass())){

            // 获取到方法拦截器
            MethodInterceptor methodInterceptor = advisedSupper.getMethodInterceptor();

            // 创建ReflectiveMethodInvocation对象，封装了方法调用的相关信息
            ReflectiveMethodInvocation invocation = new ReflectiveMethodInvocation(method, advisedSupper.getTargetSource().getTarget(), args);

            // 调用 MethodInterceptor 的 invoke 方法，传入 invocation 作为参数。
            // invoke 方法会执行拦截器逻辑，通常通过 invocation.proceed() 调用目标方法或下一个拦截器。
            return methodInterceptor.invoke(invocation);

        }

        // 如果方法匹配器判断当前调用的方法不满足切点表达式，则直接调用目标方法
        return method.invoke(advisedSupper.getTargetSource().getTarget(),args);
    }
}
