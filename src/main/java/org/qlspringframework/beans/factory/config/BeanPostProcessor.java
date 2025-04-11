package org.qlspringframework.beans.factory.config;

/**
 * @description:
 * @author: jixu
 * @create: 2025-04-11 16:35
 **/
public interface BeanPostProcessor {

    Object postProcessBeforeInitialization(Object bean , String beanName);


    Object postProcessAfterInitialization(Object bean , String beanName);
}
