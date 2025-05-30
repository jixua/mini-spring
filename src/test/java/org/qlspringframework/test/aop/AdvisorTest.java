package org.qlspringframework.test.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.junit.Test;
import org.qlspringframework.aop.AdvisedSupper;
import org.qlspringframework.aop.ClassFilter;
import org.qlspringframework.aop.TargetSource;
import org.qlspringframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.qlspringframework.aop.framework.ProxyFactory;
import org.qlspringframework.aop.framework.adapter.MethodBeforeAdviceInterceptor;
import org.qlspringframework.test.common.WorldServiceBeforeAdvice;
import org.qlspringframework.test.service.WorldService;
import org.qlspringframework.test.service.WorldServiceImpl;

/**
 * @author jixu
 * @title AdvisorTest
 * @date 2025/5/27 21:12
 */
public class AdvisorTest {


    @Test
    public void testAdvisor(){

        // 创建Advisor对象
        AspectJExpressionPointcutAdvisor advisor = new AspectJExpressionPointcutAdvisor();
        // 设置切点表达式
        advisor.setExpression("execution(* org.qlspringframework.beans.service.WorldService.sayHello(..))");
        MethodBeforeAdviceInterceptor adviceInterceptor = new MethodBeforeAdviceInterceptor(new WorldServiceBeforeAdvice());
        advisor.setAdvice(adviceInterceptor);

        ClassFilter classFilter = advisor.getPointcut().getClassFilter();
        if (classFilter.matches(WorldService.class)){

            AdvisedSupper advisedSupper = new AdvisedSupper();
            advisedSupper.setMethodMatcher(advisor.getPointcut().getMethodMatcher());
            advisedSupper.setTargetSource(new TargetSource(new WorldServiceImpl()));
            advisedSupper.setMethodInterceptor((MethodInterceptor)advisor.getAdvice());

            WorldService worldService = (WorldService) new ProxyFactory(advisedSupper).getProxy();
            worldService.sayHello();
        }
    }
}
