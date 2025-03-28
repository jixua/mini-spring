package org.qlspringframework.beans.factory.config;

/**
 * @description: 注册单列Bean
 * @author: jixu
 * @create: 2025-03-28 15:39
 **/
public interface SingletonBeanRegister {

    /**
     * 获取单列Bean
     * @param beanName Bean名称
     * @return Bean对象
     */
    Object getSingletonBean(String beanName);
}
