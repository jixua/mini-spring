package org.qlspringframework.beans.ioc.aop;

import org.junit.Test;
import org.qlspringframework.aop.AdvisedSupper;
import org.qlspringframework.aop.MethodMatcher;
import org.qlspringframework.aop.TargetSource;
import org.qlspringframework.aop.aspectj.AspectJExpressionPointcut;
import org.qlspringframework.aop.framework.JdkDynamicAopProxy;
import org.qlspringframework.aop.framework.ReflectiveMethodInvocation;
import org.qlspringframework.beans.ioc.common.WorldServiceInterceptor;
import org.qlspringframework.beans.ioc.service.HelloService;
import org.qlspringframework.beans.ioc.service.WorldService;
import org.qlspringframework.beans.ioc.service.WorldServiceImpl;

/**
 * @author jixu
 * @title DynamicProxyTest
 * @date 2025/5/22 20:07
 */
public class DynamicProxyTest {


    @Test
    public void testDynamicProxy(){

        AdvisedSupper advisedSupper = new AdvisedSupper();

        WorldServiceImpl worldService = new WorldServiceImpl();
        TargetSource targetSource = new TargetSource(worldService);
        advisedSupper.setTargetSource(targetSource);

        WorldServiceInterceptor interceptor = new WorldServiceInterceptor();
        advisedSupper.setMethodInterceptor(interceptor);
        // "execution(* org.springframework.test.service.WorldService.explode(..))"
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut("execution(* org.qlspringframework.beans.ioc.service.WorldService.sayHello(..))");
        MethodMatcher methodMatcher = pointcut.getMethodMatcher();
        advisedSupper.setMethodMatcher(methodMatcher);

        JdkDynamicAopProxy jdkDynamicAopProxy = new JdkDynamicAopProxy(advisedSupper);
        WorldService proxy = (WorldService) jdkDynamicAopProxy.getProxy();
        proxy.sayHello();


    }
}
