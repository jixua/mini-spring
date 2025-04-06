package org.qlspringframework.beans.factory.supper;

import org.qlspringframework.beans.factory.BeanException;
import org.qlspringframework.beans.factory.config.BeanDefinition;

import java.lang.reflect.Constructor;

/**
 * @author jixu
 * @title SimpleInstantiationStrategy
 * @date 2025/4/5 19:53
 */
// SimpleInstantiationStrategy类实现了InstantiationStrategy接口，用于实例化Bean对象
public class SimpleInstantiationStrategy implements InstantiationStrategy{
    /**
     * 实例化Bean
     * @param beanDefinition Bean的定义信息，包含Bean的类和其他元数据
     * @return 实例化的Bean对象
     */
    @Override
    public Object instantiate(BeanDefinition beanDefinition) {
        // 获取BeanDefinition中定义的类
        Class aClass = beanDefinition.getBeanClass();
        try {
            // 获取无参构造
            Constructor declaredConstructor = aClass.getDeclaredConstructor();
            // 使用无参构造实例化对象并返回
            return declaredConstructor.newInstance();
        } catch (Exception e) {
            // 如果实例化过程中发生异常，抛出BeanException
            throw new BeanException(e.getMessage());
        }
    }
}

