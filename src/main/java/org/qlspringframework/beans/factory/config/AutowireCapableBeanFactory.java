package org.qlspringframework.beans.factory.config;
/**
 * @description: BeanPostProcessors的前置后置方法接口
 * @author: jixu
 * @create: 2025-04-11 15:18
 **/
public interface AutowireCapableBeanFactory {

    /**
     * 在Bean初始化之前执行BeanPostProcessors的增强方法
     *
     * @param existingBean 当前已经存在的Bean实例
     * @param beanName     Bean的名称
     * @return 经过所有BeanPostProcessors处理后的Bean实例
     */
    Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName);

    /**
     * 在Bean初始化之后执行BeanPostProcessors增强方法
     *
     * @param existingBean 当前已经存在的Bean实例
     * @param beanName     Bean的名称
     * @return 经过所有BeanPostProcessors处理后的Bean实例
     */
    Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName);
}
