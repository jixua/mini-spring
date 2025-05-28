package org.qlspringframework.beans.factory.config;

/**
 * @author jixu
 * @title InstantiationAwareBeanPostProcessor
 * @date 2025/5/28 15:45
 */
public interface InstantiationAwareBeanPostProcessor extends BeanPostProcessor{



    Object postProcessBeforeInstantiation(Class<?> beanClass , String beanName);


}
