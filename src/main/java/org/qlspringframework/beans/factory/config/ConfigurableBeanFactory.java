package org.qlspringframework.beans.factory.config;

import org.qlspringframework.beans.factory.BeanFactory;
import org.qlspringframework.beans.factory.HierarchicalBeanFactory;

/**
 * ConfigurableBeanFactory 接口扩展了 BeanFactory 和 SingletonBeanRegister 接口，
 * 提供了对 Bean 工厂的配置能力，特别是允许添加 BeanPostProcessor。
 *
 * @author jixu
 * @create 2025-04-11 15:42
 */
public interface ConfigurableBeanFactory extends HierarchicalBeanFactory, SingletonBeanRegistry {

    /**
     * 添加一个 BeanPostProcessor 到工厂中。
     * BeanPostProcessor 可以在 Bean 初始化前后执行自定义逻辑。
     *
     * @param beanPostProcessor 要添加的 BeanPostProcessor 实例
     */
    void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);


    /**
     * 销毁单例bean
     */
    void destroySingletons();
}
