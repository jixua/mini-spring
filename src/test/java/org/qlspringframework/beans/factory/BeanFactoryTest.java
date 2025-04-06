package org.qlspringframework.beans.factory;

import org.qlspringframework.beans.factory.config.BeanDefinition;
import org.qlspringframework.beans.factory.supper.DefaultListableBeanFactory;

/**
 * @author jixu
 * @title BeanFactoryTest
 * @date 2025/4/5 18:30
 * @description
 */
public class BeanFactoryTest {

    public static void main(String[] args) {
        // BeanDefinition工厂 --》 用于注册Bean
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        BeanDefinition beanDefinition = new BeanDefinition(Bean.class);
        beanFactory.rigisterBeanDefinition( "bean",beanDefinition);
        /**
         * DefaultListableBeanFactory是BeanFactory的子类
         * DefaultListableBeanFactory当中
         */
        Bean bean = (Bean) beanFactory.getBean("bean");

    }
}
