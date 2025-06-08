package org.qlspringframework.aop.framework;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.qlspringframework.aop.AdvisedSupport;

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

    // AdvisedSupport 对象中包含了目标类的信息以及切面的配置
    private final AdvisedSupport advisedSupport;

    // 构造方法，初始化 CglibDynamicAopProxy 对象
    public CglibDynamicAopProxy(AdvisedSupport advisedSupport) {
        this.advisedSupport = advisedSupport;
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
        enhancer.setSuperclass(advisedSupport.getTargetSource().getTarget().getClass());

        // 设置代理对象实现的接口（可选）
        enhancer.setInterfaces(advisedSupport.getTargetSource().getTargetInterfaceClass());

        // 设置方法拦截器，用于处理方法调用
        enhancer.setCallback(new DynamicAdvisedInterceptor(advisedSupport));

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
        private final AdvisedSupport advisedSupport;

        private DynamicAdvisedInterceptor(AdvisedSupport advisedSupport) {
            this.advisedSupport = advisedSupport;
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
            try {
                String methodName = method.getName();

                // equals/hashCode 直接调用目标对象对应方法
                if ("equals".equals(methodName) && objects != null && objects.length == 1) {
                    Object other = objects[0];
                    if (o == other) {
                        return true;
                    }
                    if (other == null || !o.getClass().isAssignableFrom(other.getClass())) {
                        return false;
                    }
                    Object target = advisedSupport.getTargetSource().getTarget();
                    return target.equals(other);
                }
                if ("hashCode".equals(methodName) && (objects == null || objects.length == 0)) {
                    Object target = advisedSupport.getTargetSource().getTarget();
                    return target.hashCode();
                }

                CglibMethodInvocation methodInvocation = new CglibMethodInvocation(method, advisedSupport.getTargetSource().getTarget(), objects, methodProxy);

                // 规避 equals/hashCode 参与切点匹配
                if (!("equals".equals(methodName) || "hashCode".equals(methodName)) &&
                        advisedSupport.getMethodMatcher().matches(method, advisedSupport.getTargetSource().getTarget().getClass())) {
                    return advisedSupport.getMethodInterceptor().invoke(methodInvocation);
                }

                // 这里调用目标对象的方法，避免递归调用代理自身方法
                return method.invoke(advisedSupport.getTargetSource().getTarget(), objects);
            } catch (Throwable t) {
                System.err.println("CglibDynamicAopProxy intercept exception: " + t);
                t.printStackTrace();
                throw t;
            }
        }
    }
}

