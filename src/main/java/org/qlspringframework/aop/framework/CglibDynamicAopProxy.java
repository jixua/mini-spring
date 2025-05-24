package org.qlspringframework.aop.framework;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.qlspringframework.aop.AdvisedSupper;

import java.lang.reflect.Method;
/**
 * CglibDynamicAopProxy 类是使用 CGLIB 库实现的动态代理类。
 * 它主要用于创建目标类的子类，并在子类中织入切面逻辑。
 * 该类实现了 AopProxy 接口，用于提供获取代理对象的方法。
 *
 * @author jixu
 * @title CglibDynamicAopProxy
 * @date 2025/5/23 17:46
 */
public class CglibDynamicAopProxy implements AopProxy {

    // AdvisedSupper 对象中包含了目标类的信息以及切面的配置
    private final AdvisedSupper advisedSupper;

    // 构造方法，初始化 CglibDynamicAopProxy 对象
    public CglibDynamicAopProxy(AdvisedSupper advisedSupper) {
        this.advisedSupper = advisedSupper;
    }

    /**
     * 获取代理对象
     *
     * @return 代理对象，通过该对象可以调用目标方法以及切面方法
     */
    @Override
    public Object getProxy() {
        // 创建 CGLIB 提供的增强器对象（核心代理创建类）
        Enhancer enhancer = new Enhancer();

        // 设置被代理类的父类（目标对象的真实类）
        enhancer.setSuperclass(advisedSupper.getTargetSource().getTarget().getClass());

        // 设置代理对象实现的接口（可选）
        enhancer.setInterfaces(advisedSupper.getTargetSource().getTargetInterfaceClass());

        // 设置方法拦截器，用于处理方法调用
        enhancer.setCallback(new DynamicAdvisedInterceptor(advisedSupper));

        // 创建并返回代理对象
        return enhancer.create();
    }



    // CglibMethodInvocation 类用于处理 CGLIB 方法调用
    private static class CglibMethodInvocation extends ReflectiveMethodInvocation{
        private final MethodProxy methodProxy;

        /**
         * 构造方法，初始化ReflectiveMethodInvocation对象
         *
         * @param method   方法对象，表示要调用的方法
         * @param target   目标对象，即要调用方法的实例
         * @param argument 方法参数数组，存储调用方法时传递的参数
         */
        public CglibMethodInvocation(Method method, Object target, Object[] argument, MethodProxy methodProxy) {
            super(method, target, argument);
            this.methodProxy = methodProxy;
        }

        /**
         * 执行当前方法调用
         *
         * @return 方法执行结果
         * @throws Throwable 方法执行过程中抛出的异常
         */
        @Override
        public Object proceed() throws Throwable {
            // 使用 MethodProxy 执行方法，提高执行效率
            return this.methodProxy.invoke(target,argument);
        }
    }

    // DynamicAdvisedInterceptor 类用于适配 CGLIB 的 MethodInterceptor 接口
    private static class DynamicAdvisedInterceptor implements MethodInterceptor {
        private final AdvisedSupper advisedSupper;

        private DynamicAdvisedInterceptor(AdvisedSupper advisedSupper) {
            this.advisedSupper = advisedSupper;
        }

        /**
         * 拦截方法调用
         *
         * @param o        代理对象
         * @param method   被调用的方法对象
         * @param objects  方法参数数组
         * @param methodProxy  方法代理对象，用于调用目标方法
         * @return 方法执行结果
         * @throws Throwable 方法执行过程中抛出的异常
         */
        @Override
        public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
            // 创建 CglibMethodInvocation 对象，封装方法调用信息
            CglibMethodInvocation methodInvocation = new CglibMethodInvocation(method, advisedSupper.getTargetSource().getTarget(), objects, methodProxy);

            // 如果切点表达式匹配
            if (advisedSupper.getMethodMatcher().matches(method,advisedSupper.getTargetSource().getTarget().getClass())){
                // 使用切面的 MethodInterceptor 处理方法调用
                return advisedSupper.getMethodInterceptor().invoke(methodInvocation);
            }
            // 如果切点表达式不匹配，则直接调用原始方法
            return method.invoke(o,objects);
        }
    }
}

