package org.qlspringframework.aop.framework.autoproxy;

import org.qlspringframework.beans.factory.BeanFactoryAware;
import org.qlspringframework.beans.factory.config.SmartInstantiationAwareBeanPostProcessor;

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

