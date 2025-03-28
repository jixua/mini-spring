package org.qlspringframework.beans.factory.supper;

import org.qlspringframework.beans.factory.BeanFactory;

/**
 * @description: 抽象的Bean工厂，实现BeanFactory的相关功能
 * @author: jixu
 * @create: 2025-03-28 15:47
 **/
public abstract class AbstractBeanFactory extends DefaultSignletonBeanRegister implements BeanFactory {

    /**
     * 获取Bean
     * 在获取Bean之前需要获取到Bean的定义信息也就是BeanDefinition
     *
     * @param name Bean名称
     */
    @Override
    public void getBean(String name) {

    }
}
