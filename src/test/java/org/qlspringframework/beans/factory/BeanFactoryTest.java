package org.qlspringframework.beans.factory;

import org.junit.Test;
import org.qlspringframework.beans.PropertyValue;
import org.qlspringframework.beans.PropertyValues;
import org.qlspringframework.beans.factory.config.BeanDefinition;
import org.qlspringframework.beans.factory.supper.DefaultListableBeanFactory;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author jixu
 * @title BeanFactoryTest
 * @date 2025/4/5 18:30
 * @description
 */
public class BeanFactoryTest {

    @Test
    public void testBeanFactory() {
        // BeanDefinition工厂 --》 用于注册Bean
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        BeanDefinition beanDefinition = new BeanDefinition(Bean.class);
        beanFactory.registerBeanDefinition( "bean",beanDefinition);
        Bean bean = (Bean) beanFactory.getBean("bean");
        System.out.println(bean);
    }

    @Test
    public void testBeanProperty() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        PropertyValues propertyValues = new PropertyValues();
        propertyValues.addPropertyValue(new PropertyValue("foo", "hello"));
        propertyValues.addPropertyValue(new PropertyValue("bar", "world"));
        BeanDefinition beanDefinition = new BeanDefinition(HelloService.class, propertyValues);
        beanFactory.registerBeanDefinition("helloService", beanDefinition);

        HelloService helloService = (HelloService) beanFactory.getBean("helloService");
        System.out.println(helloService.toString());
        assertThat(helloService.getFoo()).isEqualTo("hello");
        assertThat(helloService.getBar()).isEqualTo("world");
    }
}
