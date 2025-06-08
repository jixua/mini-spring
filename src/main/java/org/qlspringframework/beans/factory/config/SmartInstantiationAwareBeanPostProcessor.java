package org.qlspringframework.beans.factory.config;

/**
 * @author jixu
 * @title SmartInstantiationAwareBeanPostProcessor
 * @date 2025/5/29 00:42
 */
public interface SmartInstantiationAwareBeanPostProcessor extends InstantiationAwareBeanPostProcessor{

    Object getEarlyBeanReference(Object bean,String beanName);
}
