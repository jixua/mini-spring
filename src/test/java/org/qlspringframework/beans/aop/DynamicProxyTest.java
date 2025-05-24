package org.qlspringframework.beans.aop;

import org.junit.Before;
import org.junit.Test;
import org.qlspringframework.aop.AdvisedSupper;
import org.qlspringframework.aop.MethodMatcher;
import org.qlspringframework.aop.TargetSource;
import org.qlspringframework.aop.aspectj.AspectJExpressionPointcut;
import org.qlspringframework.aop.framework.AopProxy;
import org.qlspringframework.aop.framework.CglibDynamicAopProxy;
import org.qlspringframework.aop.framework.JdkDynamicAopProxy;
import org.qlspringframework.aop.framework.ProxyFactory;
import org.qlspringframework.aop.framework.adapter.MethodBeforeAdviceInterceptor;
import org.qlspringframework.beans.common.WorldServiceBeforeAdvice;
import org.qlspringframework.beans.common.WorldServiceInterceptor;
import org.qlspringframework.beans.service.WorldService;
import org.qlspringframework.beans.service.WorldServiceImpl;

/**
 * @author jixu
 * @title DynamicProxyTest
 * @date 2025/5/22 20:07
 */
public class DynamicProxyTest {

    public AdvisedSupper advisedSupper = new AdvisedSupper();

    @Before
    public void setup(){

        WorldServiceImpl worldService = new WorldServiceImpl();
        TargetSource targetSource = new TargetSource(worldService);
        advisedSupper.setTargetSource(targetSource);

        WorldServiceInterceptor interceptor = new WorldServiceInterceptor();
        advisedSupper.setMethodInterceptor(interceptor);
        // "execution(* org.springframework.test.service.WorldService.explode(..))"
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut("execution(* org.qlspringframework.beans.service.WorldService.sayHello(..))");
        MethodMatcher methodMatcher = pointcut.getMethodMatcher();
        advisedSupper.setMethodMatcher(methodMatcher);
    }


    @Test
    public void testJdkDynamicProxy(){
        JdkDynamicAopProxy jdkDynamicAopProxy = new JdkDynamicAopProxy(advisedSupper);
        WorldService proxy = (WorldService) jdkDynamicAopProxy.getProxy();
        proxy.sayHello();


    }


    @Test
    public void testCglibDynamicProxy(){
        // 创建Cglib代理对象
        CglibDynamicAopProxy cglibDynamicAopProxy = new CglibDynamicAopProxy(advisedSupper);
        WorldService proxy = (WorldService) cglibDynamicAopProxy.getProxy();
        proxy.sayHello();

    }

    @Test
    public void testProxyFactory(){
        advisedSupper.setProxyTargetClass(true);
        ProxyFactory proxyFactory = new ProxyFactory(advisedSupper);
        WorldService worldService = (WorldService) proxyFactory.getProxy();
        worldService.sayHello();

    }





}
