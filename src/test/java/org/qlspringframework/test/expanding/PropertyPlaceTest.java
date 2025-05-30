package org.qlspringframework.test.expanding;

import org.junit.Assert;
import org.junit.Test;
import org.qlspringframework.test.bean.Car;
import org.qlspringframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author jixu
 * @title PropertyPlaceTest
 * @date 2025/5/31 01:20
 */
public class PropertyPlaceTest {

    @Test
    public void testPropertyPlace(){
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:property-place.xml");
        Car car = applicationContext.getBean("car", Car.class);
        Assert.assertEquals(car.getName(),"aaa");
    }
}
