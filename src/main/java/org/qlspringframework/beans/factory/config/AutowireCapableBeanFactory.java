package org.qlspringframework.beans.factory.config;

/**
 * 定义了一个接口，用于 Bean 的自动装配和生命周期管理
 * 主要负责在 Bean 初始化前后应用 BeanPostProcessor 的增强方法
 */
public interface AutowireCapableBeanFactory {

    /**
     * 在 Bean 初始化之前执行 BeanPostProcessors 的增强方法
     *
     * @param existingBean 当前已经存在的 Bean 实例
     * @param beanName     Bean 的名称
     * @return 经过所有 BeanPostProcessors 处理后的 Bean 实例
     */
    Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName);

    /**
     * 在 Bean 初始化之后执行 BeanPostProcessors 增强方法
     *
     * @param existingBean 当前已经存在的 Bean 实例
     * @param beanName     Bean 的名称
     * @return 经过所有 BeanPostProcessors 处理后的 Bean 实例
     */
    Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName);
}
