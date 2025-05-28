package org.qlspringframework.aop.framework.autoproxy;

import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.qlspringframework.aop.*;
import org.qlspringframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.qlspringframework.aop.framework.ProxyFactory;
import org.qlspringframework.beans.BeansException;
import org.qlspringframework.beans.factory.BeanFactory;
import org.qlspringframework.beans.factory.BeanFactoryAware;
import org.qlspringframework.beans.factory.config.BeanDefinition;
import org.qlspringframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.qlspringframework.beans.factory.supper.DefaultListableBeanFactory;

import java.util.Collection;

/**
 * @author jixu
 * @title DefaultAdvisorAutoProxyCreator
 * @date 2025/5/28 15:48
 */
public class DefaultAdvisorAutoProxyCreator implements InstantiationAwareBeanPostProcessor, BeanFactoryAware {

    private DefaultListableBeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = (DefaultListableBeanFactory) beanFactory;
    }

    /**
     * 前置增强用于处理代理对象
     *
     * @param beanClass
     * @param beanName
     * @return
     */
    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) {

        // 避免代理AOP相关配置类
        if (isInfrastructureClass(beanClass)) {
            return null;
        }

        // 获取到所有的Advisor
        Collection<AspectJExpressionPointcutAdvisor> advisors = beanFactory.getBeanOfType(AspectJExpressionPointcutAdvisor.class).values();
        try {
            for (AspectJExpressionPointcutAdvisor advisor : advisors) {
                // 如果要代理的是当前类
                if (advisor.getPointcut().getClassFilter().matches(beanClass)) {
                    AdvisedSupper advisedSupper = new AdvisedSupper();

                    BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);
                    Object bean = beanFactory.getInstantiationStrategy().instantiate(beanDefinition);
                    TargetSource targetSource = new TargetSource(bean);

                    advisedSupper.setMethodInterceptor(((MethodInterceptor) advisor.getAdvice()));
                    advisedSupper.setTargetSource(targetSource);
                    advisedSupper.setMethodMatcher(advisor.getPointcut().getMethodMatcher());

                    Object proxy = new ProxyFactory(advisedSupper).getProxy();
                    return proxy;

                }
            }
        } catch (Exception e) {
            throw new BeansException("代理对象："+beanName+"创建失败", e);
        }

        return null;
    }

    private boolean isInfrastructureClass(Class<?> beanClass) {

        return Advice.class.isAssignableFrom(beanClass)
                || PointCut.class.isAssignableFrom(beanClass)
                || Advisor.class.isAssignableFrom(beanClass);
    }

    /**
     * 在 Bean 初始化之前执行自定义处理逻辑。
     * 使用此方法，可以在 Bean 被初始化之前对其进行修改或执行其他操作。
     *
     * @param bean     当前正在初始化的 Bean 实例。
     * @param beanName 当前 Bean 的名称。
     * @return 返回处理后的 Bean 实例，可以是原始 Bean 或修改后的 Bean。
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        return bean;
    }

    /**
     * 在 Bean 初始化之后执行自定义处理逻辑。
     * 使用此方法，可以在 Bean 初始化完成后对其进行进一步的修改或执行其他操作。
     *
     * @param bean     当前已经初始化的 Bean 实例。
     * @param beanName 当前 Bean 的名称。
     * @return 返回处理后的 Bean 实例，可以是原始 Bean 或修改后的 Bean。
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        return bean;
    }
}
