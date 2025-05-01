package org.qlspringframework.context.support;

import org.qlspringframework.beans.factory.supper.DefaultListableBeanFactory;
import org.qlspringframework.beans.factory.xml.XmlBeanDefinitionReader;

/**
 * 抽象的XML应用上下文类，继承自AbstractRefreshableApplicationContext
 * 该类的主要职责是加载Bean定义，并处理配置资源路径
 *
 * @author: jixu
 * @create: 2025-04-21 17:19
 **/
public abstract class AbstractXmlApplicationContext extends AbstractRefreshableApplicationContext{

    /**
     * 加载Bean定义的方法
     * 该方法通过XmlBeanDefinitionReader读取配置资源路径，并加载Bean定义到beanFactory中
     *
     * @param beanFactory DefaultListableBeanFactory类型的参数，用于存储加载的Bean定义
     */
    @Override
    protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) {
        // 创建XmlBeanDefinitionReader实例，用于解析XML配置文件
        XmlBeanDefinitionReader xmlBeanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);
        // 获取配置资源路径数组
        String[] locations = getConfigLocations();
        // 使用XmlBeanDefinitionReader加载Bean定义
        xmlBeanDefinitionReader.loadBeanDefinitions(locations);
    }

    /**
     * 获取配置资源路径的抽象方法
     * 子类必须实现该方法，以提供具体的配置资源路径
     *
     * @return 返回一个字符串数组，包含所有的配置资源路径
     */
    protected abstract String[] getConfigLocations();
}
