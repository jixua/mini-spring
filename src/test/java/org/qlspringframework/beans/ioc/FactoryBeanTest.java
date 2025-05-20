package org.qlspringframework.beans.ioc;

import org.junit.Assert;
import org.junit.Test;
import org.qlspringframework.beans.ioc.bean.Car;
import org.qlspringframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author jixu
 * @title FactoryBeanTest
 * @date 2025/5/20 14:14
 */
public class FactoryBeanTest {

    @Test
    public void testFactoryBean(){
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:factory-bean.xml");
        Car car = applicationContext.getBean("car", Car.class);

        Assert.assertEquals(car.getName(),"aaa");

    }
}
