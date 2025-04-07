package org.qlspringframework.beans.ioc;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.qlspringframework.beans.PropertyValue;
import org.qlspringframework.beans.PropertyValues;
import org.qlspringframework.beans.factory.config.BeanDefinition;
import org.qlspringframework.beans.factory.config.BeanReference;
import org.qlspringframework.beans.factory.supper.DefaultListableBeanFactory;
import org.qlspringframework.beans.ioc.bean.Car;
import org.qlspringframework.beans.ioc.bean.People;

/**
 * @author jixu
 * @title BeanPropertyValueTest
 * @date 2025/4/7 10:08
 */
public class BeanPropertyValueTest {
    @Test
    public void  testBeanPropertyValue(){
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        PropertyValues propertyValues = new PropertyValues();
        propertyValues.addPropertyValue(new PropertyValue("age",20));
        propertyValues.addPropertyValue(new PropertyValue("name","jixu"));
        BeanDefinition beanDefinition = new BeanDefinition(People.class, propertyValues);
        factory.registerBeanDefinition("people",beanDefinition);
        People people = (People) factory.getBean("people");
        System.out.println(people.toString());
        Assertions.assertThat(people.getAge()).isEqualTo(20);
        Assertions.assertThat(people.getName()).isEqualTo("jixu");

    }

    @Test
    public void testBeanPropertyReference(){
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        PropertyValues carValues = new PropertyValues();
        carValues.addPropertyValue(new PropertyValue("name","Xiaomi"));
        BeanDefinition carBeanDefinition = new BeanDefinition(Car.class, carValues);
        factory.registerBeanDefinition("car",carBeanDefinition);
        PropertyValues propertyValues = new PropertyValues();
        propertyValues.addPropertyValue(new PropertyValue("age",20));
        propertyValues.addPropertyValue(new PropertyValue("name","jixu"));
        propertyValues.addPropertyValue(new PropertyValue("car",new BeanReference("car")));
        BeanDefinition peopleBeanDefinition = new BeanDefinition(People.class, propertyValues);
        factory.registerBeanDefinition("people",peopleBeanDefinition);
        People people = (People) factory.getBean("people");
        System.out.println(people.toString());
        Assertions.assertThat(people.getAge()).isEqualTo(20);
        Assertions.assertThat(people.getName()).isEqualTo("jixu");

    }
}
