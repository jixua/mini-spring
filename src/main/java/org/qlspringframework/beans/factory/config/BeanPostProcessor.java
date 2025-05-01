package org.qlspringframework.beans.factory.config;

/**
 * BeanPostProcessor 接口用于在 Bean 初始化前后执行自定义处理逻辑。
 * 实现此接口的类可以在 Bean 初始化之前或之后执行一些操作，以便于自定义 Bean 的创建过程。
 *
 * @author: jixu
 * @create: 2025-04-11 16:35
 **/
public interface BeanPostProcessor {

    /**
     * 在 Bean 初始化之前执行自定义处理逻辑。
     * 使用此方法，可以在 Bean 被初始化之前对其进行修改或执行其他操作。
     *
     * @param bean     当前正在初始化的 Bean 实例。
     * @param beanName 当前 Bean 的名称。
     * @return 返回处理后的 Bean 实例，可以是原始 Bean 或修改后的 Bean。
     */
    Object postProcessBeforeInitialization(Object bean , String beanName);

    /**
     * 在 Bean 初始化之后执行自定义处理逻辑。
     * 使用此方法，可以在 Bean 初始化完成后对其进行进一步的修改或执行其他操作。
     *
     * @param bean     当前已经初始化的 Bean 实例。
     * @param beanName 当前 Bean 的名称。
     * @return 返回处理后的 Bean 实例，可以是原始 Bean 或修改后的 Bean。
     */
    Object postProcessAfterInitialization(Object bean , String beanName);
}
