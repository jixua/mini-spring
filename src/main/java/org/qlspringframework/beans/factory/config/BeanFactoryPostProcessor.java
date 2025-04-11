package org.qlspringframework.beans.factory.config;

/**
 * @description:
 * @author: jixu
 * @create: 2025-04-11 16:36
 **/
public interface BeanFactoryPostProcessor {

    void postProcessBeanFactory(ConfigurableBeanFactory beanFactory);
}
