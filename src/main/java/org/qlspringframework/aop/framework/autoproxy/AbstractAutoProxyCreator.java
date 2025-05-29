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
import org.qlspringframework.beans.factory.BeanFactoryAware;
import org.qlspringframework.beans.factory.config.BeanDefinition;
import org.qlspringframework.beans.factory.config.SmartInstantiationAwareBeanPostProcessor;
import org.qlspringframework.beans.factory.supper.DefaultListableBeanFactory;

import java.util.Collection;

/**
 * 抽象自动代理创建器类
 * 该类实现了SmartInstantiationAwareBeanPostProcessor和BeanFactoryAware接口，
 * 用于自动创建代理 bean，其主要作用是简化AOP（面向切面编程）的配置和使用
 *
 * @author jixu
 * @title AbstractAutoProxyCreator
 * @date 2025/5/29 00:44
 */
public abstract class AbstractAutoProxyCreator implements SmartInstantiationAwareBeanPostProcessor, BeanFactoryAware {

}

