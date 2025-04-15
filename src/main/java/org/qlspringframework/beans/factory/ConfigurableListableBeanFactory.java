package org.qlspringframework.beans.factory;

import org.qlspringframework.beans.factory.config.BeanDefinition;

/**
 * ConfigurableListableBeanFactory 接口扩展了 ListableBeanFactory 接口，提供了对 Bean 工厂的配置和列表功能。
 * 该接口允许对 Bean 工厂进行更细粒度的控制，包括 Bean 的定义、依赖注入、生命周期管理等。
 *
 * @author: jixu
 * @create: 2025-04-11 15:00
 **/
public interface ConfigurableListableBeanFactory extends ListableBeanFactory {

    // 该接口目前没有定义具体的方法，但通常用于扩展 Bean 工厂的功能，
    // 例如配置 Bean 的定义、管理 Bean 的生命周期、处理 Bean 的依赖关系等。

    BeanDefinition getBeanDefinition(String beanNane);

}

