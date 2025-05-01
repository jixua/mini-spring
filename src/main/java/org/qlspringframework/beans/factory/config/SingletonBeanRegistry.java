package org.qlspringframework.beans.factory.config;

/**
 * 单例Bean注册表接口，用于管理和访问单例Bean
 * @author: jixu
 * @create: 2025-03-28 15:39
 **/
public interface SingletonBeanRegistry {

    /**
     * 获取单列Bean
     * @param beanName Bean名称
     * @return Bean对象
     */
    Object getSingletonBean(String beanName);

    /**
     * 添加单列Bean
     *
     * @param beanName Bean名称
     * @param bean Bean对象
     */
    void addSingletonBean(String beanName, Object bean);
}
