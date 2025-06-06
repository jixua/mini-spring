package org.qlspringframework.test.aop;

import org.junit.Assert;
import org.junit.Test;
import org.qlspringframework.aop.aspectj.AspectJExpressionPointcut;
import org.qlspringframework.test.service.HelloService;

import java.lang.reflect.Method;

/**
 * @author jixu
 * @title PointCutTest
 * @date 2025/5/20 19:58
 */
public class PointCutTest {

    @Test
    public void testPointCut() throws NoSuchMethodException {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut("execution(* org.qlspringframework.test.service.HelloService.*(..))");

        Class<HelloService> helloServiceClass = HelloService.class;
        Method declaredMethod = helloServiceClass.getDeclaredMethod("say");

        Assert.assertEquals(pointcut.matches(helloServiceClass),true);
        Assert.assertEquals(pointcut.matches(declaredMethod,helloServiceClass),true);

    }
}
