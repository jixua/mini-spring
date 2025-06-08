package org.qlspringframework.test.aop;

import org.junit.Before;
import org.junit.Test;
import org.qlspringframework.aop.AdvisedSupport;
import org.qlspringframework.aop.MethodMatcher;
import org.qlspringframework.aop.TargetSource;
import org.qlspringframework.aop.aspectj.AspectJExpressionPointcut;
import org.qlspringframework.aop.framework.CglibDynamicAopProxy;
import org.qlspringframework.aop.framework.JdkDynamicAopProxy;
import org.qlspringframework.aop.framework.ProxyFactory;
import org.qlspringframework.aop.framework.adapter.MethodBeforeAdviceInterceptor;
import org.qlspringframework.test.common.WorldServiceBeforeAdvice;
import org.qlspringframework.test.common.WorldServiceInterceptor;
import org.qlspringframework.test.service.WorldService;
import org.qlspringframework.test.service.WorldServiceImpl;

/**
 * @author jixu
 * @title DynamicProxyTest
 * @date 2025/5/22 20:07
 */
public class DynamicProxyTest {

    public AdvisedSupport advisedSupport = new AdvisedSupport();

    @Before
    public void setup(){
        // 被代理对象
        WorldServiceImpl worldService = new WorldServiceImpl();
        // 封装为目标资源对象
        TargetSource targetSource = new TargetSource(worldService);
        advisedSupport.setTargetSource(targetSource);

        // 创建拦截器对象
        WorldServiceInterceptor interceptor = new WorldServiceInterceptor();
        advisedSupport.setMethodInterceptor(interceptor);

        // 创建PointCut解析器
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut("execution(* org.qlspringframework.test.service.WorldService.sayHello(..))");
        MethodMatcher methodMatcher = pointcut.getMethodMatcher();
        advisedSupport.setMethodMatcher(methodMatcher);
    }


    @Test
    public void testJdkDynamicProxy(){
        JdkDynamicAopProxy jdkDynamicAopProxy = new JdkDynamicAopProxy(advisedSupport);
        WorldService proxy = (WorldService) jdkDynamicAopProxy.getProxy();
        proxy.sayHello();


    }


    @Test
    public void testCglibDynamicProxy(){
        // 创建Cglib代理对象
        CglibDynamicAopProxy cglibDynamicAopProxy = new CglibDynamicAopProxy(advisedSupport);
        WorldService proxy = (WorldService) cglibDynamicAopProxy.getProxy();
        proxy.sayHello();

    }

    @Test
    public void testProxyFactory(){
        advisedSupport.setProxyTargetClass(true);
        ProxyFactory proxyFactory = new ProxyFactory(advisedSupport);
        WorldService worldService = (WorldService) proxyFactory.getProxy();
        worldService.sayHello();

    }


    @Test
    public void testBeforeAdvice(){
        WorldServiceBeforeAdvice advice = new WorldServiceBeforeAdvice();
        MethodBeforeAdviceInterceptor methodInterceptor = new MethodBeforeAdviceInterceptor(advice);
        advisedSupport.setMethodInterceptor(methodInterceptor);

        CglibDynamicAopProxy cglibDynamicAopProxy = new CglibDynamicAopProxy(advisedSupport);
        WorldService proxy = (WorldService) cglibDynamicAopProxy.getProxy();
        proxy.sayHello();
    }



}
