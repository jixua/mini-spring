package org.qlspringframework.beans.factory.supper;

import org.qlspringframework.beans.factory.ConfigurableListableBeanFactory;
import org.qlspringframework.beans.factory.config.BeanDefinition;

import java.util.HashMap;
import java.util.Map;

/**
 * @description: 管理 Bean 的生命周期（创建、依赖注入、缓存等）
 * @author: jixu
 * @create: 2025-03-28 17:06
 **/
public class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory implements BeanDefinitionRegister , ConfigurableListableBeanFactory {


    private Map<String , BeanDefinition> beanDefinitionMap = new HashMap<>();



    /**
     * 注册BeanDefinition
     *
     * @param beanName
     * @param beanDefinition
     */
    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        beanDefinitionMap.put(beanName,beanDefinition);
    }


    /**
     * 获取BeanDefinition
     *
     * @param beanName
     * @return
     */
    @Override
    protected BeanDefinition getBeanDefinition(String beanName) {
        return beanDefinitionMap.get(beanName);
    }


}
