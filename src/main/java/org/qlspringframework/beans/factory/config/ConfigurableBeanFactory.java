package org.qlspringframework.beans.factory.config;

import org.qlspringframework.beans.factory.BeanFactory;

/**
 * @description:
 * @author: jixu
 * @create: 2025-04-11 15:42
 **/
public interface ConfigurableBeanFactory extends BeanFactory , SingletonBeanRegister {

    void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);
}
