package org.qlspringframework.beans.factory.supper;

import org.qlspringframework.beans.factory.BeanException;
import org.qlspringframework.beans.factory.config.BeanDefinition;

/**
 * @description: 主要负责Bean的创建逻辑
 * @author: jixu
 * @create: 2025-03-28 16:42
 **/
public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory{

    private InstantiationStrategy instantiationStrategy = new SimpleInstantiationStrategy();

    /**
     * @param name Bean名称
     * @param beanDefinition Bean的定义信息
     * @return Bean实列
     */
    @Override
    protected Object createBean(String name, BeanDefinition beanDefinition) {
        return doCreateBean(name , beanDefinition);
    }


    /**
     * 执行具体创建Bean的逻辑
     *
     * 如何创建Bean？
     * 通过beanDefinition当中保存的Bean的Class对象，通过反射的方式创建Bean
     */
    private Object doCreateBean(String name, BeanDefinition beanDefinition) throws BeanException {


        // 通过反射创建对象
        Object bean = null;
        try {
            // 通过InstantiationStrategy实例化Bean
            bean = createBeanInstance(beanDefinition);
        } catch (Exception e) {
            throw new BeanException(e.getMessage());
        }

        // 创建完毕后加入缓存
        super.addSingletonBean(name , bean);

        return bean;
    }


    /**
     * 创建并返回一个Bean实例
     * 此方法根据Bean定义来实例化Bean，具体实例化策略由获取到的实例化策略决定
     *
     * @param beanDefinition Bean的定义，包含了创建Bean实例所需的信息
     * @return 实例化的Bean对象
     */
    private Object createBeanInstance(BeanDefinition beanDefinition) {
        return getInstantiationStrategy().instantiate(beanDefinition);
    }


    public InstantiationStrategy getInstantiationStrategy() {
        return instantiationStrategy;
    }

    public void setInstantiationStrategy(InstantiationStrategy instantiationStrategy) {
        this.instantiationStrategy = instantiationStrategy;
    }
}
