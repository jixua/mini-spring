package org.qlspringframework.beans.factory.supper;

import org.qlspringframework.beans.BeansException;
import org.qlspringframework.beans.factory.config.BeanDefinition;
import org.qlspringframework.beans.factory.config.BeanPostProcessor;
import org.qlspringframework.beans.factory.config.ConfigurableBeanFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 抽象Bean工厂类，继承了DefaultSingletonBeanRegistry以支持单例Bean的注册和管理，
 * 同时实现了ConfigurableBeanFactory接口以支持配置和依赖注入功能。
 *
 * @author: jixu
 * @create: 2025-03-28 15:47
 **/
public abstract class AbstractBeanFactory extends DefaultSingletonBeanRegistry implements ConfigurableBeanFactory {

    /**
     * BeanPostProcess是对指定Bean的增强，可以定义多个processors
     */
    private final List<BeanPostProcessor> beanPostProcessors  = new ArrayList<>();

    /**
     * 添加beanPostProcessor
     * @param beanPostProcessor 要添加的BeanPostProcessor实例
     */
    @Override
    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
        this.beanPostProcessors.remove(beanPostProcessor);
        this.beanPostProcessors.add(beanPostProcessor);
    }

    /**
     * 获取所有的BeanPostProcessor
     * @return 包含所有BeanPostProcessor的List
     */
    public List<BeanPostProcessor> getBeanPostProcessors(){
        return this.beanPostProcessors;
    }

    /**
     * 获取Bean
     * 包含创建Bean的流程，在创建Bean的流程当中会先从缓存当中取，如果没有则创建
     * 在获取Bean之前需要获取到Bean的定义信息也就是BeanDefinition
     * 1，从缓存当中获取Bean
     * 2，尝试创建Bean并返回
     *
     * @param beanName Bean名称
     * @return 创建的Bean实例
     */
    @Override
    public Object getBean(String beanName) {
        // 尝试从缓存当中获取Bean
        Object bean = super.getSingletonBean(beanName);
        if (bean != null){
            return bean;
        }

        // 如果没有尝试创建Bean,Bean的创建需要通过BeanDefinition
        BeanDefinition beanDefinition = getBeanDefinition(beanName);

        if (beanDefinition == null){
            throw new BeansException("beanDefinition 为空");
        }

        // 创建Bean
        return createBean(beanName , beanDefinition);

    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) {
        return  ((T) getBean(name));

    }

    /**
     * 创建Bean的抽象方法，由子类实现具体逻辑
     *
     * @param beanName Bean名称
     * @param beanDefinition Bean定义信息
     * @return 创建的Bean实例
     */
    protected abstract Object createBean(String beanName, BeanDefinition beanDefinition);

    /**
     * 获取Bean定义信息的抽象方法，由子类实现具体逻辑
     *
     * @param beanName Bean名称
     * @return Bean定义信息
     */
    protected abstract BeanDefinition getBeanDefinition(String beanName) ;

}
