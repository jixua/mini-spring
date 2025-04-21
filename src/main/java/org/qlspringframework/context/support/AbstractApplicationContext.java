package org.qlspringframework.context.support;

import org.qlspringframework.beans.factory.ConfigurableListableBeanFactory;
import org.qlspringframework.context.ConfigurableApplicationContext;
import org.qlspringframework.core.io.DefaultResourceLoader;

/**
 * AbstractApplicationContext 是一个抽象类，实现了 ConfigurableApplicationContext 接口。
 * 该类提供了应用程序上下文的基本实现，特别是容器的刷新功能。
 *
 * @author: jixu
 * @create: 2025-04-19 14:37
 **/
public abstract class AbstractApplicationContext extends DefaultResourceLoader implements ConfigurableApplicationContext {

    /**
     * 刷新容器。
     * 该方法用于重新加载或刷新应用程序上下文中的配置和资源。
     * 通常用于在运行时动态更新应用程序的配置。
     *
     * 该方法是一个抽象方法的实现，具体刷新逻辑由子类提供。
     */
    @Override
    public void refresh() {
        // 通过子类创建BeanFactory，同时初始化beanDefinition
        refreshBeanFactory();
        // 获取到Bean工厂
        ConfigurableListableBeanFactory beanFactory = getBeanFactory();

        // 执行BeanPostProcess的方法



    }


    protected abstract void refreshBeanFactory();

    protected abstract ConfigurableListableBeanFactory  getBeanFactory();

}

