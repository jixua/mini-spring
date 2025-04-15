package org.qlspringframework.beans.factory.supper;

import org.qlspringframework.beans.BeanException;
import org.qlspringframework.beans.factory.config.BeanDefinition;
import org.qlspringframework.beans.factory.config.BeanPostProcessor;
import org.qlspringframework.beans.factory.config.ConfigurableBeanFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: 抽象的Bean工厂，实现BeanFactory的相关功能
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
     * @param beanPostProcessor
     */
    @Override
    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
        this.beanPostProcessors.remove(beanPostProcessor);
        this.beanPostProcessors.add(beanPostProcessor);
    }

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
            throw new BeanException("beanDefinition is null");
        }

        // 创建Bean
        return createBean(beanName , beanDefinition);

    }


    protected abstract Object createBean(String beanName, BeanDefinition beanDefinition);

    protected abstract BeanDefinition getBeanDefinition(String beanName) ;


}
