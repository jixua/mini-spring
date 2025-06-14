package org.qlspringframework.test.ioc;

import org.junit.Test;
import org.qlspringframework.beans.factory.config.BeanDefinition;
import org.qlspringframework.beans.factory.supper.DefaultListableBeanFactory;
import org.qlspringframework.test.service.HelloService;

/**
 * @author jixu
 * @title SimpleBeanFactoryTest
 * @date 2025/4/7 09:58
 */
public class SimpleBeanFactoryTest {

    @Test
    public void testBeanFactory() {
        // BeanDefinition工厂 --》 用于注册Bean
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        BeanDefinition beanDefinition = new BeanDefinition(HelloService.class);
        beanFactory.registerBeanDefinition( "HelloService",beanDefinition);
        HelloService helloService = (HelloService) beanFactory.getBean("HelloService");
        helloService.say();

    }
}
