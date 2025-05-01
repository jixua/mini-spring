package org.qlspringframework.context.support;

import org.qlspringframework.beans.factory.ConfigurableListableBeanFactory;
import org.qlspringframework.beans.factory.supper.DefaultListableBeanFactory;

/**
 * 抽象可刷新应用上下文类，提供了一个标准的应用上下文实现，
 * 主要负责管理Bean的生命周期和依赖关系
 *
 * @author: jixu
 * @create: 2025-04-21 17:12
 **/
public abstract class AbstractRefreshableApplicationContext extends AbstractApplicationContext{

    // 应用上下文中使用的Bean工厂
    private ConfigurableListableBeanFactory  beanFactory;

    /**
     * 刷新Bean工厂，主要用于创建和初始化Bean
     * 这是应用上下文启动过程中的核心方法之一
     */
    @Override
    protected void refreshBeanFactory() {
        // 创建Bean
        DefaultListableBeanFactory beanFactory = createBeanFactory();
        // 加载BeanDefinition
        loadBeanDefinitions(beanFactory);
        this.beanFactory = beanFactory;
    }

    /**
     * 抽象方法，由子类实现，用于加载Bean定义
     *
     * @param beanFactory Bean工厂，用于生产Bean实例
     */
    protected abstract void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) ;

    /**
     * 创建Bean工厂的方法
     *
     * @return 返回一个新创建的DefaultListableBeanFactory实例
     */
    private DefaultListableBeanFactory createBeanFactory() {
        DefaultListableBeanFactory defaultListableBeanFactory = new DefaultListableBeanFactory();
        return defaultListableBeanFactory;
    }

    /**
     * 获取Bean工厂
     *
     * @return 返回当前应用上下文中使用的ConfigurableListableBeanFactory实例
     */
    @Override
    public ConfigurableListableBeanFactory getBeanFactory() {
        return beanFactory;
    }

    /**
     * 设置Bean工厂
     *
     * @param beanFactory 要设置的Bean工厂
     */
    public void setBeanFactory(DefaultListableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }
    
}
