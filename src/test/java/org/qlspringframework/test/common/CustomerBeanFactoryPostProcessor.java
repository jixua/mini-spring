package org.qlspringframework.test.common;

import org.qlspringframework.beans.PropertyValue;
import org.qlspringframework.beans.PropertyValues;
import org.qlspringframework.beans.factory.ConfigurableListableBeanFactory;
import org.qlspringframework.beans.factory.config.BeanDefinition;
import org.qlspringframework.beans.factory.config.BeanFactoryPostProcessor;

/**
 * @description:
 * @author: jixu
 * @create: 2025-04-15 13:29
 **/
public class CustomerBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    /**
     * 对 BeanFactory 进行后处理的方法。该方法在 Spring 容器实例化所有 bean 之后，但在 bean 初始化之前被调用。
     * 实现类可以通过该方法对 BeanFactory 进行自定义的修改或扩展。
     *
     * @param beanFactory 可配置的 BeanFactory 实例，允许对 bean 定义进行修改或扩展。
     */
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        BeanDefinition beanDefinition = beanFactory.getBeanDefinition("people");
        PropertyValues propertyValues = beanDefinition.getPropertyValues();
        propertyValues.addPropertyValue(new PropertyValue("name","ji"));
    }
}
