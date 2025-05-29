package org.qlspringframework.aop.framework.autoproxy;

import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.qlspringframework.aop.AdvisedSupper;
import org.qlspringframework.aop.Advisor;
import org.qlspringframework.aop.PointCut;
import org.qlspringframework.aop.TargetSource;
import org.qlspringframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.qlspringframework.aop.framework.ProxyFactory;
import org.qlspringframework.beans.BeansException;
import org.qlspringframework.beans.factory.BeanFactory;
import org.qlspringframework.beans.factory.config.BeanDefinition;
import org.qlspringframework.beans.factory.supper.DefaultListableBeanFactory;

import java.util.Collection;

/**
 * 抽象的顾问自动代理创建者类
 * 该类的作用是自动创建代理对象，根据特定的规则或条件，动态地应用顾问（Advisor）到目标对象上
 * 主要用于Spring框架的AOP（面向切面编程）功能中，以实现自动代理机制
 *
 * @author jixu
 * @title AbstractAdvisorAutoProxyCreator
 * @date 2025/5/29 00:44
 */
public abstract class AbstractAdvisorAutoProxyCreator extends AbstractAutoProxyCreator{

    // 定义一个 BeanFactory 属性，用于管理 Bean 的创建和生命周期。
    private DefaultListableBeanFactory beanFactory;

    /**
     * 设置 BeanFactory。
     * 当本类需要访问或操作 Spring 容器中的 Bean 时，通过此方法注入 BeanFactory。
     *
     * @param beanFactory Spring 容器的 BeanFactory。
     */
    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = (DefaultListableBeanFactory) beanFactory;
    }

    /**
     * 在 Bean 实例化之前进行后处理。
     * 用于创建自定义 TargetSource代理对象
     * 跳过默认生命周期：直接返回代理对象后，Spring 会跳过该 Bean 的默认实例化、依赖注入和初始化流程，由代理完全控制目标对象的行为。
     *
     * @param beanClass Bean 的类。
     * @param beanName  Bean 的名称。
     * @return 返回代理对象，如果没有代理则返回 null。
     */
    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) {

        return null;
    }

    /**
     * 判断指定的类是否是基础设施类。
     * 基础设施类包括 Advice、Pointcut 和 Advisor 类，这些类用于 AOP 的配置和实现。
     *
     * @param beanClass 要判断的类。
     * @return 如果是基础设施类则返回 true，否则返回 false。
     */
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

        return wrapIfNecessary(bean, beanName);

    }

    /**
     * 根据需要包装 Bean。
     * 如果 Bean 需要被代理，则创建并返回代理对象；否则返回原始 Bean。
     *
     * @param bean     当前 Bean 实例。
     * @param beanName 当前 Bean 的名称。
     * @return 返回可能被包装过的 Bean 实例。
     */
    protected Object wrapIfNecessary(Object bean, String beanName){

        Class<?> beanClass = bean.getClass();
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
                    Object beanInstantiate = beanFactory.getInstantiationStrategy().instantiate(beanDefinition);
                    TargetSource targetSource = new TargetSource(beanInstantiate);

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

        return bean;
    }
}

