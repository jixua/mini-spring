package org.qlspringframework.beans.factory.config;

/**
 * @description: BeanPostProcessor 是一个接口，用于在 Spring 容器中 Bean 的初始化前后进行自定义处理。
 * @author: jixu
 * @create: 2025-04-11 16:35
 **/
public interface BeanPostProcessor {

    /**
     * 在 Bean 初始化之前执行自定义处理逻辑。
     *
     * @param bean     当前正在初始化的 Bean 实例。
     * @param beanName 当前 Bean 的名称。
     * @return 返回处理后的 Bean 实例，可以是原始 Bean 或修改后的 Bean。
     */
    Object postProcessBeforeInitialization(Object bean , String beanName);

    /**
     * 在 Bean 初始化之后执行自定义处理逻辑。
     *
     * @param bean     当前已经初始化的 Bean 实例。
     * @param beanName 当前 Bean 的名称。
     * @return 返回处理后的 Bean 实例，可以是原始 Bean 或修改后的 Bean。
     */
    Object postProcessAfterInitialization(Object bean , String beanName);
}
