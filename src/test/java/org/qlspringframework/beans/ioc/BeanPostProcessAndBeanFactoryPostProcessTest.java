package org.qlspringframework.beans.ioc;

import org.junit.Test;
import org.qlspringframework.beans.factory.supper.DefaultListableBeanFactory;
import org.qlspringframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.qlspringframework.beans.ioc.bean.People;
import org.qlspringframework.beans.ioc.common.CustomerBeanFactoryPostProcessor;
import org.qlspringframework.beans.ioc.common.CustomerBeanPostProcessor;

/**
 * 测试类，用于验证Spring框架中的BeanPostProcessor和BeanFactoryPostProcessor的功能。
 *
 * @author: jixu
 * @create: 2025-04-15 13:08
 **/
public class BeanPostProcessAndBeanFactoryPostProcessTest {

    /**
     * 测试BeanPostProcessor的功能。
     * 该方法通过加载Spring配置文件，注册自定义的BeanPostProcessor，并获取Bean实例，
     * 验证BeanPostProcessor是否能够对Bean进行后置处理。
     */
    @Test
    public void testBeanPostPostProcess() {
        // 创建默认的BeanFactory实例
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();

        // 创建XML Bean定义读取器，并加载指定的Spring配置文件
        XmlBeanDefinitionReader xmlBeanDefinitionReader = new XmlBeanDefinitionReader(factory);
        xmlBeanDefinitionReader.loadBeanDefinitions("classpath:spring.xml");

        // 向BeanFactory注册自定义的BeanPostProcessor
        factory.addBeanPostProcessor(new CustomerBeanPostProcessor());

        // 从BeanFactory中获取名为"people"的Bean实例，并打印其内容
        People people = (People) factory.getBean("people");
        System.out.println(people);
    }

    @Test
    public void testBeanFactoryPostProcess() {
        // 创建默认的BeanFactory实例
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();

        // 创建XML Bean定义读取器，并加载指定的Spring配置文件
        XmlBeanDefinitionReader xmlBeanDefinitionReader = new XmlBeanDefinitionReader(factory);
        xmlBeanDefinitionReader.loadBeanDefinitions("classpath:spring.xml");

        People people = (People) factory.getBean("people");
        System.out.println(people);

        CustomerBeanFactoryPostProcessor customerBeanFactoryPostProcessor = new CustomerBeanFactoryPostProcessor();
        customerBeanFactoryPostProcessor.postProcessBeanFactory(factory);

        people = (People) factory.getBean("people");
        System.out.println(people);


    }
}

