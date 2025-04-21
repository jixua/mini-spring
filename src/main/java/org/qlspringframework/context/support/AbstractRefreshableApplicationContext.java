package org.qlspringframework.context.support;

import org.qlspringframework.beans.factory.ConfigurableListableBeanFactory;
import org.qlspringframework.beans.factory.supper.DefaultListableBeanFactory;

/**
 * @description:
 * @author: jixu
 * @create: 2025-04-21 17:12
 **/
public abstract class AbstractRefreshableApplicationContext extends AbstractApplicationContext{



    private ConfigurableListableBeanFactory  beanFactory;


    @Override
    protected void refreshBeanFactory() {
        // 创建Bean
        DefaultListableBeanFactory beanFactory = createBeanFactory();
        // 加载BeanDefinition
        loadBeanDefinitions(beanFactory);
        this.beanFactory = beanFactory;
    }

    protected abstract void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) ;

    private DefaultListableBeanFactory createBeanFactory() {
        DefaultListableBeanFactory defaultListableBeanFactory = new DefaultListableBeanFactory();
        return defaultListableBeanFactory;
    }


    @Override
    public ConfigurableListableBeanFactory getBeanFactory() {
        return beanFactory;
    }

    public void setBeanFactory(DefaultListableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }


}
