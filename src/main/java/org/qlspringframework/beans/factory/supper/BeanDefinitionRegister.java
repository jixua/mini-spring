package org.qlspringframework.beans.factory.supper;

import org.qlspringframework.beans.factory.config.BeanDefinition;

/**
 * Bean定义注册接口
 * 该接口主要用于在IoC容器中注册Bean定义
 * 它允许用户将自定义的Bean定义添加到容器中，以便容器管理和初始化这些Bean
 *
 * @author: jixu
 * @create: 2025-03-28 17:04
 **/
public interface BeanDefinitionRegister {

    /**
     * 注册Bean定义到IoC容器中
     *
     * @param beanName Bean的名称，用于唯一标识一个Bean
     * @param beanDefinition Bean的定义，包含Bean的元数据，如类名、作用域、依赖关系等
     */
    void registerBeanDefinition(String beanName , BeanDefinition beanDefinition);
}
