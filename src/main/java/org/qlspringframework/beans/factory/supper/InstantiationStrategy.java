package org.qlspringframework.beans.factory.supper;

import org.qlspringframework.beans.factory.config.BeanDefinition;

/**
 * 实例化Bean的策略接口
 * 该接口定义了如何实例化Bean的策略方法
 * 主要用于在不同的场合下，根据Bean的定义信息来创建Bean实例
 *
 * @author jixu
 * @title InstantiationStrategy
 * @date 2025/4/5 19:52
 */
public interface InstantiationStrategy {
    /**
     * 根据Bean定义信息实例化Bean
     * 该方法根据传入的BeanDefinition对象中的信息来创建Bean实例
     * 主要解决了如何根据配置信息动态创建对象实例的问题
     *
     * @param beanDefinition Bean定义信息，包含Bean的类名、属性等信息
     * @return 实例化的Bean对象
     */
    Object instantiate(BeanDefinition beanDefinition);
}

