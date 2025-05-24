package org.qlspringframework.beans.ioc;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.qlspringframework.beans.PropertyValue;
import org.qlspringframework.beans.PropertyValues;
import org.qlspringframework.beans.factory.config.BeanDefinition;
import org.qlspringframework.beans.factory.config.BeanReference;
import org.qlspringframework.beans.factory.supper.DefaultListableBeanFactory;
import org.qlspringframework.beans.bean.Car;
import org.qlspringframework.beans.bean.People;

/**
 * Bean属性值和引用的测试类。
 * 该类包含两个测试方法，一个用于测试Bean的属性值，另一个用于测试Bean的属性引用。
 * @author jixu
 * @title BeanPropertyValueTest
 * @date 2025/4/7 10:08
 */
public class BeanPropertyValueTest {

    /**
     * 测试Bean属性值的设置和获取。
     * 该测试方法通过DefaultListableBeanFactory注册一个People类的Bean定义，
     * 并为其设置属性值（age和name），然后通过工厂获取该Bean并验证属性值是否正确。
     */
    @Test
    public void testBeanPropertyValue() {
        // 创建Bean工厂
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();

        // 创建属性值集合，并添加age和name属性
        PropertyValues propertyValues = new PropertyValues();
        propertyValues.addPropertyValue(new PropertyValue("age", 20));
        propertyValues.addPropertyValue(new PropertyValue("name", "jixu"));

        // 创建People类的Bean定义，并注册到工厂中
        BeanDefinition beanDefinition = new BeanDefinition(People.class, propertyValues);
        factory.registerBeanDefinition("people", beanDefinition);

        // 从工厂中获取People Bean，并验证其属性值
        People people = (People) factory.getBean("people");
        System.out.println(people.toString());
        Assertions.assertThat(people.getAge()).isEqualTo(20);
        Assertions.assertThat(people.getName()).isEqualTo("jixu");
    }

    /**
     * 测试Bean属性引用。
     * 该测试方法通过DefaultListableBeanFactory注册两个Bean定义：Car和People。
     * People Bean的car属性引用Car Bean，验证属性引用是否正确。
     */
    @Test
    public void testBeanPropertyReference() {
        // 创建Bean工厂
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();

        // 创建Car Bean的属性值集合，并添加name属性
        PropertyValues carValues = new PropertyValues();
        carValues.addPropertyValue(new PropertyValue("name", "Xiaomi"));

        // 创建Car类的Bean定义，并注册到工厂中
        BeanDefinition carBeanDefinition = new BeanDefinition(Car.class, carValues);
        factory.registerBeanDefinition("car", carBeanDefinition);

        // 创建People Bean的属性值集合，并添加age、name和car属性（car属性引用Car Bean）
        PropertyValues propertyValues = new PropertyValues();
        propertyValues.addPropertyValue(new PropertyValue("age", 20));
        propertyValues.addPropertyValue(new PropertyValue("name", "jixu"));
        propertyValues.addPropertyValue(new PropertyValue("car", new BeanReference("car")));

        // 创建People类的Bean定义，并注册到工厂中
        BeanDefinition peopleBeanDefinition = new BeanDefinition(People.class, propertyValues);
        factory.registerBeanDefinition("people", peopleBeanDefinition);

        // 从工厂中获取People Bean，并验证其属性值
        People people = (People) factory.getBean("people");
        System.out.println(people.toString());
        Assertions.assertThat(people.getAge()).isEqualTo(20);
        Assertions.assertThat(people.getName()).isEqualTo("jixu");
    }
}
