package org.qlspringframework.beans.factory.config;

import org.qlspringframework.beans.factory.ConfigurableListableBeanFactory;

/**
 * BeanFactoryPostProcessor 是一个接口，用于在 Spring 容器实例化所有 bean 之后，但在 bean 初始化之前，
 * 对 BeanFactory 进行后处理。实现该接口的类可以通过 `postProcessBeanFactory` 方法对 BeanFactory 进行自定义的修改或扩展。
 *
 * @author: jixu
 * @create: 2025-04-11 16:36
 **/
public interface BeanFactoryPostProcessor {

    /**
     * 对 BeanFactory 进行后处理的方法。该方法在 Spring 容器实例化所有 bean 之后，但在 bean 初始化之前被调用。
     * 实现类可以通过该方法对 BeanFactory 进行自定义的修改或扩展。
     *
     * @param beanFactory 可配置的 BeanFactory 实例，允许对 bean 定义进行修改或扩展。
     */
    void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory);
}
