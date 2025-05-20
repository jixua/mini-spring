package org.qlspringframework.beans.ioc.aop;

import org.junit.Assert;
import org.junit.Test;
import org.qlspringframework.aop.aspectj.AspectJExpressionPointcut;
import org.qlspringframework.beans.ioc.service.HelloService;

import java.lang.reflect.Method;

/**
 * @author jixu
 * @title PointCutTest
 * @date 2025/5/20 19:58
 */
public class PointCutTest {

    @Test
    public void testPointCut() throws NoSuchMethodException {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut("execution(* org.qlspringframework.beans.ioc.service.HelloService.*(..))");

        Class<HelloService> helloServiceClass = HelloService.class;
        Method declaredMethod = helloServiceClass.getDeclaredMethod("say");

        Assert.assertEquals(pointcut.matches(helloServiceClass),true);
        Assert.assertEquals(pointcut.matches(declaredMethod,helloServiceClass),true);

    }
}
