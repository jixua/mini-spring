package org.qlspringframework.beans.factory.supper;

import org.qlspringframework.beans.factory.config.BeanDefinition;

/**
 * @description: BeanDefiniton注册
 * @author: jixu
 * @create: 2025-03-28 17:04
 **/
public interface BeanDefinitionRegister {

    /**
     *  注册Bean
     * @param beanName
     * @param beanDefinition
     */
    void rigisterBeanDefinition(String beanName , BeanDefinition beanDefinition);
}
